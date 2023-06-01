package NSDSprojects.OrderService.Model;

import NSDSprojects.Common.Kafka.OrderKafka;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class OrderProducer {
    private final String topic = "order";

    @Autowired
    private KafkaTemplate<String, OrderKafka> kafkaTemplate;


    public void send(String key, OrderKafka orderKafka) {
        System.out.println("OrderProducer: " + key);
        kafkaTemplate.send(topic, key, orderKafka);
    }
}
