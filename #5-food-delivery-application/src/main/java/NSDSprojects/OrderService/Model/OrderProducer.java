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


    public void send(OrderKafka orderKafka) {
        System.out.println("OrderProducer is sending an order by: " + orderKafka.getName());
        kafkaTemplate.send(topic, orderKafka);
    }
}
