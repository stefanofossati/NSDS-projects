package NSDSprojects.ShippingService.Model;

import NSDSprojects.Common.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShippingRepository extends JpaRepository<User, Long> {
}
