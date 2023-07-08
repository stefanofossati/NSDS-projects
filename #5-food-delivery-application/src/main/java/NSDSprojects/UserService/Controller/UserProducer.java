package NSDSprojects.UserService.Controller;

import NSDSprojects.Common.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class UserProducer {

    private Logger logger = LoggerFactory.getLogger(UserProducer.class);
    private final String topic = "user";

    @Autowired
    private KafkaTemplate<String, User> kafkaTemplate;

    public void send(String key, User user) {
        logger.debug("userProducer: " + user);
        kafkaTemplate.send(topic, key, user);
    }
}
