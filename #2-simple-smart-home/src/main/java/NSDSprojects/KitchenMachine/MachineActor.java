package NSDSprojects.KitchenMachine;

import NSDSprojects.Messages.GenericMessages.TickMessage;
import NSDSprojects.Messages.InHouseEntertainment.TurnTVMessage;
import NSDSprojects.Messages.KitchenMachine.TurnKitchenMachineMessage;
import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Cancellable;
import akka.actor.Props;

import java.time.Duration;

public class MachineActor extends AbstractActor {

    Cluster cluster = Cluster.get(getContext().getSystem());

    private Cancellable kitchenMachineRoutine;

    public void preStart(){
        cluster.subscribe(getSelf(), ClusterEvent.initialStateAsEvents(), MemberEvent.class, UnreachableMember.class);
    }

    public void postStop(){
        cluster.unsubscribe(getSelf());
    }

    @Override
    public Receive createReceive() {
        return off();
    }

    private final Receive off() {
        return receiveBuilder()
                .match(TurnKitchenMachineMessage.class, this::TurnOn)
                .build();
    }

    private final Receive on(){
        return receiveBuilder()
                .match(TurnTVMessage.class, this::TurnOff)
                .match(TickMessage.class, this::tickOperation)
                .build();
    }

    void TurnOn(TurnKitchenMachineMessage msg){
        getContext().become(on());
        kitchenMachineRoutine = getContext().system().scheduler().schedule(
                Duration.ofSeconds(0),
                Duration.ofSeconds(10),
                self(),
                new TickMessage(sender()),
                getContext().system().dispatcher(),
                ActorRef.noSender()
        );
    }

    void TurnOff(TurnTVMessage msg){
        kitchenMachineRoutine.cancel();
        getContext().become(off());
    }

    void tickOperation(TickMessage msg){
        msg.getReplyTo().tell(msg, self());
    }

    static Props props () {
        return Props.create(MachineActor.class);
    }
}
