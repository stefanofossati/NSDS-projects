package NSDSprojects.OrderService.Model;

import java.util.Map;

public class OrderRequest {
    private String name;
    private Map<String, Integer> items;

    public OrderRequest() {}

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
