package NSDSprojects.Messages.GenericMessages;

import com.fasterxml.jackson.annotation.JsonCreator;

public class RemoveDeviceMessage {
    String deviceid;
    @JsonCreator
    public RemoveDeviceMessage(String deviceid) {
        this.deviceid = deviceid;
    }

    public String getDeviceid() {
        return deviceid;
    }
}
