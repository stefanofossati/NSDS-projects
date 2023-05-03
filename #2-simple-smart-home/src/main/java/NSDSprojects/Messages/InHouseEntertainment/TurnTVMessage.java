package NSDSprojects.Messages.InHouseEntertainment;

import com.fasterxml.jackson.annotation.JsonCreator;

public class TurnTVMessage {
    String tv;
    @JsonCreator
    public TurnTVMessage(String tv) {
        this.tv = tv;
    }

    public String getTv() {
        return tv;
    }
}
