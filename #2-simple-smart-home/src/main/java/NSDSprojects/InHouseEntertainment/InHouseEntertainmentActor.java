package NSDSprojects.InHouseEntertainment;

import NSDSprojects.Messages.GenericMessages.*;
import NSDSprojects.Messages.HVAC.RemoveSensorMessage;
import NSDSprojects.Messages.InHouseEntertainment.AddTVMessage;
import NSDSprojects.Messages.InHouseEntertainment.RemoveTVMessage;
import NSDSprojects.Messages.InHouseEntertainment.TurnTVMessage;
import akka.actor.*;
import akka.japi.pf.DeciderBuilder;

import java.time.Duration;
import java.util.HashMap;

public class InHouseEntertainmentActor extends AbstractActor {

    Cluster cluster = Cluster.get(getContext().getSystem());
    private int dE = 1;
    private int energyConsumption = 0;

    private HashMap<String, ActorRef> tvs = new HashMap<>();


    public void preStart(){
        cluster.subscribe(getSelf(), ClusterEvent.initialStateAsEvents(), MemberEvent.class, UnreachableMember.class);
    }

    public void postStop(){
        cluster.unsubscribe(getSelf());
    }


    private static SupervisorStrategy strategy =
            new OneForOneStrategy(
                    10,
                    Duration.ofMinutes(1),
                    DeciderBuilder.match(Exception.class, e -> SupervisorStrategy.resume())
                            .build());

    public Receive createReceive() {
        return receiveBuilder()
                .match(TurnTVMessage.class, this::turnTV)
                .match(TickMessage.class, this::increaseEnergyConsumption)

                .match(AddTVMessage.class, this::addTv)
                .match(RemoveTVMessage.class, this::removeTv)
                .match(RequestAllDeviceMessage.class, this::getDevices)
                .match(RequestEnergyConsumptionMessage.class, this::retrieveConsumption)
                .build();
    }

    void addTv (AddTVMessage msg){
        tvs.put(msg.getDeviceid(), getContext().actorOf(TVActor.props(), msg.getDeviceid()));
    }

    void removeTv (RemoveTVMessage msg){
        if(tvs.containsKey(msg.getDeviceid())){
            getContext().stop(tvs.get(msg.getDeviceid()));
            tvs.remove(msg.getDeviceid());
        }else {
            sender().tell(new WarningMessage("TV inserted to be removed doesnt exists!"), self());
        }
    }

    void getDevices(RequestAllDeviceMessage msg){
        sender().tell(new ReplyDevicesMessage(tvs.keySet()), self());
    }

    void turnTV(TurnTVMessage msg){
        if(tvs.containsKey(msg.getTv())) {
            tvs.get(msg.getTv()).tell(msg, self());
        }else{
            sender().tell(new WarningMessage("TV inserted to be turned doesnt exists!"), self());
        }
    }

    void increaseEnergyConsumption (TickMessage msg){
        this.energyConsumption += this.dE;
    }

    void retrieveConsumption (RequestEnergyConsumptionMessage msg){
        sender().tell(new EnergyConsumptionMessage(this.energyConsumption, "InHouseEntertainment"), self());
    }

    static Props props () {
        return Props.create(InHouseEntertainmentActor.class);
    }
}
