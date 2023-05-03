/*package NSDSprojects;

import NSDSprojects.Messages.GenericMessages.*;
import NSDSprojects.Messages.HVAC.AddSensorMessage;
import NSDSprojects.Messages.HVAC.RemoveSensorMessage;
import NSDSprojects.Messages.HVAC.TemperatureMessage;
import NSDSprojects.Messages.InHouseEntertainment.AddTVMessage;
import NSDSprojects.Messages.InHouseEntertainment.RemoveTVMessage;
import NSDSprojects.Messages.InHouseEntertainment.TurnTVMessage;
import NSDSprojects.Messages.KitchenMachine.AddMachineMessage;
import NSDSprojects.Messages.KitchenMachine.RemoveMachineMessage;
import NSDSprojects.Messages.KitchenMachine.TurnMachineMessage;
import akka.actor.AbstractActor;
import akka.actor.ActorSelection;
import akka.actor.Props;
import akka.cluster.Cluster;
import akka.cluster.ClusterEvent;

public class ClientActor extends AbstractActor {

    String hvacAddr = "akka://HVACServer@192.168.56.1:9003/user/HVACActor";
    ActorSelection hvac = getContext().actorSelection(hvacAddr);

    String inHouseEntertainmentAddr = "akka://InHouseEntertainmentServer@192.168.56.1:9004/user/InHouseEntertainmentActor";
    ActorSelection inHouseEntertainment = getContext().actorSelection(inHouseEntertainmentAddr);

    String kitchenMachineAddr = "akka://KitchenMachineServer@192.168.56.1:9005/user/KitchenMachineActor";
    ActorSelection kitchenMachine = getContext().actorSelection(kitchenMachineAddr);

    Cluster cluster = Cluster.get(getContext().getSystem());

    public void preStart(){
        cluster.subscribe(getSelf(), ClusterEvent.initialStateAsEvents(), ClusterEvent.MemberEvent.class, ClusterEvent.UnreachableMember.class);
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
                .match(TurnMachineMessage.class, this::sendTurnMachine) //6

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
        switch (msg.getController()) {
            case "HVAC" -> hvac.tell(msg, self());
            case "InHouseEnt" -> inHouseEntertainment.tell(msg, self());
            case "KitchenMachines" -> kitchenMachine.tell(msg, self());
            default -> System.out.println("invalid value");
        }
    }

    void sendDesiredTemp (TemperatureMessage msg){
        hvac.tell(msg, self());
    }

    void sendTurnTv (TurnTVMessage msg){
        inHouseEntertainment.tell(msg, self());
    }

    void sendTurnMachine (TurnMachineMessage msg){
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
        System.out.println(msg.getFrom() + "'s problem: " + msg.getEnergyConsumption());
    }

    void showWarning (WarningMessage msg){
        System.out.println(msg.getText());
    }

    static Props props () {
        return Props.create(ClientActor.class);
    }

}
*/