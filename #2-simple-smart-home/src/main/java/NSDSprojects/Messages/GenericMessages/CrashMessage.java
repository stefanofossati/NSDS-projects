package NSDSprojects.Messages.GenericMessages;

import com.fasterxml.jackson.annotation.JsonCreator;

public class CrashMessage {
    String deviceid;
    @JsonCreator
    public CrashMessage(String deviceId) {
        this.deviceid = deviceId;
    }
    public String getDeviceid() {
        return deviceid;
    }
}
