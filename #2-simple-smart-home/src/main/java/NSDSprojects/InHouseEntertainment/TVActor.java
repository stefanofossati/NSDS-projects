package NSDSprojects.InHouseEntertainment;

import NSDSprojects.CustomException;
import NSDSprojects.Messages.GenericMessages.CrashMessage;
import NSDSprojects.Messages.GenericMessages.TickMessage;
import NSDSprojects.Messages.InHouseEntertainment.TurnTVMessage;
import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Cancellable;
import akka.actor.Props;
import akka.cluster.Cluster;
import akka.cluster.ClusterEvent;


import java.time.Duration;

public class TVActor extends AbstractActor {
    Cluster cluster = Cluster.get(getContext().getSystem());
    private Cancellable tvOnRoutine;


    public void preStart(){
        cluster.subscribe(getSelf(), ClusterEvent.initialStateAsEvents(), ClusterEvent.MemberEvent.class, ClusterEvent.UnreachableMember.class);
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
                .match(TurnTVMessage.class, this::TurnOn)
                .match(CrashMessage.class, this::onCrash)
                .build();
    }

    private final Receive on(){
        return receiveBuilder()
                .match(TurnTVMessage.class, this::TurnOff)
                .match(TickMessage.class, this::tickOperation)
                .match(CrashMessage.class, this::onCrash)
                .build();
    }

    void TurnOn(TurnTVMessage msg){
        getContext().become(on());
        tvOnRoutine = getContext().system().scheduler().scheduleWithFixedDelay(
                Duration.ofSeconds(0),
                Duration.ofSeconds(3),
                self(),
                new TickMessage(sender()),
                getContext().system().dispatcher(),
                ActorRef.noSender()
        );
    }

    void TurnOff(TurnTVMessage msg){
        tvOnRoutine.cancel();
        getContext().become(off());
    }

    void tickOperation(TickMessage msg){
        msg.getReplyTo().tell(msg, self());
    }

    void onCrash(CrashMessage msg) throws CustomException{
        throw new CustomException();
    }

    static Props props () {
        return Props.create(TVActor.class);
    }

}
