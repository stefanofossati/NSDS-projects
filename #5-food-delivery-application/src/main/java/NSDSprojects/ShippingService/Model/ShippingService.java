package NSDSprojects.ShippingService.Model;

import NSDSprojects.Common.Kafka.OrderState;
import NSDSprojects.Common.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ShippingService {
    private final ShippingConsumer shippingConsumer;
    private final OrderRepository orderRepository;

    @Autowired
    public ShippingService(ShippingConsumer shippingConsumer, OrderRepository orderRepository){
        this.shippingConsumer = shippingConsumer;
        this.orderRepository = orderRepository;
    }

    public OrderState getOrderStatus(Long orderId) {
        Optional<Order> order = orderRepository.findById(orderId);
        return order.map(Order::getOrderState).orElse(null);
    }

    public boolean updateOrderStatus(Long orderId, OrderState delivered) {
        Optional<Order> optOrder = orderRepository.findById(orderId);
        if(optOrder.isPresent()) {
            Order order = optOrder.get();
            order.setOrderState(delivered);
            orderRepository.save(order);
            return true;
        } else return false;
    }
}
