package NSDSprojects.Common;

import jakarta.persistence.*;

import java.util.Map;

@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    final private String name;

    @ElementCollection
    @CollectionTable(name = "items", joinColumns = @JoinColumn(name = "order_id"))
    @MapKeyColumn(name = "item_name")
    @Column(name = "quantity")
    final private Map<String, Integer> items;


    public Order(String name, Map<String, Integer> items) {
        this.name = name;
        this.items = items;
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
}
