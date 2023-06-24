package NSDSprojects.ShippingService.Repository;

import NSDSprojects.Common.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

    public Optional<UserEntity> findById(Long id);

    @Query("SELECT u FROM UserEntity u WHERE u.name = :username")
    public UserEntity findByName(String username);

    public ArrayList<UserEntity> findAll();
}
