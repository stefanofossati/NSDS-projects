package NSDSprojects.ShippingService.Repository;

import NSDSprojects.Common.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    public Order findByGlobalUID(String globalUID);

    @Query("SELECT o FROM Order o WHERE o.name = :username")
    public ArrayList<Order> findByUsername(String username);

    public ArrayList<Order> findAll();
}

