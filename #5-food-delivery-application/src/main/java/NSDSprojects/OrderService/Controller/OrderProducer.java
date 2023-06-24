package NSDSprojects.OrderService.Controller;

import NSDSprojects.Common.Kafka.OrderKafka;
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
    private KafkaTemplate<String, OrderKafka> kafkaTemplate;


    public void send(String key, OrderKafka orderKafka) {
        logger.debug("OrderProducer is sending an order by: " + orderKafka.getName() + " with key " + key);
        kafkaTemplate.send(topic, key, orderKafka);
    }
}
