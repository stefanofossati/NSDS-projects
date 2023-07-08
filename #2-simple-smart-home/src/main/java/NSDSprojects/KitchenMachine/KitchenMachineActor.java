package NSDSprojects.KitchenMachine;

import NSDSprojects.CustomException;
import NSDSprojects.Messages.GenericMessages.*;
import NSDSprojects.Messages.KitchenMachine.MachineReplyMessage;
import NSDSprojects.Messages.KitchenMachine.TurnMachineMessage;
import akka.actor.*;
import akka.japi.pf.DeciderBuilder;

import java.time.Duration;
import java.util.HashMap;

public class KitchenMachineActor extends AbstractActor {
    private HashMap<String, MachineInfoContainer> machines = new HashMap<>();

    private int energyConsumption = 0;
    private int dE = 5;


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
                .match(MachineReplyMessage.class, this::increaseEnergyConsumption)
                .match(AddDeviceMessage.class, this::addMachine)
                .match(RemoveDeviceMessage.class, this::removeMachine)
                .match(RequestDeviceMessage.class, this::getDevices)
                .match(RequestEnergyConsumptionMessage.class, this::retrieveConsumption)

                .match(CrashMessage.class, this::doCrash)
                .match(CrashServerMessage.class, this::doSelfCrash)
                .build();
    }

    void addMachine (AddDeviceMessage msg){
        if(!machines.containsKey(msg.getDeviceid())){
            machines.put(msg.getDeviceid(), new MachineInfoContainer(getContext().actorOf(MachineActor.props(), msg.getDeviceid()), "off"));
            System.out.println(msg.getDeviceid() + " added");
        }else{
            sender().tell(new TextMessage("KitchenMachine " + msg.getDeviceid() + " already exists"), self());
        }
    }

    void removeMachine (RemoveDeviceMessage msg){
        if(machines.containsKey(msg.getDeviceid())){
            getContext().stop(machines.get(msg.getDeviceid()).getMachineref());
            machines.remove(msg.getDeviceid());
            System.out.println("KitchenMachine " + msg.getDeviceid() + "removed");
            sender().tell(new TextMessage("KitchenMachine " + msg.getDeviceid() + " removed"), self());
        }else {
            sender().tell(new TextMessage("Machine inserted to be removed doesnt exists!"), self());
        }
    }

    void turnMachine(TurnMachineMessage msg){
        if(machines.containsKey(msg.getKitchenmachine())) {
            machines.get(msg.getKitchenmachine()).getMachineref().tell(msg, self());
        } else {
            sender().tell(new TextMessage("Machine inserted to be turned doenst exists!"), self());
        }
    }

    void increaseEnergyConsumption (MachineReplyMessage msg){
        if(machines.get(msg.getMachine()).getState() != msg.getState()){
            machines.get(msg.getMachine()).changeState();
        }
        if(msg.getState().equals("on")) {
            this.energyConsumption += this.dE;
            System.out.println(msg.getMachine() + " | " + this.energyConsumption);
        }
    }

    void getDevices(RequestDeviceMessage msg){
        machines.entrySet().forEach(entry ->
                sender().tell(new TextMessage("Room '" + entry.getKey().toString() + "' - Current State: " + (entry.getValue().getState())), self())
        );
    }

    void retrieveConsumption (RequestEnergyConsumptionMessage msg){
        sender().tell(new EnergyConsumptionMessage(this.energyConsumption), self());
    }

    void doCrash(CrashMessage msg){
        if(machines.containsKey(msg.getDeviceid())) {
            machines.get(msg.getDeviceid()).getMachineref().tell(msg, self());
        }else{
            sender().tell(new TextMessage("Room inserted to be removed doesnt exists!"), self());
        }
    }

    void doSelfCrash(CrashServerMessage msg) throws CustomException{
        throw new CustomException();
    }

    static Props props () {
        return Props.create(KitchenMachineActor.class);
    }
}
