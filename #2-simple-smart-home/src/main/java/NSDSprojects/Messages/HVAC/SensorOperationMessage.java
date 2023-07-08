package NSDSprojects.Messages.HVAC;

import com.fasterxml.jackson.annotation.JsonCreator;

public class SensorOperationMessage {

    private int operation;

    @JsonCreator
    public SensorOperationMessage(int operation) {
        this.operation = operation;
    }

    public int getOperation() {
        return operation;
    }

}
