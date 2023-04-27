package NSDSprojects.Messages.HVAC;

public class SensorReplyMessage {

    private String room;
    private float temp;
    private boolean active;

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
