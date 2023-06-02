package NSDSprojects.OrderService.Model;

import NSDSprojects.Common.Item;
import NSDSprojects.Common.Kafka.OrderKafka;
import NSDSprojects.Common.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderService {
    private final OrderProducer orderProducer;

    private final OrderRepository orderRepository;
    private final ItemRepository itemRepository;

    @Autowired
    public OrderService(OrderProducer orderProducer, OrderRepository orderRepository, ItemRepository itemRepository) {
        this.orderProducer = orderProducer;
        this.orderRepository = orderRepository;
        this.itemRepository = itemRepository;
    }

    @Transactional
    public void createOrder(Order order) {
        System.out.println("Order to send");
        // for each item inserted in the order, reduce its availability in the storage
        order.getItems().forEach((key, value) -> {
            Item item = itemRepository.findByName(key);
            item.setAmount(item.getAmount() - value);
            itemRepository.save(item);
        });
        // save the order in the database to be sent later by the sender tasks
        orderRepository.save(order);
        orderProducer.send(new OrderKafka(order.getName(), order.getItems()));
    }

    public boolean modifyAvailability(AvailabilityRequest availabilityRequest) {
        Item itemRecord = itemRepository.findByName(availabilityRequest.getItemName());
        if(itemRecord == null) {
            // item not existing yet
            Item createdItem = new Item(availabilityRequest.getItemName(), availabilityRequest.getAmount());
            itemRepository.save(createdItem);
            return false;
        } else {
            // item already existing, simply update its availability
            itemRecord.setAmount(availabilityRequest.getAmount());
            itemRepository.save(itemRecord);
            return true;
        }
    }

    public boolean checkItemsAvailability(OrderRequest order) {
        return order.getItems().entrySet().stream().allMatch((x) -> itemRepository.findByName(x.getKey()).getAmount() >= x.getValue());
    }
}
