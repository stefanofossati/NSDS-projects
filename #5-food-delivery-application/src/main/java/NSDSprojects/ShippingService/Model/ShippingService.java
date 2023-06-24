package NSDSprojects.ShippingService.Model;

import NSDSprojects.Common.Kafka.OrderState;
import NSDSprojects.Common.Order;
import NSDSprojects.Common.User;
import NSDSprojects.OrderService.Model.OrderController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Service
public class ShippingService {
    private final ShippingConsumer shippingConsumer;
    private final OrderRepository orderRepository;

    private final UserRepository userRepository;

    private Logger logger = LoggerFactory.getLogger(ShippingService.class);

    @Autowired
    public ShippingService(ShippingConsumer shippingConsumer, OrderRepository orderRepository, UserRepository userRepository){
        this.shippingConsumer = shippingConsumer;
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
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

    public ArrayList<Order> getOrdersStatus(String username) {
        ArrayList<Order> orders = orderRepository.findByUsername(username);
        if(orders.isEmpty()) {
            return null;
        } else {
            return orders;
        }
    }

    public ArrayList<OrderDelivery> getOrderToDeliver() {
        ArrayList<OrderDelivery> orders = new ArrayList<>();
        ArrayList<Order> allOrders = orderRepository.findAll();
        for(Order order : allOrders) {
            if(order.getOrderState() != OrderState.DELIVERED){
                User user = userRepository.findByName(order.getName());
                if(user != null){  // il sistema non fa check sul fatto che ci sono ordini fatti da utenti non registrati
                    orders.add(new OrderDelivery(order.getId(), order.getName(), user.getAddress(), order.getItems()));
                }else{
                    orders.add(new OrderDelivery(order.getId(), order.getName(), "No address", order.getItems()));
                }
            }
        }
        return orders;
    }
}
