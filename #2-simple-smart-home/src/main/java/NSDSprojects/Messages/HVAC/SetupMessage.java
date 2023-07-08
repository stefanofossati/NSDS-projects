package NSDSprojects.Messages.HVAC;

import com.fasterxml.jackson.annotation.JsonCreator;

public class SetupMessage {
    float initialTemp;
    String room;
    @JsonCreator
    public SetupMessage(String room, float initialTemp) {
        this.initialTemp = initialTemp;
        this.room = room;
    }

    public float getInitialTemp() {
        return initialTemp;
    }

    public String getRoom() {
        return room;
    }
}
