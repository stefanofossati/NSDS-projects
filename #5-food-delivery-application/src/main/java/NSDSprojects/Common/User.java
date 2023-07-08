package NSDSprojects.Common;

import jakarta.persistence.MappedSuperclass;

@MappedSuperclass
public class User {
    private String name;
    private String address;

    public User() {
    }
    public User(String name) {
        this.name = name;
    }
    public User(String name, String address) {
        this.name = name;
        this.address = address;
    }

    public String getName() {
            return name;
    }

    public String getAddress(){
        return address;
    }
}
