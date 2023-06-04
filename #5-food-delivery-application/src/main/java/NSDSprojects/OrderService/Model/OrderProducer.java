package NSDSprojects.OrderService.Model;

import NSDSprojects.Common.Kafka.OrderKafka;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class OrderProducer {

    private final Logger logger = LoggerFactory.getLogger(OrderProducer.class);
    private final String topic = "order";

    @Autowired
    private KafkaTemplate<String, OrderKafka> kafkaTemplate;


    public void send(String key, OrderKafka orderKafka) {
        logger.debug("OrderProducer is sending an order by: " + orderKafka.getName());
        kafkaTemplate.send(topic, key, orderKafka);
    }
}
