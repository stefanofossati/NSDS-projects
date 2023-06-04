package NSDSprojects.ShippingService.Model;

import NSDSprojects.Common.Kafka.OrderKafka;
import NSDSprojects.Common.Kafka.UserKafka;
import NSDSprojects.Common.Order;
import NSDSprojects.Common.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;


@Component
@EnableJpaRepositories
public class ShippingConsumer {

    private final UserRepository userRepository;
    private final OrderRepository orderRepository;

    @Autowired
    public ShippingConsumer(UserRepository userRepository, OrderRepository orderRepository) {
        this.userRepository = userRepository;
        this.orderRepository = orderRepository;
    }

    @Transactional
    @KafkaListener(topics = "${spring.kafka.topic1}", containerFactory = "kafkaListenerContainerFactoryOrder")
    public void consumeOrderMessage(OrderKafka order, Acknowledgment acknowledgment) {
        System.out.println("ShippingService received a new order: " + order.getItems().toString());
        orderRepository.save(new Order(order.getName(), order.getItems()));
        acknowledgment.acknowledge();
    }

    /**
     * Leggiamo e mettiamo nel DB
     * @param user
     */
    @Transactional
    @KafkaListener(topics = "${spring.kafka.topic2}", containerFactory = "kafkaListenerContainerFactoryUser")
    public void consumeUserMessage(UserKafka user, Acknowledgment acknowledgment) {
        System.out.println("ShippingService received a new user: " + user);
        userRepository.save(new User(user.getName(), user.getAddress()));
        acknowledgment.acknowledge();
    }
}
