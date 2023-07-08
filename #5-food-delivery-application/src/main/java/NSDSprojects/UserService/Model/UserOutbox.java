package NSDSprojects.UserService.Model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "users_outbox")
public class UserOutbox {
    @Id
    private Long Id;

    private String name;

    private String address;

    private boolean isSent;

    public UserOutbox() {
    }

    public UserOutbox(Long id, String name, String address) {
        Id = id;
        this.name = name;
        this.address = address;
        isSent = false;
    }

    public Long getId() {
        return Id;
    }

    public String getName() {
        return name;
    }
    public String getAddress() {
        return address;
    }

    public boolean getIsSent() {
        return isSent;
    }

    public void setId(Long id) {
        Id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setIsSent(boolean sent) {
        isSent = sent;
    }
}

