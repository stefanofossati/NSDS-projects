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

    private final ShippingRepository shippingRepository;

    @Autowired
    public ShippingConsumer(ShippingRepository shippingRepository) {
        this.shippingRepository = shippingRepository;
    }

    @KafkaListener(topics = "${spring.kafka.topic1}", containerFactory = "kafkaListenerContainerFactoryOrder")
    public void consumeOrderMessage(OrderKafka message) {
        System.out.println("ShippingService: " + message);
        // facciamo cose
    }

    /**
     * Leggiamo e mettiamo nel DB
     * @param user
     */
    @Transactional
    @KafkaListener(topics = "${spring.kafka.topic2}", containerFactory = "kafkaListenerContainerFactoryUser")
    public void consumeUserMessage(UserKafka user, Acknowledgment acknowledgment) {
        System.out.println("ShippingService: " + user);
        shippingRepository.save(new User(user.getName(), user.getAddress()));
        acknowledgment.acknowledge();
    }
}
