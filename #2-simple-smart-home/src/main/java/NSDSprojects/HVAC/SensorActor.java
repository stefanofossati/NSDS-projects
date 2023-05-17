package NSDSprojects.HVAC;

import NSDSprojects.CustomException;
import NSDSprojects.Messages.GenericMessages.CrashMessage;
import NSDSprojects.Messages.HVAC.SensorOperationMessage;
import NSDSprojects.Messages.HVAC.SensorReplyMessage;
import NSDSprojects.Messages.HVAC.SetupMessage;
import NSDSprojects.Messages.GenericMessages.TickMessage;
import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Cancellable;
import akka.actor.Props;
import akka.cluster.Cluster;
import akka.cluster.ClusterEvent;

import java.time.Duration;


public class SensorActor extends AbstractActor {

    //Cluster cluster = Cluster.get(getContext().getSystem());
    private String room;
    private float currentTemp;
    final float dE = 10.0f;

    private int operation = 0;

    private Cancellable sensorTask;

   /* public void preStart(){
        cluster.subscribe(getSelf(), ClusterEvent.initialStateAsEvents(), ClusterEvent.MemberEvent.class, ClusterEvent.UnreachableMember.class);
    }

    public void postStop(){
        cluster.unsubscribe(getSelf());
    }
*/
    @Override
    public Receive createReceive() {
        return inactive();
    }

    private final Receive inactive() {
        return receiveBuilder()
                .match(SetupMessage.class, this::setup)
                .match(SensorOperationMessage.class, this::instantiateTask)
                .match(CrashMessage.class, this::onCrash)
                .build();
    }

    private final Receive active(){
        return receiveBuilder()
                .match(SensorOperationMessage.class, this::checkOperation)
                .match(TickMessage.class, this::modifyTemp)
                .match(CrashMessage.class, this::onCrash)
                .build();
    }

    public float getTemp() {
        return currentTemp;
    }

    void setup(SetupMessage msg){
        this.currentTemp = msg.getInitialTemp();
        this.room = msg.getRoom();
    }

     void instantiateTask(SensorOperationMessage msg) {
         if (msg.getOperation() != 0) {
             getContext().become(active());
             operation = msg.getOperation();
             sensorTask = getContext().system().scheduler().scheduleWithFixedDelay(
                    Duration.ofSeconds(0),
                    Duration.ofSeconds(3),
                    self(),
                    new TickMessage(sender()),
                    getContext().system().dispatcher(),
                    ActorRef.noSender()
                );
         }
     }

     void checkOperation(SensorOperationMessage msg){
        if(msg.getOperation() != 0){
            operation = msg.getOperation();
        } else {
            sensorTask.cancel();
            this.operation = 0;
        }
     }

     void modifyTemp (TickMessage msg){
        if(operation != 0 && operation > 0){
            this.currentTemp += 1f;
            msg.getReplyTo().tell(new SensorReplyMessage(this.room, this.currentTemp, true), self());
        } else if (operation != 0 && operation < 0) {
            this.currentTemp -= 1f;
            msg.getReplyTo().tell(new SensorReplyMessage(this.room, this.currentTemp, true), self());
        }
     }

    void onCrash(CrashMessage msg) throws CustomException {
        throw new CustomException();
    }

    static Props props () {
        return Props.create(SensorActor.class);
    }

}
