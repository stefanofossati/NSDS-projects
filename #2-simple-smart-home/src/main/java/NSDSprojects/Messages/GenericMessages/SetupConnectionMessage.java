package NSDSprojects.Messages.GenericMessages;

public class SetupConnectionMessage {
    String info;

    public SetupConnectionMessage(String info) {
        this.info = info;
    }

    public String getInfo() {
        return info;
    }
}
