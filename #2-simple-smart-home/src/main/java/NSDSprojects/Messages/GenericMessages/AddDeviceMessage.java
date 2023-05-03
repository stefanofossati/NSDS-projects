package NSDSprojects.Messages.GenericMessages;

import com.fasterxml.jackson.annotation.JsonCreator;

public class AddDeviceMessage {
    String deviceid;
    @JsonCreator
    public AddDeviceMessage(String deviceId) {
        this.deviceid = deviceId;
    }

    public String getDeviceid() {
        return deviceid;
    }
}
