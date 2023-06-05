package NSDSprojects.OrderService.Model;

import NSDSprojects.Common.Order;
import jakarta.persistence.Entity;
import jakarta.persistence.*;
import jakarta.persistence.Table;

import java.util.Map;

@Entity
@Table(name = "orders_outbox")
public class OrderOutbox{
    @Id
    private Long Id;

    private String name;

    @ElementCollection
    @CollectionTable(name = "items_in_order_outbox", joinColumns = @JoinColumn(name = "order_outbox_id"))
    @MapKeyColumn(name = "item_name_outbox")
    @Column(name = "quantity_outbox")
    private Map<String, Integer> items;

    private boolean isSent;

    public OrderOutbox() {
    }
    public OrderOutbox(Long id, Order order){
        Id = id;
        name = order.getName();
        items = order.getItems();
        isSent = false;
    }

    public Long getId() {
        return Id;
    }

    public boolean getIsSent() {
        return isSent;
    }

    public String getName() {
        return name;
    }

    public Map<String, Integer> getItems() {
        return items;
    }

    public void setId(Long id) {
        Id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setItems(Map<String, Integer> items) {
        this.items = items;
    }

    public void setIsSent(boolean isSent) {
        this.isSent = isSent;
    }
}
