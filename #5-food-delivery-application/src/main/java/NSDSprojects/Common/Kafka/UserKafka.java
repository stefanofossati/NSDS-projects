package NSDSprojects.Common.Kafka;

public class UserKafka {
    private String name;
    private String address;

    public UserKafka() {
    }

    public UserKafka(String name, String address) {
        this.name = name;
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
