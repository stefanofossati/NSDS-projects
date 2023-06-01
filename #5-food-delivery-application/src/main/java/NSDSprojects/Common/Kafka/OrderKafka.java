package NSDSprojects.Common.Kafka;

import java.util.Map;

public class OrderKafka {
    private String name;

    private Map<String, Integer> items;

    public OrderKafka(String name, Map<String, Integer> items) {
        this.name = name;
        this.items = items;
    }

    public Map<String, Integer> getItems() {
        return items;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setItems(Map<String, Integer> items) {
        this.items = items;
    }

}
