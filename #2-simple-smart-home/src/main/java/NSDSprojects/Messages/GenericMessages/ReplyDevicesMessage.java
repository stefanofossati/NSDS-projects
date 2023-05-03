package NSDSprojects.Messages.GenericMessages;

import com.fasterxml.jackson.annotation.JsonCreator;

import java.util.Set;

public class ReplyDevicesMessage {
    Set<String> devices;

    @JsonCreator
    public ReplyDevicesMessage(Set<String> devices) {
        this.devices = devices;
    }

    public Set<String> getDevices() {
        return devices;
    }
}
