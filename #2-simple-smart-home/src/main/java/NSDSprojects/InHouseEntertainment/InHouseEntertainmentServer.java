package NSDSprojects.InHouseEntertainment;

import NSDSprojects.InHouseEntertainment.InHouseEntertainmentActor;
import akka.actor.ActorSystem;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

import java.io.File;

public class InHouseEntertainmentServer {
    public static void main(String[] args){
        Config conf = ConfigFactory.parseFile(new File("src/main/java/resources/inhouseentertainment_conf.conf"));
        ActorSystem sys = ActorSystem.create("InHouseEntertainmentServer", conf);
        sys.actorOf(InHouseEntertainmentActor.props(), "InHouseEntertainmentActor");
    }
}
