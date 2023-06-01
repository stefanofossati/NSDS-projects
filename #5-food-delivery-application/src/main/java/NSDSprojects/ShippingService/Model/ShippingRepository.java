package NSDSprojects.ShippingService.Model;

import NSDSprojects.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShippingRepository extends JpaRepository<User, Long> {
}
