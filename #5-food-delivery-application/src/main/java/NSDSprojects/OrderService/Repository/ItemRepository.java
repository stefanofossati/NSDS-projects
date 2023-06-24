package NSDSprojects.OrderService.Repository;

import NSDSprojects.Common.Item;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.ArrayList;
import java.util.Map;

public interface ItemRepository extends JpaRepository<Item, Long> {
    public Item findByName(String name);

    ArrayList<Item> findAll();
}
