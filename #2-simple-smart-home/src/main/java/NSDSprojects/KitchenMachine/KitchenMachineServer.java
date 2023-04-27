package NSDSprojects.KitchenMachine;

import akka.actor.ActorSystem;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

import java.io.File;

public class KitchenMachineServer {
    public static void main(String[] args){
        Config conf = ConfigFactory.parseFile(new File("kitchenmachine_conf.conf"));
        ActorSystem sys = ActorSystem.create("KitchenMachineServer", conf);
        sys.actorOf(KitchenMachineActor.props(), "KitchenMachineActor");
    }
}
