package NSDSprojects.InHouseEntertainment;

import NSDSprojects.CustomException;
import NSDSprojects.Messages.GenericMessages.CrashMessage;
import NSDSprojects.Messages.GenericMessages.TickMessage;
import NSDSprojects.Messages.InHouseEntertainment.TVReplyMessage;
import NSDSprojects.Messages.InHouseEntertainment.TurnTVMessage;
import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Cancellable;
import akka.actor.Props;

import java.time.Duration;

public class TVActor extends AbstractActor {
   // Cluster cluster = Cluster.get(getContext().getSystem());
    private Cancellable tvOnRoutine;
    private String state = "off";



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
        state = "on";
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
        state = "off";
        tvOnRoutine.cancel();
        sender().tell(new TVReplyMessage(self().path().name(), this.state), self());
        getContext().become(off());
    }

    void tickOperation(TickMessage msg){
        msg.getReplyTo().tell(new TVReplyMessage(self().path().name(), this.state), self());
    }

    void onCrash(CrashMessage msg) throws CustomException{
        throw new CustomException();
    }

    static Props props () {
        return Props.create(TVActor.class);
    }

}