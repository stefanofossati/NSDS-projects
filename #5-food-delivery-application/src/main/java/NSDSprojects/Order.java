package NSDSprojects;

import jakarta.persistence.*;

@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;


    public Order() {
        name ="cose";
    }

    public Long getId() {
        return id;
    }
}
