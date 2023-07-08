package NSDSprojects.HVAC;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeoutException;

import static akka.pattern.Patterns.ask;
import static java.util.concurrent.TimeUnit.SECONDS;

public class HVACServer {
    public static void main(String[] args) throws IOException {
        Config conf = ConfigFactory.parseFile(new File("src/main/java/resources/hvac_conf.conf"));
        ActorSystem sys = ActorSystem.create("HVACServer", conf);
        final ActorRef supervisor = sys.actorOf(HVACSupervisor.props(), "HVACSupervisor");
        scala.concurrent.duration.Duration timeout = scala.concurrent.duration.Duration.create(5, SECONDS);
        scala.concurrent.Future<Object> waitingForHVACServer = ask(supervisor, Props.create(HVACActor.class), 5000);
        ActorRef server = null;
        try {
            server = (ActorRef) waitingForHVACServer.result(timeout, null);
        } catch (TimeoutException | InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(server.path());
    }
}
