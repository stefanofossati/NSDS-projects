package NSDSprojects.Messages.HVAC;

import com.fasterxml.jackson.annotation.JsonCreator;

public class TemperatureMessage {

    private float temp;
    private String room;
    @JsonCreator
    public TemperatureMessage(float temp, String room) {
        this.temp = temp;
        this.room = room;
    }

    public float getTemp() {
        return temp;
    }

    public String getRoom() {
        return room;
    }
}
