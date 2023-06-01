package NSDSprojects.OrderService.Model;

import NSDSprojects.Common.Kafka.OrderKafka;
import NSDSprojects.Common.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class OrderService {
    private final OrderProducer orderProducer;

    private final OrderRepository orderRepository;

    @Autowired
    public OrderService(OrderProducer orderProducer, OrderRepository orderRepository) {
        this.orderProducer = orderProducer;
        this.orderRepository = orderRepository;
    }


    public void createOrder(String message) {
        Map<String, Integer> items = new HashMap<>();
        items.put("item1", 1);
        Order orderSaved = orderRepository.save(new Order(message, items));
        System.out.println("Order to send");
        orderProducer.send(orderSaved.getId().toString() ,new OrderKafka(orderSaved.getName(), orderSaved.getItems()));
    }
}
