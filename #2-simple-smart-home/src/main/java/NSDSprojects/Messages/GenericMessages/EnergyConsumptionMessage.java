package NSDSprojects.Messages.GenericMessages;
import com.fasterxml.jackson.annotation.JsonCreator;
public class EnergyConsumptionMessage {
    private float energyConsumption;
    private String from;
    @JsonCreator
    public EnergyConsumptionMessage(float energyConsumption) {
        this.energyConsumption = energyConsumption;
    }

    public float getEnergyConsumption() {
        return energyConsumption;
    }
}
