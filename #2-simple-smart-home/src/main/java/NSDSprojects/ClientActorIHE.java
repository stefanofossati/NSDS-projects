package NSDSprojects;

import NSDSprojects.Messages.GenericMessages.*;
import NSDSprojects.Messages.InHouseEntertainment.TurnTVMessage;
import akka.actor.AbstractActor;
import akka.actor.ActorSelection;
import akka.actor.Props;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class ClientActorIHE extends AbstractActor {
    String inHouseEntertainmentAddr;
    ActorSelection inHouseEntertainment;


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

                .match(TurnTVMessage.class, this::sendTurnTv)

                .match(SetupConnectionMessage.class, this::setup)
                .build();
    }

    void requestDevices(RequestDeviceMessage msg){
        inHouseEntertainment.tell(msg, self());
    }

    void onAddNewDevice(AddDeviceMessage msg){
        inHouseEntertainment.tell(msg, self());
    }

    void onRemoveDevice(RemoveDeviceMessage msg){
        inHouseEntertainment.tell(msg, self());
    }

    void onCrash(CrashMessage msg){
        inHouseEntertainment.tell(msg, self());
    }

    void onServerCrash(CrashServerMessage msg){
        inHouseEntertainment.tell(msg, self());
    }

    void getConsumption (RequestEnergyConsumptionMessage msg){
        inHouseEntertainment.tell(msg, self());
    }

    void showConsumption (EnergyConsumptionMessage msg){
        System.out.println("InHouseEntertainment's consumption: " + msg.getEnergyConsumption());
    }

    void showWarning (TextMessage msg){
        System.out.println(msg.getText());
    }

    void sendTurnTv (TurnTVMessage msg){
        inHouseEntertainment.tell(msg, self());
    }

    static Props props () {
        return Props.create(ClientActorIHE.class);
    }

    void setup(SetupConnectionMessage msg){
        this.inHouseEntertainmentAddr = "akka://InHouseEntertainmentServer@" + msg.getInfo() + ":25521/user/InHouseEntertainmentSupervisor/InHouseEntertainmentActor";
        System.out.println(this.inHouseEntertainmentAddr);
        this.inHouseEntertainment = getContext().actorSelection(this.inHouseEntertainmentAddr);
    }
}
