package NSDSprojects.Messages.HVAC;

public class TemperatureMessage {

    private float temp;
    private String room;

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
