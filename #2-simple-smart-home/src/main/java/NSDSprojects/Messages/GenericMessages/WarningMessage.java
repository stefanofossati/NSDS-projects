package NSDSprojects.Messages.GenericMessages;

import com.fasterxml.jackson.annotation.JsonCreator;

public class WarningMessage {
    String text;
    @JsonCreator
    public WarningMessage(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }
}
