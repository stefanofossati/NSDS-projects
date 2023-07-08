package NSDSprojects.InHouseEntertainment;

import NSDSprojects.CustomException;
import akka.actor.AbstractActor;
import akka.actor.OneForOneStrategy;
import akka.actor.Props;
import akka.actor.SupervisorStrategy;
import akka.japi.pf.DeciderBuilder;

import java.time.Duration;



public class InHouseEntertainmentSupervisor extends AbstractActor {

    private static SupervisorStrategy strategy =
            new OneForOneStrategy(
                    10,
                    Duration.ofMinutes(1),
                    DeciderBuilder.match(CustomException.class, e -> SupervisorStrategy.resume())
                            .build());
    @Override
    public SupervisorStrategy supervisorStrategy(){
        return strategy;
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(
                        Props.class,
                        props -> {
                            getSender().tell(getContext().actorOf(InHouseEntertainmentActor.props(), "InHouseEntertainmentActor"), getSelf());
                        })
                .build();
    }

    static Props props () {
        return Props.create(InHouseEntertainmentSupervisor.class);
    }
}