package NSDSprojects;

import NSDSprojects.Messages.GenericMessages.*;
import NSDSprojects.Messages.HVAC.AddSensorMessage;
import NSDSprojects.Messages.HVAC.RemoveSensorMessage;
import NSDSprojects.Messages.HVAC.TemperatureMessage;
import NSDSprojects.Messages.InHouseEntertainment.AddTVMessage;
import NSDSprojects.Messages.InHouseEntertainment.RemoveTVMessage;
import NSDSprojects.Messages.InHouseEntertainment.TurnTVMessage;
import NSDSprojects.Messages.KitchenMachine.AddMachineMessage;
import NSDSprojects.Messages.KitchenMachine.RemoveMachineMessage;
import NSDSprojects.Messages.KitchenMachine.TurnKitchenMachineMessage;
import akka.actor.AbstractActor;
import akka.actor.ActorSelection;
import akka.actor.Props;

public class ClientActor extends AbstractActor {

    //private ActorSelection hvac, kitchenMachine, inHouseEntertainment;
    String hvacAddr = "akka.tcp://HVACServer@127.0.0.1:9000/user/HVACActor";
    ActorSelection hvac = getContext().actorSelection(hvacAddr);

    String inHouseEntertainmentAddr = "akka.tcp://InHouseEntertainmentServer@INDIRIZZOIP:PORTA/user/InHouseEntertainmentActor";
    ActorSelection inHouseEntertainment = getContext().actorSelection(inHouseEntertainmentAddr);

    String kitchenMachineAddr = "akka.tcp://KitchenMachineServer@INDIRIZZOIP:PORTA/user/KitchenMachineActor";
    ActorSelection kitchenMachine = getContext().actorSelection(kitchenMachineAddr);

    Cluster cluster = Cluster.get(getContext().getSystem());

    public void preStart(){
        cluster.subscribe(getSelf(), ClusterEvent.initialStateAsEvents(), MemberEvent.class, UnreachableMember.class);
    }

    public void postStop(){
        cluster.unsubscribe(getSelf());
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()

                .match(RequestAllDeviceMessage.class, this::requestAllDevices) //1-3
                .match(ReplyDevicesMessage.class, this::DeviceAvailable) // reply to 1-3

                .match(TemperatureMessage.class, this::sendDesiredTemp) //4
                .match(TurnTVMessage.class, this::sendTurnTv) //5
                .match(TurnKitchenMachineMessage.class, this::sendTurnMachine) //6

                .match(AddSensorMessage.class, this::sendNewSensor) //7
                .match(AddTVMessage.class, this::sendNewTv) //8
                .match(AddMachineMessage.class, this::sendNewMachine) //9

                .match(RemoveSensorMessage.class, this::sendRemoveSensor) //10
                .match(RemoveTVMessage.class, this::sendRemoveTv) //11
                .match(RemoveMachineMessage.class, this::sendRemoveMachine) //12

                .match(RequestEnergyConsumptionMessage.class, this::getConsumption) //10
                .match(EnergyConsumptionMessage.class,  this::showConsumption) //10

                .match(WarningMessage.class, this::showWarning)
                .build();
    }

    void DeviceAvailable(ReplyDevicesMessage msg){
        System.out.println("Devices: ");
        for (String device : msg.getDevices()) {
            System.out.println(" - " + device);
        }
    }


    void requestAllDevices (RequestAllDeviceMessage msg){
        switch (msg.getController()){
            case "HVAC":
                hvac.tell(msg, self());
                break;
            case "InHouseEnt":
                inHouseEntertainment.tell(msg, self());
                break;
            case "KitchenMachines":
                kitchenMachine.tell(msg, self());
                break;
            default:
                System.out.println("invalid value");
        }
    }

    void sendDesiredTemp (TemperatureMessage msg){
        hvac.tell(msg, self());
    }

    void sendTurnTv (TurnTVMessage msg){
        inHouseEntertainment.tell(msg, self());
    }

    void sendTurnMachine (TurnKitchenMachineMessage msg){
        kitchenMachine.tell(msg, self());
    }

    void sendNewSensor (AddSensorMessage msg){
        hvac.tell(msg, sender());
    }

    void sendNewTv (AddTVMessage msg){
        inHouseEntertainment.tell(msg, self());
    }

    void sendNewMachine (AddMachineMessage msg){
        kitchenMachine.tell(msg, self());
    }

    void sendRemoveSensor (RemoveSensorMessage msg) { hvac.tell(msg, self());}

    void sendRemoveTv (RemoveTVMessage msg) { inHouseEntertainment.tell(msg, self());}

    void sendRemoveMachine (RemoveMachineMessage msg) { kitchenMachine.tell(msg, self());}

    void getConsumption (RequestEnergyConsumptionMessage msg){
        hvac.tell(msg, self());
        inHouseEntertainment.tell(msg, self());
        kitchenMachine.tell(msg, self());
    }

    void showConsumption (EnergyConsumptionMessage msg){
        System.out.println(msg.getFrom() + "'s consumption: " + msg.getEnergyConsumption());
    }

    void showWarning (WarningMessage msg){
        System.out.println(msg.getText());
    }

    void setup() {
        String hvacAddr = "akka.tcp://HVACServer@INDIRIZZOIP:PORTA/user/HVACActor";
        hvac = getContext().actorSelection(hvacAddr);

        String inHouseEntertainmentAddr = "akka.tcp://HVACServer@INDIRIZZOIP:PORTA/user/InHouseEntertainmentActor";
        inHouseEntertainment = getContext().actorSelection(inHouseEntertainmentAddr);

        String kitchenMachineAddr = "akka.tcp://HVACServer@INDIRIZZOIP:PORTA/user/KitchenMachineActor";
        kitchenMachine = getContext().actorSelection(kitchenMachineAddr);
    }

    static Props props () {
        return Props.create(ClientActor.class);
    }

}
