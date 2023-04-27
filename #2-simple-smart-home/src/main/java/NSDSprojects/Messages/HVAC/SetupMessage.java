package NSDSprojects.Messages.HVAC;

public class SetupMessage {
    float initialTemp;
    String room;

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
