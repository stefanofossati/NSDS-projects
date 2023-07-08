package NSDSprojects.InHouseEntertainment;

import NSDSprojects.CustomException;
import NSDSprojects.Messages.GenericMessages.*;
import NSDSprojects.Messages.InHouseEntertainment.TVReplyMessage;
import NSDSprojects.Messages.InHouseEntertainment.TurnTVMessage;
import akka.actor.*;
import akka.japi.pf.DeciderBuilder;

import java.time.Duration;
import java.util.HashMap;

public class InHouseEntertainmentActor extends AbstractActor {
    private int dE = 1;
    private int energyConsumption = 0;

    private HashMap<String, TVInfoContainer> tvs = new HashMap<>();

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
                .match(CrashServerMessage.class, this::doSelfCrash)
                .build();
    }

    void addTv (AddDeviceMessage msg){
        if(!tvs.containsKey(msg.getDeviceid())){
            tvs.put(msg.getDeviceid(), new TVInfoContainer(getContext().actorOf(TVActor.props(), msg.getDeviceid()), "off"));
            System.out.println(msg.getDeviceid() + " added");
        }else{
            sender().tell(new TextMessage("TV " + msg.getDeviceid() + " already exists"), self());
        }

    }

    void removeTv (RemoveDeviceMessage msg){
        if(tvs.containsKey(msg.getDeviceid())){
            getContext().stop(tvs.get(msg.getDeviceid()).getTvref());
            tvs.remove(msg.getDeviceid());
            System.out.println("TV " + msg.getDeviceid() + "removed");
            sender().tell(new TextMessage("TV " + msg.getDeviceid() + " removed"), self());
        }else {
            sender().tell(new TextMessage("TV inserted to be removed doesnt exists!"), self());
        }
    }

    void getDevices(RequestDeviceMessage msg){
        tvs.entrySet().forEach(entry ->
                sender().tell(new TextMessage("Television: '" + entry.getKey().toString() + "' - Current State: " + (entry.getValue().getState())), self())
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
            System.out.println(msg.getTv() + " | " +this.energyConsumption);
        }
    }

    void retrieveConsumption (RequestEnergyConsumptionMessage msg){
        sender().tell(new EnergyConsumptionMessage(this.energyConsumption), self());
    }

    void doCrash(CrashMessage msg){
        if(tvs.containsKey(msg.getDeviceid())) {
            tvs.get(msg.getDeviceid()).getTvref().tell(msg, self());
        }
    }

    void doSelfCrash(CrashServerMessage msg) throws CustomException{
        throw new CustomException();
    }

    static Props props () {
        return Props.create(InHouseEntertainmentActor.class);
    }
}
