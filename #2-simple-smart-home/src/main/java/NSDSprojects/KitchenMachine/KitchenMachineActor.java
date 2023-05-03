package NSDSprojects.KitchenMachine;

import NSDSprojects.CustomException;
import NSDSprojects.Messages.GenericMessages.*;
import NSDSprojects.Messages.KitchenMachine.TurnMachineMessage;
import akka.actor.*;
import akka.cluster.Cluster;
import akka.cluster.ClusterEvent;
import akka.japi.pf.DeciderBuilder;

import java.time.Duration;
import java.util.HashMap;

public class KitchenMachineActor extends AbstractActor {

    Cluster cluster = Cluster.get(getContext().getSystem());

    private HashMap<String, ActorRef> machines = new HashMap<>();

    private int energyConsumption = 0;
    private int dE = 5;



    public void preStart(){
        cluster.subscribe(getSelf(), ClusterEvent.initialStateAsEvents(), ClusterEvent.MemberEvent.class, ClusterEvent.UnreachableMember.class);
    }

    public void postStop(){
        cluster.unsubscribe(getSelf());
    }


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
                .match(TurnMachineMessage.class, this::turnMachine)
                .match(TickMessage.class, this::increaseEnergyConsumption)
                .match(AddDeviceMessage.class, this::addMachine)
                .match(RemoveDeviceMessage.class, this::removeMachine)
                .match(RequestDeviceMessage.class, this::getDevices)
                .match(RequestEnergyConsumptionMessage.class, this::retrieveConsumption)

                .match(CrashMessage.class, this::doCrash)
                .build();
    }

    void addMachine (AddDeviceMessage msg){
            machines.put(msg.getDeviceid(), getContext().actorOf(MachineActor.props(), msg.getDeviceid()));
    }

    void removeMachine (RemoveDeviceMessage msg){
        if(machines.containsKey(msg.getDeviceid())){
            getContext().stop(machines.get(msg.getDeviceid()));
            machines.remove(msg.getDeviceid());
        }else {
            sender().tell(new WarningMessage("Machine inserted to be removed doesnt exists!"), self());
        }
    }

    void turnMachine(TurnMachineMessage msg){
        if(machines.containsKey(msg.getKitchenmachine())) {
            machines.get(msg.getKitchenmachine()).tell(msg, self());
        } else {
            sender().tell(new WarningMessage("Machine inserted to be turned doenst exists!"), self());
        }
    }

    void increaseEnergyConsumption (TickMessage msg){
        this.energyConsumption += this.dE;
        System.out.println(this.energyConsumption);
    }

    void getDevices(RequestDeviceMessage msg){
        sender().tell(new ReplyDevicesMessage(machines.keySet()), self());
    }

    void retrieveConsumption (RequestEnergyConsumptionMessage msg){
        sender().tell(new EnergyConsumptionMessage(this.energyConsumption), self());
    }

    void doCrash(CrashMessage msg){
        if(machines.containsKey(msg.getDeviceid())) {
            machines.get(msg.getDeviceid()).tell(msg, self());
        }else{
            sender().tell(new WarningMessage("Room inserted to be removed doesnt exists!"), self());
        }
    }

    static Props props () {
        return Props.create(KitchenMachineActor.class);
    }
}
