package NSDSprojects.Common;

import jakarta.persistence.*;

import java.util.Map;

@Entity
@Table(name = "orders")
public class OrderEntity  extends Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private OrderState orderState;

    public OrderEntity() {
    }

    public OrderEntity(String name, Map<String, Integer> items) {
        super(name, items);
        this.orderState = OrderState.IN_PREPARATION;
    }

    public OrderEntity(Long Id, String name, Map<String, Integer> items) {
        super(name, items);
        this.id = Id;
        this.orderState = OrderState.IN_PREPARATION;
    }

    public Long getId() {
        return id;
    }


    public OrderState getOrderState() {
        return orderState;
    }

    public void setOrderState(OrderState orderState) {
        this.orderState = orderState;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
    }

    public void setItems(Map<String, Integer> items) {
    }
}
