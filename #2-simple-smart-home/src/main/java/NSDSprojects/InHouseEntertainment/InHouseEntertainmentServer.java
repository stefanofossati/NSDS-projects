package NSDSprojects.InHouseEntertainment;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

import java.io.File;
import java.util.concurrent.TimeoutException;

import static akka.pattern.Patterns.ask;
import static java.util.concurrent.TimeUnit.SECONDS;

public class InHouseEntertainmentServer {
    public static void main(String[] args){
        Config conf = ConfigFactory.parseFile(new File("src/main/resources/inhouseentertainment_conf.conf"));
        ActorSystem sys = ActorSystem.create("InHouseEntertainmentServer", conf);
        final ActorRef supervisor = sys.actorOf(InHouseEntertainmentSupervisor.props(), "InHouseEntertainmentSupervisor");
        scala.concurrent.duration.Duration timeout = scala.concurrent.duration.Duration.create(5, SECONDS);
        scala.concurrent.Future<Object> waitingForIHEServer = ask(supervisor, Props.create(InHouseEntertainmentActor.class), 5000);
        ActorRef server = null;
        try {
            server = (ActorRef) waitingForIHEServer.result(timeout, null);
        } catch (TimeoutException | InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(server.path());
    }
}
