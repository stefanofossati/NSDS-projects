package NSDSprojects.Common;

import jakarta.persistence.*;

@Entity
@Table(name = "users")
public class UserEntity extends User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    public UserEntity() {
        super();
    }

    public UserEntity(Long id, String name, String address) {
        super(name, address);
        this.id = id;
    }

    public UserEntity(String name, String address) {
        super(name, address);
    }

    public Long getId() {
        return id;
    }
}
