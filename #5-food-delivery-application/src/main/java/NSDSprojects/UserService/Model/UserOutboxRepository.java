package NSDSprojects.UserService.Model;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserOutboxRepository extends JpaRepository<UserOutbox, Long> {

    @Query(value = "SELECT * FROM users_outbox WHERE is_sent = false", nativeQuery = true)
    List<UserOutbox> notSent();
}
