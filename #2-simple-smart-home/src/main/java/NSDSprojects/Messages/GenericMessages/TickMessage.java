package NSDSprojects.Messages.GenericMessages;

import akka.actor.ActorRef;

public class TickMessage {
    ActorRef replyTo;

    public TickMessage(ActorRef replyTo) {
        this.replyTo = replyTo;
    }

    public ActorRef getReplyTo() {
        return replyTo;
    }
}
