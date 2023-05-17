package NSDSprojects.KitchenMachine;

import NSDSprojects.CustomException;
import NSDSprojects.Messages.GenericMessages.CrashMessage;
import NSDSprojects.Messages.GenericMessages.TickMessage;
import NSDSprojects.Messages.KitchenMachine.MachineReplyMessage;
import NSDSprojects.Messages.KitchenMachine.TurnMachineMessage;
import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Cancellable;
import akka.actor.Props;
import akka.cluster.Cluster;
import akka.cluster.ClusterEvent;

import java.time.Duration;

public class MachineActor extends AbstractActor {

    private String state = "off";

    //Cluster cluster = Cluster.get(getContext().getSystem());

    private Cancellable kitchenMachineRoutine;

    /*public void preStart(){
        cluster.subscribe(getSelf(), ClusterEvent.initialStateAsEvents(), ClusterEvent.MemberEvent.class, ClusterEvent.UnreachableMember.class);
    }

    public void postStop(){
        cluster.unsubscribe(getSelf());
    }*/

    @Override
    public Receive createReceive() {
        return off();
    }

    private final Receive off() {
        return receiveBuilder()
                .match(TurnMachineMessage.class, this::TurnOn)
                .match(CrashMessage.class, this::onCrash)
                .build();
    }

    private final Receive on(){
        return receiveBuilder()
                .match(TurnMachineMessage.class, this::TurnOff)
                .match(TickMessage.class, this::tickOperation)
                .match(CrashMessage.class, this::onCrash)
                .build();
    }

    void TurnOn(TurnMachineMessage msg){
        getContext().become(on());
        state = "on";
        kitchenMachineRoutine = getContext().system().scheduler().scheduleWithFixedDelay(
                Duration.ofSeconds(0),
                Duration.ofSeconds(3),
                self(),
                new TickMessage(sender()),
                getContext().system().dispatcher(),
                ActorRef.noSender()
        );
    }

    void TurnOff(TurnMachineMessage msg){
        state = "off";
        kitchenMachineRoutine.cancel();
        sender().tell(new MachineReplyMessage(self().path().name(), this.state), self());
        getContext().become(off());
    }

    void tickOperation(TickMessage msg){
        msg.getReplyTo().tell(new MachineReplyMessage(self().path().name(), this.state), self());
    }

    void onCrash(CrashMessage msg) throws CustomException{
        throw new CustomException();
    }

    static Props props () {
        return Props.create(MachineActor.class);
    }
}
