package NSDSprojects.HVAC;

import NSDSprojects.CustomException;
import NSDSprojects.Messages.GenericMessages.*;
import NSDSprojects.Messages.HVAC.*;
import akka.actor.*;
import akka.japi.pf.DeciderBuilder;

import java.time.Duration;
import java.util.HashMap;
import java.util.Random;


public class HVACActor extends AbstractActor {
    private int dE = 10;
    private HashMap<String, RoomInfoContainer> sensors = new HashMap<>();
    private float energyConsumption = 0;

    private static SupervisorStrategy strategy =
            new OneForOneStrategy(
                10,
                Duration.ofMinutes(1),
                DeciderBuilder.match(CustomException.class, e -> SupervisorStrategy.resume())
                        .build());
    @Override
    public SupervisorStrategy supervisorStrategy(){
        return strategy;
    }

    public Receive createReceive() {
        return receiveBuilder()
                .match(TemperatureMessage.class, this::tempOperation)
                .match(SensorReplyMessage.class, this::checkSensor)

                .match(AddDeviceMessage.class, this::addSensor)
                .match(RemoveDeviceMessage.class, this::removeSensor)
                .match(RequestDeviceMessage.class, this::getRooms)
                .match(RequestEnergyConsumptionMessage.class, this::retrieveConsumption)

                .match(CrashMessage.class, this::doCrash)
                .match(CrashServerMessage.class, this::doSelfCrash)
                .build();
    }

    void tempOperation(TemperatureMessage msg){

        if(sensors.containsKey(msg.getRoom())) {
            if (sensors.get(msg.getRoom()).getDesiredTemp() != msg.getTemp()) {
                sensors.get(msg.getRoom()).setDesiredTemp(msg.getTemp());
                if (sensors.get(msg.getRoom()).getTemp() > msg.getTemp()) {
                    sensors.get(msg.getRoom()).getRoomref().tell(new SensorOperationMessage(-1), self());
                } else {
                    sensors.get(msg.getRoom()).getRoomref().tell(new SensorOperationMessage(1), self());
                }
            } else {
                sender().tell(new TextMessage("Room already has Temperature equal to DesiredTemp"), self());
            }
        } else {
            sender().tell(new TextMessage("Room inserted doesnt exists!"), self());
        }
    }

    void checkSensor(SensorReplyMessage msg){
        if(msg.isActive()){
            this.energyConsumption += dE;
            sensors.get(msg.getRoom()).setTemp(msg.getTemp());
            System.out.println("room:" + msg.getRoom() + " | current consumption: " + this.energyConsumption + " | temp: "+ msg.getTemp()/10);
        }
        if(sensors.get(msg.getRoom()).getDesiredTemp() == msg.getTemp()){
            sender().tell(new SensorOperationMessage(0), self());
            System.out.println("room " + msg.getRoom() + " reached desired temperature!");
        }else {
            if(sensors.get(msg.getRoom()).getDesiredTemp() > msg.getTemp()){
                sender().tell(new SensorOperationMessage(1), self());
            }else {
                sender().tell(new SensorOperationMessage(-1), self());
            }
        }
    }

    void addSensor (AddDeviceMessage msg){
        if(!sensors.containsKey(msg.getDeviceid())){
            Random rand = new Random();
            float initialTemp = rand.nextInt(250-100) + 100;
            ActorRef sens = getContext().actorOf(SensorActor.props(), msg.getDeviceid());
            sensors.put(msg.getDeviceid(), new RoomInfoContainer(sens, initialTemp, -999));
            System.out.println("Created sensor: " + msg.getDeviceid() + " with temperature: " + initialTemp/10);
            sens.tell(new SetupMessage(msg.getDeviceid(), initialTemp), self());
        } else {
            sender().tell(new TextMessage("Room name " + msg.getDeviceid() + " already exists"), self());
        }
    }

    void removeSensor (RemoveDeviceMessage msg){
        if(sensors.containsKey(msg.getDeviceid())){
            getContext().stop(sensors.get(msg.getDeviceid()).getRoomref());
            sensors.remove(msg.getDeviceid());
            System.out.println("Room " + msg.getDeviceid() + "removed");
            sender().tell(new TextMessage("Room " + msg.getDeviceid() + "removed"), self());
        }else {
            sender().tell(new TextMessage("Room inserted to be removed doesnt exists!"), self());
        }
    }

    void retrieveConsumption (RequestEnergyConsumptionMessage msg){
        sender().tell(new EnergyConsumptionMessage(this.energyConsumption), self());
    }


    void getRooms(RequestDeviceMessage msg){
        sensors.entrySet().forEach(entry ->
                sender().tell(new TextMessage("Room '" + entry.getKey().toString() + "' - Current Temp: " + (entry.getValue().getTemp()/10)), self())
        );
    }

    void doCrash(CrashMessage msg){
        if(sensors.containsKey(msg.getDeviceid())) {
            sensors.get(msg.getDeviceid()).getRoomref().tell(msg, self());
        }else{
            sender().tell(new TextMessage("Room inserted to be removed doesnt exists!"), self());
        }
    }

    void doSelfCrash(CrashServerMessage msg) throws CustomException{
        throw new CustomException();
    }

    static Props props () {
        return Props.create(HVACActor.class);
    }



}
