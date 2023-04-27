package NSDSprojects.Messages.InHouseEntertainment;

public class AddTVMessage {
    String deviceid;

    public AddTVMessage(String deviceId) {
        this.deviceid = deviceId;
    }

    public String getDeviceid() {
        return deviceid;
    }

}
