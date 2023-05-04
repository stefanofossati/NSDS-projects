package NSDSprojects.InHouseEntertainment;

import akka.actor.ActorRef;

public class TVInfoContainer {
    private ActorRef tvref;
    private String state;

    public TVInfoContainer(ActorRef tvref, String state) {
        this.tvref = tvref;
        this.state = state;
    }

    public ActorRef getTvref() {
        return tvref;
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
