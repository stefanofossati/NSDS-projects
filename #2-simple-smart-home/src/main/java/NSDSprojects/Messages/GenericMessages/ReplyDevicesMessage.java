package NSDSprojects.Messages.GenericMessages;

import java.util.Set;

public class ReplyDevicesMessage {
    Set<String> devices;

    public ReplyDevicesMessage(Set<String> devices) {
        this.devices = devices;
    }

    public Set<String> getDevices() {
        return devices;
    }
}
