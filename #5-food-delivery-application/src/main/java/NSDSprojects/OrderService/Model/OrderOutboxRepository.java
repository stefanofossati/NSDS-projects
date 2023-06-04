package NSDSprojects.OrderService.Model;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderOutboxRepository extends JpaRepository<OrderOutbox, Long> {

    @Query(value = "SELECT * FROM orders_outbox WHERE is_sent = false", nativeQuery = true)
    List<OrderOutbox> notSent();
}
