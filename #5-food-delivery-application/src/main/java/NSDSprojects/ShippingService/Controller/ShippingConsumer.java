package NSDSprojects.ShippingService.Controller;

import NSDSprojects.Common.Order;
import NSDSprojects.Common.OrderEntity;
import NSDSprojects.Common.User;
import NSDSprojects.Common.UserEntity;
import NSDSprojects.ShippingService.Repository.OrderRepository;
import NSDSprojects.ShippingService.Repository.UserRepository;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
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
    private boolean firstOrderConsumedAfterRecovery;
    private boolean firstUserConsumedAfterRecovery;

    @Autowired
    private ApplicationContext applicationContext;


    @Autowired
    public ShippingConsumer(UserRepository userRepository, OrderRepository orderRepository) {
        this.userRepository = userRepository;
        this.orderRepository = orderRepository;
        this.firstOrderConsumedAfterRecovery = true;
        this.firstUserConsumedAfterRecovery = true;
    }

    @Transactional
    @KafkaListener(topics = "${spring.kafka.topic1}", containerFactory = "kafkaListenerContainerFactoryOrder")
    public void consumeOrderMessage(ConsumerRecord<String, Order> record, Acknowledgment acknowledgment) {
        String key = record.key();
        Order order = record.value();
        System.out.println("ShippingService received a new order: " + key);
        if(firstOrderConsumedAfterRecovery) {
            // check whether the first order found is a duplicate due to a previous crash of the service
            boolean isDuplicate = orderRepository.findById(Long.parseLong(key)).isPresent();
            if(!isDuplicate) {
                // if it's not a duplicate, save it
                orderRepository.save(new OrderEntity(Long.parseLong(key), order.getName(), order.getItems()));
            } else {
                System.out.println("Found a duplicate order having key: " + key);
            }
            firstOrderConsumedAfterRecovery = false;
        } else {
            orderRepository.save(new OrderEntity(Long.parseLong(key), order.getName(), order.getItems()));
        }
        acknowledgment.acknowledge();
    }

    @Transactional
    @KafkaListener(topics = "${spring.kafka.topic2}", containerFactory = "kafkaListenerContainerFactoryUser")
    public void consumeUserMessage(ConsumerRecord<String, User> record, Acknowledgment acknowledgment) {
        String key = record.key();
        User user = record.value();
        System.out.println("ShippingService received a new user: " + user);
        if(firstUserConsumedAfterRecovery) {
            // check whether the first user found is a duplicate due to a previous crash of the service
            boolean isDuplicate = userRepository.findById(Long.parseLong(key)).isPresent();
            if(!isDuplicate) {
                // if it's not a duplicate, save it
                userRepository.save(new UserEntity(Long.parseLong(key), user.getName(), user.getAddress()));
            } else {
                System.out.println("Found a duplicate user having key: " + key);
            }
            firstUserConsumedAfterRecovery = false;
        } else {
            userRepository.save(new UserEntity(Long.parseLong(key), user.getName(), user.getAddress()));
        }
        acknowledgment.acknowledge();
    }
}
