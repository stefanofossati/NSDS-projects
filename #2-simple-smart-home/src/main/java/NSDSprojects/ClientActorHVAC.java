package NSDSprojects;

import NSDSprojects.Messages.GenericMessages.*;
import NSDSprojects.Messages.HVAC.TemperatureMessage;
import akka.actor.AbstractActor;
import akka.actor.ActorSelection;
import akka.actor.Props;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class ClientActorHVAC extends AbstractActor {
    String hvacAddr;
    ActorSelection hvac;

    @Override
    public Receive createReceive() {
        return receiveBuilder()

                .match(RequestDeviceMessage.class, this::requestDevices)

                .match(AddDeviceMessage.class, this::onAddNewDevice)
                .match(RemoveDeviceMessage.class, this::onRemoveDevice)
                .match(CrashMessage.class, this::onCrash)
                .match(CrashServerMessage.class, this::onServerCrash)

                .match(RequestEnergyConsumptionMessage.class, this::getConsumption)
                .match(EnergyConsumptionMessage.class,  this::showConsumption)

                .match(TextMessage.class, this::showWarning)

                .match(TemperatureMessage.class, this::sendDesiredTemp)

                .match(SetupConnectionMessage.class, this::setup)
                .build();
    }

    void requestDevices(RequestDeviceMessage msg){
        hvac.tell(msg, self());
    }

    void onAddNewDevice(AddDeviceMessage msg){
        hvac.tell(msg, self());
    }

    void onRemoveDevice(RemoveDeviceMessage msg){
        hvac.tell(msg, self());
    }

    void onCrash(CrashMessage msg){
        hvac.tell(msg, self());
    }
    void onServerCrash(CrashServerMessage msg){
        hvac.tell(msg, self());
    }

    void getConsumption (RequestEnergyConsumptionMessage msg){
        hvac.tell(msg, self());
    }

    void showConsumption (EnergyConsumptionMessage msg){
        System.out.println("HVAC's consumption: " + msg.getEnergyConsumption());
    }

    void showWarning (TextMessage msg){
        System.out.println(msg.getText());
    }

    void sendDesiredTemp (TemperatureMessage msg){
        hvac.tell(msg, self());
    }

    static Props props () {
        return Props.create(ClientActorHVAC.class);
    }

    void setup(SetupConnectionMessage msg){
        try {
            File myObj = new File("src/main/resources/connectTo/hvac.txt");
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                this.hvacAddr = "akka://HVACServer@" + data + "/user/HVACSupervisor/HVACActor";
                System.out.println(hvacAddr);
                this.hvac = getContext().actorSelection(this.hvacAddr);
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("File not found");
            e.printStackTrace();
        }


    }
}
