package NSDSprojects.UserService.Model;

import NSDSprojects.Common.Kafka.UserKafka;
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
public class UserMessageRelay {

    private final Logger logger = LoggerFactory.getLogger(UserMessageRelay.class);

    private final UserProducer userProducer;

    private final UserOutboxRepository userOutboxRepository;

    @Autowired
    public UserMessageRelay(UserProducer userProducer, UserOutboxRepository userOutboxRepository) {
        this.userProducer = userProducer;
        this.userOutboxRepository = userOutboxRepository;
    }


    @Transactional
    @Scheduled(fixedRate = 2000)
    public void relay(){
        List<UserOutbox> list = userOutboxRepository.notSent();
        if(list.size() > 0){
            logger.debug("Find " + list.size() + " users not sent");
            for(UserOutbox userOutbox : list){
                sendUser(userOutbox);
            }
        }
    }

    @Transactional
    public void sendUser(UserOutbox userOutbox){
        userProducer.send(userOutbox.getId().toString(), new UserKafka(userOutbox.getName(), userOutbox.getAddress()));
        userOutboxRepository.deleteById(userOutbox.getId());
        logger.debug("User sent " + userOutbox.getId().toString() + " " + userOutbox.getName());
    }

}
