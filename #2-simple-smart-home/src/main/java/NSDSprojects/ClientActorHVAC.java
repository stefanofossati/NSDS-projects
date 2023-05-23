package NSDSprojects;

import NSDSprojects.Messages.GenericMessages.*;
import NSDSprojects.Messages.HVAC.TemperatureMessage;
import akka.actor.AbstractActor;
import akka.actor.ActorSelection;
import akka.actor.Props;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class ClientActorHVAC extends AbstractActor {
    /*
    String hvacAddr = "akka://HVACServer@192.168.1.229:25520/user/HVACActor";
    ActorSelection hvac = getContext().actorSelection(hvacAddr);
*/
    String hvacAddr;
    ActorSelection hvac;
/*
    Cluster cluster = Cluster.get(getContext().getSystem());

    public void preStart(){
        cluster.subscribe(getSelf(), ClusterEvent.initialStateAsEvents(), ClusterEvent.MemberEvent.class, ClusterEvent.UnreachableMember.class);
    }

    public void postStop(){
        cluster.unsubscribe(getSelf());
    }
*/

    @Override
    public Receive createReceive() {
        return receiveBuilder()

                .match(RequestDeviceMessage.class, this::requestDevices) //1-3

                .match(AddDeviceMessage.class, this::onAddNewDevice) //7
                .match(RemoveDeviceMessage.class, this::onRemoveDevice) //10
                .match(CrashMessage.class, this::onCrash)

                .match(RequestEnergyConsumptionMessage.class, this::getConsumption) //10
                .match(EnergyConsumptionMessage.class,  this::showConsumption) //10

                .match(TextMessage.class, this::showWarning)

                .match(TemperatureMessage.class, this::sendDesiredTemp) //4

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
            File myObj = new File("src/main/java/resources/connectTo/hvac.txt");
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                this.hvacAddr = "akka://HVACServer@" + data + "/user/HVACActor";
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
