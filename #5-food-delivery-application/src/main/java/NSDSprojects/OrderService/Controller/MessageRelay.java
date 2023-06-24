package NSDSprojects.OrderService.Controller;

import NSDSprojects.Common.Kafka.OrderKafka;
import NSDSprojects.OrderService.Model.OrderOutbox;
import NSDSprojects.OrderService.Repository.OrderOutboxRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Configuration
@EnableScheduling
public class MessageRelay {
    private final Logger logger = LoggerFactory.getLogger(MessageRelay.class);
    private final OrderProducer orderProducer;
    private final OrderOutboxRepository orderOutboxRepository;

    @Autowired
    public MessageRelay(OrderProducer orderProducer, OrderOutboxRepository orderOutboxRepository) {
        this.orderProducer = orderProducer;
        this.orderOutboxRepository = orderOutboxRepository;
    }


    @Transactional
    @Scheduled(fixedRate = 2000)
    public void relay() {
        List<OrderOutbox> list = orderOutboxRepository.notSent();
        if(list.size() > 0){
            logger.debug("Find " + list.size() + " orders not sent");
            for (OrderOutbox orderOutbox : list) {
                sendOrder(orderOutbox);
            }
        }

    }

    @Transactional
    public void sendOrder(OrderOutbox orderOutbox) {
        orderProducer.send(orderOutbox.getId().toString() ,new OrderKafka(orderOutbox.getName(), orderOutbox.getItems()));
        orderOutboxRepository.deleteById(orderOutbox.getId());
        logger.debug("Order sent " + orderOutbox.getId().toString() + " " + orderOutbox.getName());
    }
}
