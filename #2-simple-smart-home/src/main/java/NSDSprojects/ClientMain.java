package NSDSprojects;

import NSDSprojects.Messages.GenericMessages.*;
import NSDSprojects.Messages.HVAC.TemperatureMessage;
import NSDSprojects.Messages.KitchenMachine.TurnMachineMessage;
import NSDSprojects.Messages.InHouseEntertainment.TurnTVMessage;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;



import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class ClientMain {
    public static void main(String[] args) {
        boolean on = true, deviceONOFF;
        String device;
        float desiredTemp;

        Config conf = ConfigFactory.parseFile(new File("src/main/resources/client_conf.conf"));

        final ActorSystem sys = ActorSystem.create("System", conf);

        final ActorRef clientHVAC = sys.actorOf(ClientActorHVAC.props(), "clientHVAC");
        clientHVAC.tell(new SetupConnectionMessage(), ActorRef.noSender());
        final ActorRef clientIHE = sys.actorOf(ClientActorIHE.props(), "clientIHE");
        clientIHE.tell(new SetupConnectionMessage(), ActorRef.noSender());
        final ActorRef clientKM = sys.actorOf(ClientActorKM.props(), "clientKM");
        clientKM.tell(new SetupConnectionMessage(), ActorRef.noSender());

        while (on) {

            System.out.println("Select command:");

            System.out.println("1 - Show rooms");
            System.out.println("2 - Show TVs");
            System.out.println("3 - Show KitchenMachines");

            System.out.println("4 - Set Temperature in a Room");
            System.out.println("5 - ON/OFF on TV");
            System.out.println("6 - ON/OFF on KitchenMachine");

            System.out.println("7 - Setup new Sensor");
            System.out.println("8 - Setup new TV");
            System.out.println("9 - Setup new KitchenMachine");

            System.out.println("10 - Remove sensor");
            System.out.println("11 - Remove TV");
            System.out.println("12 - Remove KitchenMachine");

            System.out.println("13 - Show Energy Consumption");

            System.out.println("14 - Turn OFF Panel");

            System.out.println("15 - Make sensor crash");
            System.out.println("16 - Make tv crash");
            System.out.println("17 - Make machine crash");

            System.out.println("18 - Make HVACServer crash");
            System.out.println("19 - Make IHEServer crash");
            System.out.println("20 - Make KMServer crash");

            Scanner scan = new Scanner(System.in);

            int selection = scan.nextInt();

            switch (selection) {
                case 1:
                    clientHVAC.tell(new RequestDeviceMessage(), ActorRef.noSender());
                    break;
                case 2:
                    clientIHE.tell(new RequestDeviceMessage(), ActorRef.noSender());
                    break;
                case 3:
                    clientKM.tell(new RequestDeviceMessage(), ActorRef.noSender());
                    break;
                case 4:
                    System.out.println("Select room: ");
                    device = scan.next();
                    System.out.println("Insert desired temperature:");
                    desiredTemp = scan.nextFloat();
                    clientHVAC.tell(new TemperatureMessage(desiredTemp*10, device), ActorRef.noSender());
                    break;
                case 5:
                    System.out.println("Select TV: ");
                    device = scan.next();
                    clientIHE.tell(new TurnTVMessage(device), ActorRef.noSender());
                    break;
                case 6:
                    System.out.println("Select KitchenMachine: ");
                    device = scan.next();
                    clientKM.tell(new TurnMachineMessage(device), ActorRef.noSender());
                    break;
                case 7:
                    System.out.println("Insert new room name:");
                    device = scan.next();
                    clientHVAC.tell(new AddDeviceMessage(device), ActorRef.noSender());
                    break;
                case 8:
                    System.out.println("Insert new TV identifier: ");
                    device = scan.next();
                    clientIHE.tell(new AddDeviceMessage(device), ActorRef.noSender());
                    break;
                case 9:
                    System.out.println("Insert new KitchenMachine identifier: ");
                    device = scan.next();
                    clientKM.tell(new AddDeviceMessage(device), ActorRef.noSender());
                    break;
                case 10:
                    System.out.println("Insert room to be removed: ");
                    device = scan.next();
                    clientHVAC.tell(new RemoveDeviceMessage(device), ActorRef.noSender());
                    break;
                case 11:
                    System.out.println("Insert TVID to be removed: ");
                    device = scan.next();
                    clientIHE.tell(new RemoveDeviceMessage(device), ActorRef.noSender());
                    break;
                case 12:
                    System.out.println("Insert KitchenMachineID to be removed: ");
                    device = scan.next();
                    clientKM.tell(new RemoveDeviceMessage(device), ActorRef.noSender());
                    break;
                case 13:
                    System.out.println("Consumption:");
                    clientHVAC.tell(new RequestEnergyConsumptionMessage(), ActorRef.noSender());
                    clientIHE.tell(new RequestEnergyConsumptionMessage(), ActorRef.noSender());
                    clientKM.tell(new RequestEnergyConsumptionMessage(), ActorRef.noSender());
                    break;
                case 14:
                    on = false;
                    break;
                case 15:
                    System.out.println("Select room: ");
                    device = scan.next();
                    clientHVAC.tell(new CrashMessage(device), ActorRef.noSender());
                    break;
                case 16:
                    System.out.println("Select tv: ");
                    device = scan.next();
                    clientIHE.tell(new CrashMessage(device), ActorRef.noSender());
                    break;
                case 17:
                    System.out.println("Select machine: ");
                    device = scan.next();
                    clientKM.tell(new CrashMessage(device), ActorRef.noSender());
                    break;

                case 18:
                    clientHVAC.tell(new CrashServerMessage(), ActorRef.noSender());
                case 19:
                    clientIHE.tell(new CrashServerMessage(), ActorRef.noSender());
                case 20:
                    clientKM.tell(new CrashServerMessage(), ActorRef.noSender());
                default:
                    System.out.println("Wrong command inserted");
            }
        }
        System.out.println("Panel Off");
    }

    static private String selectDevice (){
        return null;
    }

    private Map<String, String> ParsArg(String[] arg){
        Map<String, String> map = new HashMap<String, String>();
        for (int i = 0; i < 4; i++) {
            String[] split = arg[i].split(":");
            if(split[0].equalsIgnoreCase("CLIENT") || split[0].equalsIgnoreCase("HVAC") || split[0].equalsIgnoreCase("IHE") || split[0].equalsIgnoreCase("KM")){
                map.put(split[0].toUpperCase(), split[1]);
            }
        }

        if (map.isEmpty()) {
            System.out.println("Wrong arguments");
            System.exit(1);
        }
        return map;
    }
}