package NSDSprojects.OrderService.Model;

import NSDSprojects.ShippingService.Model.ShippingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class OrderProducer {
    private final String topic = "order";

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    private OrderRepository orderRepository;


    public void send(String message) {
        System.out.println("OrderProducer: " + message);
        kafkaTemplate.send(topic, message);
    }
}
