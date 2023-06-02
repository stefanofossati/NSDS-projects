package NSDSprojects.UserService.Model;

import NSDSprojects.Common.Kafka.UserKafka;
import NSDSprojects.Common.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class UserProducer {
    private final String topic = "user";

    @Autowired
    private KafkaTemplate<String, UserKafka> kafkaTemplate;

    public void send(String key, UserKafka user) {
        System.out.println("userProducer: " + user);
        kafkaTemplate.send(topic, key, user);
    }
}
