package NSDSprojects.Messages.HVAC;

public class RemoveSensorMessage {
    String deviceid;

    public RemoveSensorMessage(String deviceid) {
        this.deviceid = deviceid;
    }

    public String getDeviceid() {
        return deviceid;
    }
}
