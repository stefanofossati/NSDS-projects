package NSDSprojects.Messages.GenericMessages;

import akka.actor.ActorRef;
import com.fasterxml.jackson.annotation.JsonCreator;

public class TickMessage {
    ActorRef replyTo;
    @JsonCreator
    public TickMessage(ActorRef replyTo) {
        this.replyTo = replyTo;
    }

    public ActorRef getReplyTo() {
        return replyTo;
    }
}
