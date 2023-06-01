package NSDSprojects.ShippingService.Model;

import NSDSprojects.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;


@Component
@EnableJpaRepositories
public class ShippingConsumer {

    private final ShippingRepository shippingRepository;

    @Autowired
    public ShippingConsumer(ShippingRepository shippingRepository) {
        this.shippingRepository = shippingRepository;
    }

    @KafkaListener(topics = "${spring.kafka.topic2}")
    public void consumeOrderMessage(String message) {
        System.out.println("ShippingService: " + message);
        // facciamo cose
    }

    /**
     * Leggiamo e mettiamo nel DB
     * @param message
     */

    @KafkaListener(topics = "${spring.kafka.topic1}")
    public void consumeUserMessage(String message, Acknowledgment acknowledgment) {
        System.out.println("ShippingService: " + message);
        shippingRepository.save(new User(message));
        acknowledgment.acknowledge();
    }
}
