package NSDSprojects.Messages.GenericMessages;

import com.fasterxml.jackson.annotation.JsonCreator;

public class TextMessage {
    String text;
    @JsonCreator
    public TextMessage(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }
}
