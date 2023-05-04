package NSDSprojects.KitchenMachine;

import akka.actor.ActorRef;

public class MachineInfoContainer {
    private ActorRef machineref;

    private String state;

    public MachineInfoContainer(ActorRef machineref, String state) {
        this.machineref = machineref;
        this.state = state;
    }

    public ActorRef getMachineref() {
        return machineref;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public void changeState(){
        if(this.state.equals("on")){
            this.state = "off";
        } else {
            if(this.state.equals("off")){
                this.state = "on";
            }
        }
    }
}
