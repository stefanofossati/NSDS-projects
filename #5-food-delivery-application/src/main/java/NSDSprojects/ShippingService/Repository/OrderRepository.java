package NSDSprojects.ShippingService.Repository;

import NSDSprojects.Common.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<OrderEntity, Long> {
    public Optional<OrderEntity> findById(Long Id);

    @Query("SELECT o FROM OrderEntity o WHERE o.name = :username")
    public ArrayList<OrderEntity> findByUsername(String username);

    public ArrayList<OrderEntity> findAll();
}

