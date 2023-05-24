package NSDSprojects;

import NSDSprojects.Messages.GenericMessages.*;
import NSDSprojects.Messages.KitchenMachine.TurnMachineMessage;
import akka.actor.AbstractActor;
import akka.actor.ActorSelection;
import akka.actor.Props;
import akka.cluster.Cluster;
import akka.cluster.ClusterEvent;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class ClientActorKM extends AbstractActor {
    String kitchenMachineAddr;
    ActorSelection kitchenMachine;

    @Override
    public Receive createReceive() {
        return receiveBuilder()

                .match(RequestDeviceMessage.class, this::requestDevices)

                .match(AddDeviceMessage.class, this::onAddNewDevice)
                .match(RemoveDeviceMessage.class, this::onRemoveDevice)
                .match(CrashMessage.class, this::onCrash)

                .match(RequestEnergyConsumptionMessage.class, this::getConsumption)
                .match(EnergyConsumptionMessage.class,  this::showConsumption)

                .match(TextMessage.class, this::showWarning)

                .match(TurnMachineMessage.class, this::sendTurnMachine)

                .match(SetupConnectionMessage.class, this::setup)
                .build();
    }

    void requestDevices(RequestDeviceMessage msg){
        kitchenMachine.tell(msg, self());
    }

    void onAddNewDevice(AddDeviceMessage msg){
        kitchenMachine.tell(msg, self());
    }

    void onRemoveDevice(RemoveDeviceMessage msg){
        kitchenMachine.tell(msg, self());
    }

    void onCrash(CrashMessage msg){
        kitchenMachine.tell(msg, self());
    }

    void getConsumption (RequestEnergyConsumptionMessage msg){
        kitchenMachine.tell(msg, self());
    }

    void showConsumption (EnergyConsumptionMessage msg){
        System.out.println("Kitchen machine's consumption: " + msg.getEnergyConsumption());
    }

    void showWarning (TextMessage msg){
        System.out.println(msg.getText());
    }

    void sendTurnMachine (TurnMachineMessage msg){
        kitchenMachine.tell(msg, self());
    }

    static Props props () {
        return Props.create(ClientActorKM.class);
    }

    void setup(SetupConnectionMessage msg) {
        try {
            File myObj = new File("src/main/java/resources/connectTo/km.txt");
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                this.kitchenMachineAddr = "akka://KitchenMachineServer@" + data + "/user/KitchenMachineActor";
                System.out.println(this.kitchenMachineAddr);
                this.kitchenMachine = getContext().actorSelection(this.kitchenMachineAddr);
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("File not found");
            e.printStackTrace();
        }
    }
}
