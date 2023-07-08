package NSDSprojects.Common;

import jakarta.persistence.*;

import java.util.Map;

@MappedSuperclass
public class Order {
    private String name;

    @ElementCollection
    @CollectionTable(name = "items_in_order", joinColumns = @JoinColumn(name = "order_id"))
    @MapKeyColumn(name = "item_name")
    @Column(name = "quantity")
    private Map<String, Integer> items;

    public Order() {
    }


    public Order(String name, Map<String, Integer> items) {
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
