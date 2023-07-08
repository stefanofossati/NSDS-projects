package NSDSprojects.Messages.InHouseEntertainment;

public class TVReplyMessage {
    String tv, state;

    public TVReplyMessage(String tv, String state) {
        this.tv = tv;
        this.state = state;
    }

    public String getTv() {
        return tv;
    }

    public String getState() {
        return state;
    }
}
