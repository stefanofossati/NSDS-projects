package NSDSprojects.Messages.HVAC;

import com.fasterxml.jackson.annotation.JsonCreator;

public class SensorReplyMessage {

    private String room;
    private float temp;
    private boolean active;
    @JsonCreator
    public SensorReplyMessage(String room, float temp, boolean active) {
        this.room = room;
        this.temp = temp;
        this.active = active;
    }

    public String getRoom() {
        return room;
    }

    public float getTemp() {
        return temp;
    }

    public boolean isActive() {
        return active;
    }
}
