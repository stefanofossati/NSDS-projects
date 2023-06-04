package NSDSprojects.ShippingService.Model;

import NSDSprojects.Common.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    public Order findByGlobalUID(String globalUID);
}

