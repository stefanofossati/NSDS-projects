package NSDSprojects.KitchenMachine;

import NSDSprojects.InHouseEntertainment.TVActor;
import NSDSprojects.Messages.GenericMessages.*;
import NSDSprojects.Messages.HVAC.AddSensorMessage;
import NSDSprojects.Messages.InHouseEntertainment.RemoveTVMessage;
import NSDSprojects.Messages.KitchenMachine.AddMachineMessage;
import NSDSprojects.Messages.KitchenMachine.RemoveMachineMessage;
import NSDSprojects.Messages.KitchenMachine.TurnKitchenMachineMessage;
import akka.actor.*;
import akka.japi.pf.DeciderBuilder;

import java.time.Duration;
import java.util.HashMap;

public class KitchenMachineActor extends AbstractActor {

    Cluster cluster = Cluster.get(getContext().getSystem());

    private HashMap<String, ActorRef> machines = new HashMap<>();

    private int energyConsumption = 0;
    private int dE = 5;



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
                .match(TurnKitchenMachineMessage.class, this::turnMachine)
                .match(TickMessage.class, this::increaseEnergyConsumption)

                .match(AddMachineMessage.class, this::addMachine)
                .match(RemoveMachineMessage.class, this::removeMachine)
                .match(RequestAllDeviceMessage.class, this::getDevices)
                .match(RequestEnergyConsumptionMessage.class, this::retrieveConsumption)
                .build();
    }

    void addMachine (AddMachineMessage msg){
            machines.put(msg.getDeviceid(), getContext().actorOf(MachineActor.props(), msg.getDeviceid()));
    }

    void removeMachine (RemoveMachineMessage msg){
        if(machines.containsKey(msg.getDeviceid())){
            getContext().stop(machines.get(msg.getDeviceid()));
            machines.remove(msg.getDeviceid());
        }else {
            sender().tell(new WarningMessage("Machine inserted to be removed doesnt exists!"), self());
        }
    }

    void turnMachine(TurnKitchenMachineMessage msg){
        if(machines.containsKey(msg.getKitchenmachine())) {
            machines.get(msg.getKitchenmachine()).tell(msg, self());
        } else {
            sender().tell(new WarningMessage("Machine inserted to be turned doenst exists!"), self());
        }
    }

    void increaseEnergyConsumption (TickMessage msg){
        this.energyConsumption += this.dE;
    }

    void getDevices(RequestAllDeviceMessage msg){
        sender().tell(new ReplyDevicesMessage(machines.keySet()), self());
    }

    void retrieveConsumption (RequestEnergyConsumptionMessage msg){
        sender().tell(new EnergyConsumptionMessage(this.energyConsumption, "KitchenMachine"), self());
    }

    static Props props () {
        return Props.create(KitchenMachineActor.class);
    }
}
