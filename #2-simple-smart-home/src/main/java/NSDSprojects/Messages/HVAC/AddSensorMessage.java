package NSDSprojects.Messages.HVAC;

public class AddSensorMessage {
    String deviceid;

    public AddSensorMessage(String deviceId) {
        this.deviceid = deviceId;
    }

    public String getDeviceid() {
        return deviceid;
    }

}
