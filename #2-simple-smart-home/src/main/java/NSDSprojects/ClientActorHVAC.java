package NSDSprojects;

import NSDSprojects.Messages.GenericMessages.*;
import NSDSprojects.Messages.HVAC.TemperatureMessage;
import akka.actor.AbstractActor;
import akka.actor.ActorSelection;
import akka.actor.Props;
import akka.cluster.Cluster;
import akka.cluster.ClusterEvent;

public class ClientActorHVAC extends AbstractActor {
    String hvacAddr = "akka://HVACServer@192.168.56.1:9003/user/HVACActor";
    ActorSelection hvac = getContext().actorSelection(hvacAddr);

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

                .match(RequestDeviceMessage.class, this::requestDevices) //1-3
                .match(ReplyDevicesMessage.class, this::showDevices) // reply to 1-3

                .match(AddDeviceMessage.class, this::onAddNewDevice) //7
                .match(RemoveDeviceMessage.class, this::onRemoveDevice) //10
                .match(CrashMessage.class, this::onCrash)

                .match(RequestEnergyConsumptionMessage.class, this::getConsumption) //10
                .match(EnergyConsumptionMessage.class,  this::showConsumption) //10

                .match(WarningMessage.class, this::showWarning)

                .match(TemperatureMessage.class, this::sendDesiredTemp) //4

                .build();
    }

    void requestDevices(RequestDeviceMessage msg){
        hvac.tell(msg, self());
    }

    void showDevices(ReplyDevicesMessage msg){
        System.out.println("HVAC Devices: ");
        for (String device : msg.getDevices()) {
            System.out.println(" - " + device);
        }
    }

    void onAddNewDevice(AddDeviceMessage msg){
        hvac.tell(msg, self());
    }

    void onRemoveDevice(RemoveDeviceMessage msg){
        hvac.tell(msg, self());
    }

    void onCrash(CrashMessage msg){
        hvac.tell(msg, self());
    }

    void getConsumption (RequestEnergyConsumptionMessage msg){
        hvac.tell(msg, self());
    }

    void showConsumption (EnergyConsumptionMessage msg){
        System.out.println("HVAC's consumption: " + msg.getEnergyConsumption());
    }

    void showWarning (WarningMessage msg){
        System.out.println(msg.getText());
    }

    void sendDesiredTemp (TemperatureMessage msg){
        hvac.tell(msg, self());
    }

    static Props props () {
        return Props.create(ClientActorHVAC.class);
    }

}
