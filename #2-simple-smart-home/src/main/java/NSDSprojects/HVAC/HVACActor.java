package NSDSprojects.HVAC;

import NSDSprojects.Messages.GenericMessages.*;
import NSDSprojects.Messages.HVAC.*;
import akka.actor.*;
import akka.japi.pf.DeciderBuilder;

import java.time.Duration;
import java.util.HashMap;

public class HVACActor extends AbstractActor {

    Cluster cluster = Cluster.get(getContext().getSystem());
    private int dE = 10;
    private HashMap<String, RoomInfoContainer> sensors = new HashMap<>();
    private float energyConsumption = 0;

    public void preStart(){
        cluster.subscribe(getSelf(), ClusterEvent.initialStateAsEvents(), MemberEvent.class, UnreachableMember.class);
    }

    public void postStop(){
        cluster.unsubscribe(getSelf());
    }

    private static SupervisorStrategy strategy =
            new OneForOneStrategy(
                10,
                Duration.ofMinutes(1),
                DeciderBuilder.match(Exception.class, e -> SupervisorStrategy.resume())
                        .build());


    public Receive createReceive() {
        return receiveBuilder()
                .match(TemperatureMessage.class, this::tempOperation)
                .match(SensorReplyMessage.class, this::checkSensor)

                .match(AddSensorMessage.class, this::addSensor)
                .match(RemoveSensorMessage.class, this::removeSensor)
                .match(RequestAllDeviceMessage.class, this::getRooms)
                .match(RequestEnergyConsumptionMessage.class, this::retrieveConsumption)

                .build();
    }

    void tempOperation(TemperatureMessage msg){


        if(sensors.containsKey(msg.getRoom())) {
            sensors.get(msg.getRoom()).setDesiredTemp(msg.getTemp());
            if (sensors.get(msg.getRoom()).getDesiredTemp() != msg.getTemp()) {
                if (sensors.get(msg.getRoom()).getTemp() > msg.getTemp()) {
                    sensors.get(msg.getRoom()).getRoomref().tell(new SensorOperationMessage(-1), self());
                } else {
                    sensors.get(msg.getRoom()).getRoomref().tell(new SensorOperationMessage(1), self());
                }
            } else {
                sender().tell(new WarningMessage("Room already has Temperature equal to DesiredTemp"), self());
            }
        } else {
            sender().tell(new WarningMessage("Room inserted doesnt exists!"), self());
        }
    }

    void checkSensor(SensorReplyMessage msg){
        if(msg.isActive()){
            this.energyConsumption += dE;
        }
        if(sensors.get(msg.getRoom()).getDesiredTemp() == msg.getTemp()){
            sender().tell(new SensorOperationMessage(0), self());
        }else {
            if(sensors.get(msg.getRoom()).getDesiredTemp() > msg.getTemp()){
                sender().tell(new SensorOperationMessage(1), self());
            }else {
                sender().tell(new SensorOperationMessage(-1), self());
            }
        }
    }

    void addSensor (AddSensorMessage msg){
        float initialTemp = (float)Math.random() * 25 + 10;
        ActorRef sens = getContext().actorOf(SensorActor.props(), msg.getDeviceid());
        sensors.put(msg.getDeviceid(), new RoomInfoContainer(sens, initialTemp, -999));
        sens.tell(new SetupMessage(msg.getDeviceid(), initialTemp), self());
    }

    void removeSensor (RemoveSensorMessage msg){
        if(sensors.containsKey(msg.getDeviceid())){
            getContext().stop(sensors.get(msg.getDeviceid()).getRoomref());
            sensors.remove(msg.getDeviceid());
        }else {
            sender().tell(new WarningMessage("Room inserted to be removed doesnt exists!"), self());
        }
    }

    void retrieveConsumption (RequestEnergyConsumptionMessage msg){
        sender().tell(new EnergyConsumptionMessage(this.energyConsumption, "HVAC"), self());
    }


    void getRooms(RequestAllDeviceMessage msg){
        sender().tell(new ReplyDevicesMessage(sensors.keySet()), self());
    }

    static Props props () {
        return Props.create(HVACActor.class);
    }



}
