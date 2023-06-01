package NSDSprojects.UserService.Model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class UserProducer {
    private final String topic = "user";

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    public void send(String message) {
        System.out.println("userProducer: " + message);
        kafkaTemplate.send(topic, message);
    }
}
