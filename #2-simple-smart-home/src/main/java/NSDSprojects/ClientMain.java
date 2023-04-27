package NSDSprojects;

import NSDSprojects.Messages.GenericMessages.RequestEnergyConsumptionMessage;
import NSDSprojects.Messages.HVAC.AddSensorMessage;
import NSDSprojects.Messages.GenericMessages.RequestAllDeviceMessage;
import NSDSprojects.Messages.HVAC.RemoveSensorMessage;
import NSDSprojects.Messages.HVAC.TemperatureMessage;
import NSDSprojects.Messages.InHouseEntertainment.AddTVMessage;
import NSDSprojects.Messages.InHouseEntertainment.RemoveTVMessage;
import NSDSprojects.Messages.KitchenMachine.AddMachineMessage;
import NSDSprojects.Messages.KitchenMachine.RemoveMachineMessage;
import NSDSprojects.Messages.KitchenMachine.TurnKitchenMachineMessage;
import NSDSprojects.Messages.InHouseEntertainment.TurnTVMessage;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;

import java.util.Scanner;

public class ClientMain {
    public static void main(String[] args) {
        boolean on = true, deviceONOFF;
        String device;
        float desiredTemp;

        final ActorSystem sys = ActorSystem.create("System");
        final ActorRef client = sys.actorOf(ClientActor.props(), "client");

        while (on) {


            System.out.println("Select command:");

            System.out.println("1 - Show rooms");
            System.out.println("2 - Show TVs");
            System.out.println("3 - Show KitchenMachines");

            System.out.println("4 - Set Temperature in a Room");
            System.out.println("5 - ON/OFF on TV");
            System.out.println("6 - ON/OFF on KitchenMachine");

            System.out.println("7 - Setup new Sensor");
            System.out.println("8 - Setup new TV:");
            System.out.println("9 - Setup new KitchenMachine");

            System.out.println("10 - Remove sensor");
            System.out.println("11 - Remove TV");
            System.out.println("12 - Remove KitchenMachine");

            System.out.println("13 - Show Energy Consumption");

            System.out.println("14 - Turn OFF Panel");

            Scanner scan = new Scanner(System.in);

            int selection = scan.nextInt();

            switch (selection) {
                case 1:
                    client.tell(new RequestAllDeviceMessage("HVAC"), ActorRef.noSender());
                    break;
                case 2:
                    client.tell(new RequestAllDeviceMessage("InHouseEnt"), ActorRef.noSender());
                    break;
                case 3:
                    client.tell(new RequestAllDeviceMessage("KitchenMachines"), ActorRef.noSender());
                    break;
                case 4:
                    System.out.println("Select room: ");
                    device = scan.next();
                    System.out.println("Insert desired temperature:");
                    desiredTemp = scan.nextFloat();
                    client.tell(new TemperatureMessage(desiredTemp, device), ActorRef.noSender());
                    break;
                case 5:
                    System.out.println("Select TV: ");
                    device = scan.next();
                    client.tell(new TurnTVMessage(device), ActorRef.noSender());
                    break;
                case 6:
                    System.out.println("Select KitchenMachine: ");
                    device = scan.nextLine();
                    client.tell(new TurnKitchenMachineMessage(device), ActorRef.noSender());
                    break;
                case 7:
                    System.out.println("Insert new room name:");
                    device = scan.next();
                    client.tell(new AddSensorMessage(device), ActorRef.noSender());
                    break;
                case 8:
                    System.out.println("Insert new TV identifier: ");
                    device = scan.next();
                    client.tell(new AddTVMessage(device), ActorRef.noSender());
                    break;
                case 9:
                    System.out.println("Insert new KitchenMachine identifier: ");
                    device = scan.next();
                    client.tell(new AddMachineMessage(device), ActorRef.noSender());
                    break;
                case 10:
                    System.out.println("Insert room to be removed: ");
                    device = scan.next();
                    client.tell(new RemoveSensorMessage(device), ActorRef.noSender());
                case 11:
                    System.out.println("Insert TVID to be removed: ");
                    device = scan.next();
                    client.tell(new RemoveTVMessage(device), ActorRef.noSender());
                case 12:
                    System.out.println("Insert KitchenMachineID to be removed: ");
                    device = scan.next();
                    client.tell(new RemoveMachineMessage(device), ActorRef.noSender());
                case 13:
                    System.out.println("Consumption:");
                    client.tell(new RequestEnergyConsumptionMessage(), ActorRef.noSender());
                case 14:
                    on = false;
                    break;
                default:
                    System.out.println("Wrong command inserted");
            }
        }
        System.out.println("Panel Off");
    }

    static private String selectDevice (){
        return null;
    }
}