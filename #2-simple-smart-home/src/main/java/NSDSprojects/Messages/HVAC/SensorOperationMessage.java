package NSDSprojects.Messages.HVAC;

public class SensorOperationMessage {

    private int operation;


    public SensorOperationMessage(int operation) {
        this.operation = operation;
    }

    public int getOperation() {
        return operation;
    }

}
