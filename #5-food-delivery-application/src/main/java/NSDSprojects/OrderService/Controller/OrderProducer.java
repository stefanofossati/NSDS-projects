package NSDSprojects.OrderService.Controller;

import NSDSprojects.Common.Order;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class OrderProducer {

    private final Logger logger = LoggerFactory.getLogger(OrderProducer.class);

    @Value(value="${spring.kafka.topic}")
    private String topic;

    @Autowired
    private KafkaTemplate<String, Order> kafkaTemplate;


    public void send(String key, Order order) {
        logger.debug("OrderProducer is sending an order by: " + order.getName() + " with key " + key);
        kafkaTemplate.send(topic, key, order);
    }
}
