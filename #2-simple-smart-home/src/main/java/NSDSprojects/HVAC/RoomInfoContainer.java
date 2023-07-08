package NSDSprojects.HVAC;

import akka.actor.ActorRef;

public class RoomInfoContainer {
    private ActorRef roomref;
    private float temp, desiredTemp;

    public RoomInfoContainer(ActorRef roomref, float temp, float desiredTemp) {
        this.roomref = roomref;
        this.temp = temp;
        this.desiredTemp = desiredTemp;
    }

    public ActorRef getRoomref() {
        return roomref;
    }

    public float getTemp() {
        return temp;
    }

    public float getDesiredTemp() {
        return desiredTemp;
    }

    public void setTemp(float temp) {
        this.temp = temp;
    }

    public void setDesiredTemp(float desiredTemp) {
        this.desiredTemp = desiredTemp;
    }
}
