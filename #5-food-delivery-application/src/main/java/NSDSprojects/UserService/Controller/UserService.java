package NSDSprojects.UserService.Controller;

import NSDSprojects.Common.User;
import NSDSprojects.UserService.Model.UserOutbox;
import NSDSprojects.UserService.Reopsitory.UserOutboxRepository;
import NSDSprojects.UserService.Reopsitory.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    private Logger logger = LoggerFactory.getLogger(UserService.class);
    private final UserProducer userProducer;

    private final UserRepository userRepository;

    private final UserOutboxRepository userOutboxRepository;

    @Autowired
    public UserService(UserProducer userProducer, UserRepository userRepository, UserOutboxRepository userOutboxRepository) {
        this.userProducer = userProducer;
        this.userRepository = userRepository;
        this.userOutboxRepository = userOutboxRepository;
    }

    @Transactional
    public void createUser(User user) {
        User userSaved = userRepository.save(user);
        logger.debug("User to send: " + user.getName());
        UserOutbox userOutbox = new UserOutbox(userSaved.getId(), userSaved.getName(), userSaved.getAddress());
        userOutboxRepository.save(userOutbox);
        logger.debug("User saved in outbox");
        //userProducer.send(userSaved.getId().toString(), new UserKafka(userSaved.getName(), userSaved.getAddress()));
    }

    public boolean doesUserExist(String name) {
        return userRepository.existsByName(name);
    }
}
