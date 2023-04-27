package NSDSprojects.Messages.GenericMessages;

public class RequestAllDeviceMessage {
    String controller;

    public RequestAllDeviceMessage(String controller) {
        this.controller = controller;
    }

    public String getController() {
        return controller;
    }

}
