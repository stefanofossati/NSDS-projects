package NSDSprojects.Messages.GenericMessages;

public class EnergyConsumptionMessage {
    private float energyConsumption;
    private String from;

    public EnergyConsumptionMessage(float energyConsumption, String from) {
        this.energyConsumption = energyConsumption;
        this.from = from;
    }

    public float getEnergyConsumption() {
        return energyConsumption;
    }

    public String getFrom() {
        return from;
    }
}
