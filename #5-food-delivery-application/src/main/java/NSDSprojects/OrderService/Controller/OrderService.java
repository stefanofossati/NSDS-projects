package NSDSprojects.OrderService.Controller;

import NSDSprojects.Common.Item;
import NSDSprojects.Common.OrderEntity;
import NSDSprojects.OrderService.Model.AvailabilityRequest;
import NSDSprojects.OrderService.Model.OrderOutbox;
import NSDSprojects.OrderService.Model.OrderRequest;
import NSDSprojects.OrderService.Repository.ItemRepository;
import NSDSprojects.OrderService.Repository.OrderOutboxRepository;
import NSDSprojects.OrderService.Repository.OrderRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;

@Service
public class OrderService {

    private Logger logger = LoggerFactory.getLogger(OrderService.class);
    private final OrderProducer orderProducer;

    private final OrderRepository orderRepository;

    private final OrderOutboxRepository orderOutboxRepository;
    private final ItemRepository itemRepository;

    @Autowired
    public OrderService(OrderProducer orderProducer, OrderRepository orderRepository, OrderOutboxRepository orderOutboxRepository, ItemRepository itemRepository) {
        this.orderProducer = orderProducer;
        this.orderRepository = orderRepository;
        this.orderOutboxRepository = orderOutboxRepository;
        this.itemRepository = itemRepository;
    }

    @Transactional
    public void createOrder(OrderEntity orderEntity) {
        logger.debug("Order to send");
        // for each item inserted in the order, reduce its availability in the storage
        orderEntity.getItems().forEach((key, value) -> {
            Item item = itemRepository.findByName(key);
            item.setAmount(item.getAmount() - value);
            itemRepository.save(item);
        });
        // save the order in the database to be sent later by the sender tasks
        OrderEntity orderEntitySaved = orderRepository.save(orderEntity);
        logger.debug("Order saved in DB");
        OrderOutbox orderOutbox = new OrderOutbox(orderEntitySaved.getId(), orderEntitySaved); //maybe we can use a trigger to do this
        orderOutboxRepository.save(orderOutbox);
        logger.debug("Order saved in outbox");
        //orderProducer.send( "dd", new OrderKafka(order.getName(), order.getItems()));
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

    public ArrayList<Item> getAvailability() {
        return itemRepository.findAll();
    }
}
