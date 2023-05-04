package NSDSprojects;

import NSDSprojects.Messages.GenericMessages.*;
import NSDSprojects.Messages.InHouseEntertainment.TurnTVMessage;
import akka.actor.AbstractActor;
import akka.actor.ActorSelection;
import akka.actor.Props;
import akka.cluster.Cluster;
import akka.cluster.ClusterEvent;

public class ClientActorIHE extends AbstractActor {
    String inHouseEntertainmentAddr = "akka://InHouseEntertainmentServer@192.168.56.1:9004/user/InHouseEntertainmentActor";
    ActorSelection inHouseEntertainment = getContext().actorSelection(inHouseEntertainmentAddr);

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

                .match(AddDeviceMessage.class, this::onAddNewDevice) //7
                .match(RemoveDeviceMessage.class, this::onRemoveDevice) //10
                .match(CrashMessage.class, this::onCrash)

                .match(RequestEnergyConsumptionMessage.class, this::getConsumption) //10
                .match(EnergyConsumptionMessage.class,  this::showConsumption) //10

                .match(TextMessage.class, this::showWarning)

                .match(TurnTVMessage.class, this::sendTurnTv)
                .build();
    }

    void requestDevices(RequestDeviceMessage msg){
        inHouseEntertainment.tell(msg, self());
    }

    void onAddNewDevice(AddDeviceMessage msg){
        inHouseEntertainment.tell(msg, self());
    }

    void onRemoveDevice(RemoveDeviceMessage msg){
        inHouseEntertainment.tell(msg, self());
    }

    void onCrash(CrashMessage msg){
        inHouseEntertainment.tell(msg, self());
    }

    void getConsumption (RequestEnergyConsumptionMessage msg){
        inHouseEntertainment.tell(msg, self());
    }

    void showConsumption (EnergyConsumptionMessage msg){
        System.out.println("InHouseEntertainment's consumption: " + msg.getEnergyConsumption());
    }

    void showWarning (TextMessage msg){
        System.out.println(msg.getText());
    }

    void sendTurnTv (TurnTVMessage msg){
        inHouseEntertainment.tell(msg, self());
    }

    static Props props () {
        return Props.create(ClientActorIHE.class);
    }
}
