package NSDSprojects.Messages.KitchenMachine;

public class AddMachineMessage {
    String deviceid;

    public AddMachineMessage(String deviceId) {
        this.deviceid = deviceId;
    }

    public String getDeviceid() {
        return deviceid;
    }

}
