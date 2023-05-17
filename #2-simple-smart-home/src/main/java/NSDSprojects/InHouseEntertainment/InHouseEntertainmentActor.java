package NSDSprojects.InHouseEntertainment;

import NSDSprojects.CustomException;
import NSDSprojects.Messages.GenericMessages.*;
import NSDSprojects.Messages.InHouseEntertainment.TVReplyMessage;
import NSDSprojects.Messages.InHouseEntertainment.TurnTVMessage;
import akka.actor.*;
import akka.cluster.Cluster;
import akka.cluster.ClusterEvent;
import akka.japi.pf.DeciderBuilder;

import java.time.Duration;
import java.util.HashMap;

public class InHouseEntertainmentActor extends AbstractActor {

    //Cluster cluster = Cluster.get(getContext().getSystem());
    private int dE = 1;
    private int energyConsumption = 0;

    private HashMap<String, TVInfoContainer> tvs = new HashMap<>();


/*
    public void preStart(){
        cluster.subscribe(getSelf(), ClusterEvent.initialStateAsEvents(), ClusterEvent.MemberEvent.class, ClusterEvent.UnreachableMember.class);
    }

    public void postStop(){
        cluster.unsubscribe(getSelf());
    }
*/

    private static SupervisorStrategy strategy =
            new OneForOneStrategy(
                    10,
                    Duration.ofMinutes(1),
                    DeciderBuilder.match(CustomException.class, e -> SupervisorStrategy.resume())
                            .build());

    @Override
    public SupervisorStrategy supervisorStrategy(){
        return strategy;
    }

    public Receive createReceive() {
        return receiveBuilder()
                .match(TurnTVMessage.class, this::turnTV)
                .match(TVReplyMessage.class, this::increaseEnergyConsumption)

                .match(AddDeviceMessage.class, this::addTv)
                .match(RemoveDeviceMessage.class, this::removeTv)
                .match(RequestDeviceMessage.class, this::getDevices)
                .match(RequestEnergyConsumptionMessage.class, this::retrieveConsumption)

                .match(CrashMessage.class, this::doCrash)
                .build();
    }

    void addTv (AddDeviceMessage msg){
        tvs.put(msg.getDeviceid(), new TVInfoContainer(getContext().actorOf(TVActor.props(), msg.getDeviceid()), "off"));
    }

    void removeTv (RemoveDeviceMessage msg){
        if(tvs.containsKey(msg.getDeviceid())){
            getContext().stop(tvs.get(msg.getDeviceid()).getTvref());
            tvs.remove(msg.getDeviceid());
        }else {
            sender().tell(new TextMessage("TV inserted to be removed doesnt exists!"), self());
        }
    }

    void getDevices(RequestDeviceMessage msg){
        tvs.entrySet().forEach(entry ->
                sender().tell(new TextMessage("Room '" + entry.getKey().toString() + "' - Current State: " + (entry.getValue().getState())), self())
        );
    }

    void turnTV(TurnTVMessage msg){
        if(tvs.containsKey(msg.getTv())) {
            tvs.get(msg.getTv()).getTvref().tell(msg, self());
        }else{
            sender().tell(new TextMessage("TV inserted to be turned doesnt exists!"), self());
        }
    }

    void increaseEnergyConsumption (TVReplyMessage msg){
        if(tvs.get(msg.getTv()).getState() != msg.getState()){
            tvs.get(msg.getTv()).changeState();
        }
        if(msg.getState().equals("on")) {
            this.energyConsumption += this.dE;
            System.out.println(this.energyConsumption);
        }
    }

    void retrieveConsumption (RequestEnergyConsumptionMessage msg){
        sender().tell(new EnergyConsumptionMessage(this.energyConsumption), self());
    }

    void doCrash(CrashMessage msg){
        if(tvs.containsKey(msg.getDeviceid())) {
            tvs.get(msg.getDeviceid()).getTvref().tell(msg, self());
        }else{
            sender().tell(new TextMessage("Room inserted to be removed doesnt exists!"), self());
        }
    }

    static Props props () {
        return Props.create(InHouseEntertainmentActor.class);
    }
}
