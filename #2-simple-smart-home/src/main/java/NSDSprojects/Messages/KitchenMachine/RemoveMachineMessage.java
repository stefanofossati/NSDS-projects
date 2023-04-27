package NSDSprojects.Messages.KitchenMachine;

public class RemoveMachineMessage {
    String deviceid;

    public RemoveMachineMessage(String deviceid) {
        this.deviceid = deviceid;
    }

    public String getDeviceid() {
        return deviceid;
    }
}
