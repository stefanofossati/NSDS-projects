package NSDSprojects.Common;

import NSDSprojects.Common.Kafka.OrderState;
import jakarta.persistence.*;

import java.util.Map;

@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @ElementCollection
    @CollectionTable(name = "items_in_order", joinColumns = @JoinColumn(name = "order_id"))
    @MapKeyColumn(name = "item_name")
    @Column(name = "quantity")
    private Map<String, Integer> items;

    private OrderState orderState;

    public Order() {

    }

    public Order(String name, Map<String, Integer> items) {
        this.name = name;
        this.items = items;
        this.orderState = OrderState.IN_PREPARATION;
    }

    public Long getId() {
        return id;
    }

    public Map<String, Integer> getItems() {
        return items;
    }

    public String getName() {
        return name;
    }

    public Order(OrderState orderState) {
        this.orderState = orderState;
    }
}
