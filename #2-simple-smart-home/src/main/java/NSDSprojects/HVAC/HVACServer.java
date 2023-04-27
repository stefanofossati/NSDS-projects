package NSDSprojects.HVAC;

import akka.actor.ActorSystem;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

import java.io.File;
import java.io.IOException;

public class HVACServer {
    public static void main(String[] args) throws IOException {
        File configurationfile = new File("src/main/java/resources/hvac_conf.conf");
        Config conf = ConfigFactory.parseFile(configurationfile);

        ActorSystem sys = ActorSystem.create("HVACServer", conf);
        sys.actorOf(HVACActor.props(), "HVACActor");
    }
}
