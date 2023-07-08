package NSDSprojects.Messages.KitchenMachine;

public class MachineReplyMessage {
    String machine, state;

    public MachineReplyMessage(String machine, String state) {
        this.machine = machine;
        this.state = state;
    }

    public String getMachine() {
        return machine;
    }

    public String getState() {
        return state;
    }
}
