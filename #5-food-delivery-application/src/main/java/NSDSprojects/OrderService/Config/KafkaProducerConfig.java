package NSDSprojects.OrderService.Config;

import NSDSprojects.Common.Kafka.OrderKafka;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaProducerConfig {

    //TODO probabilmente possiamo anche mettere questo in application.properties

    @Value(value = "${spring.kafka.bootstrap-servers}")
    private String serverAddress;

    @Value(value = "${spring.kafka.producer.key-serializer}")
    private String keySerializer;

    @Value(value = "${spring.kafka.producer.value-serializer}")
    private String valueSerializer;

    @Bean
    public ProducerFactory<String, OrderKafka> producerFactory() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,serverAddress);
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, keySerializer);
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, valueSerializer);
        configProps.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, String.valueOf(true));
        return new DefaultKafkaProducerFactory<>(configProps);
    }

    @Bean
    public KafkaTemplate<String, OrderKafka> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }
}
