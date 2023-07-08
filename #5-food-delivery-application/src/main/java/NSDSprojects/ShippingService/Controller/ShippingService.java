package NSDSprojects.ShippingService.Controller;

import NSDSprojects.Common.OrderState;
import NSDSprojects.Common.OrderEntity;
import NSDSprojects.Common.UserEntity;
import NSDSprojects.ShippingService.Model.OrderDelivery;
import NSDSprojects.ShippingService.Repository.OrderRepository;
import NSDSprojects.ShippingService.Repository.UserRepository;
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
    public ShippingService( UserRepository userRepository, OrderRepository orderRepository, ShippingConsumer shippingConsumer){
        this.userRepository = userRepository;
        this.orderRepository = orderRepository;
        this.shippingConsumer = shippingConsumer;


    }

    public OrderState getOrderStatus(Long orderId) {
        Optional<OrderEntity> order = orderRepository.findById(orderId);
        return order.map(OrderEntity::getOrderState).orElse(null);
    }

    public boolean updateOrderStatus(Long orderId, OrderState delivered) {
        Optional<OrderEntity> optOrder = orderRepository.findById(orderId);
        if(optOrder.isPresent()) {
            OrderEntity orderEntity = optOrder.get();
            orderEntity.setOrderState(delivered);
            orderRepository.save(orderEntity);
            return true;
        } else return false;
    }

    public ArrayList<OrderEntity> getOrdersStatus(String username) {
        ArrayList<OrderEntity> orderEntities = orderRepository.findByUsername(username);
        if(orderEntities.isEmpty()) {
            return null;
        } else {
            return orderEntities;
        }
    }

    public ArrayList<OrderDelivery> getOrderToDeliver() {
        ArrayList<OrderDelivery> orders = new ArrayList<>();
        ArrayList<OrderEntity> allOrders = orderRepository.findAll();
        for(OrderEntity orderEntity : allOrders) {
            if(orderEntity.getOrderState() != OrderState.DELIVERED){
                UserEntity user = userRepository.findByName(orderEntity.getName());
                if(user != null){  // il sistema non fa check sul fatto che ci sono ordini fatti da utenti non registrati
                    orders.add(new OrderDelivery(orderEntity.getId(), orderEntity.getName(), user.getAddress(), orderEntity.getItems()));
                }else{
                    orders.add(new OrderDelivery(orderEntity.getId(), orderEntity.getName(), "No address", orderEntity.getItems()));
                }
            }
        }
        return orders;
    }
}
