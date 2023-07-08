package NSDSprojects.KitchenMachine;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import com.typesafe.config.ConfigValueFactory;

import java.io.File;
import java.util.concurrent.TimeoutException;

import static akka.pattern.Patterns.ask;
import static java.util.concurrent.TimeUnit.SECONDS;

public class KitchenMachineServer {
    public static void main(String[] args){
        Config conf = ConfigFactory.load("kitchenmachine_conf.conf");
        Config conf2 = conf.withValue("akka.remote.artery.canonical.hostname", ConfigValueFactory.fromAnyRef(returnIP(args)));
        ActorSystem sys = ActorSystem.create("KitchenMachineServer", conf2);
        final ActorRef supervisor = sys.actorOf(KitchenMachineSupervisor.props(), "KitchenMachineSupervisor");
        scala.concurrent.duration.Duration timeout = scala.concurrent.duration.Duration.create(5, SECONDS);
        scala.concurrent.Future<Object> waitingForKMServer = ask(supervisor, Props.create(KitchenMachineActor.class), 5000);
        ActorRef server = null;
        try {
            server = (ActorRef) waitingForKMServer.result(timeout, null);
        } catch (TimeoutException | InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(server.path());
    }

    private static String returnIP(String[] args) {
        String[] split = args[0].split(":");
        if(!split[0].equalsIgnoreCase("IP")){
            System.out.println("Wrong argument. Please use IP:xxx.xxx.xxx.xxx");
            System.exit(1);
        }
        return split[1];
    }
}
