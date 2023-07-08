package NSDSprojects.Messages.KitchenMachine;

import com.fasterxml.jackson.annotation.JsonCreator;

public class TurnMachineMessage {
    String kitchenmachine;
    @JsonCreator
    public TurnMachineMessage(String kitchenmachine) {
        this.kitchenmachine = kitchenmachine;
    }

    public String getKitchenmachine() {
        return kitchenmachine;
    }
}
