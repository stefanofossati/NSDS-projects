package NSDSprojects.ShippingService.Config;

import NSDSprojects.Common.Kafka.OrderKafka;
import NSDSprojects.Common.Kafka.UserKafka;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;

@EnableKafka
@Configuration
public class KafkaConsumerConfig {

    private final String serverAddress = "localhost:9092";
    private final String groupId = "shipping";
    private final boolean autoCommit = false;
    private final int autoCommitInterval = 100;
    private final String offsetResetStrategy = "latest";
    @Bean
    KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, OrderKafka>> kafkaListenerContainerFactoryOrder() {
        ConcurrentKafkaListenerContainerFactory<String, OrderKafka> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactoryOrder());
        factory.setConcurrency(1);
        factory.getContainerProperties().setPollTimeout(3000);
        factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL_IMMEDIATE);
        factory.getContainerProperties().setSyncCommits(true);
        return factory;
    }

    @Bean
    KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, UserKafka>> kafkaListenerContainerFactoryUser() {
        ConcurrentKafkaListenerContainerFactory<String, UserKafka> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactoryUser());
        factory.setConcurrency(1);
        factory.getContainerProperties().setPollTimeout(3000);
        factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL_IMMEDIATE);
        factory.getContainerProperties().setSyncCommits(true);
        return factory;
    }


    @Bean
    public ConsumerFactory<String, OrderKafka> consumerFactoryOrder() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, serverAddress);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, String.valueOf(autoCommit));
        props.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, String.valueOf(autoCommitInterval));
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, String.valueOf(offsetResetStrategy));
        return new DefaultKafkaConsumerFactory<>(props, new StringDeserializer(), new JsonDeserializer<>(OrderKafka.class));
    }

    @Bean
    public ConsumerFactory<String, UserKafka> consumerFactoryUser() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, serverAddress);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, String.valueOf(autoCommit));
        props.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, String.valueOf(autoCommitInterval));
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, String.valueOf(offsetResetStrategy));
        return new DefaultKafkaConsumerFactory<>(props, new StringDeserializer(), new JsonDeserializer<>(UserKafka.class));
    }


}