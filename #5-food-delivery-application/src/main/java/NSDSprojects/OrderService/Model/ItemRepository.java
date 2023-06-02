package NSDSprojects.OrderService.Model;

import NSDSprojects.Common.Item;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemRepository extends JpaRepository<Item, Long> {
    public Item findByName(String name);
}
