package NSDSprojects.ShippingService.Model;

import NSDSprojects.Common.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    public User findByGlobalUID(String globalUID);

    @Query("SELECT u FROM User u WHERE u.name = :username")
    public User findByName(String username);

    public ArrayList<User> findAll();
}
