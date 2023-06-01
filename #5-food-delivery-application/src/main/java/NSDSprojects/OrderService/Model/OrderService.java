package NSDSprojects.OrderService.Model;

import NSDSprojects.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
        orderRepository.save(new Order());
        System.out.println("Order to send");
        orderProducer.send(message);
    }
}
