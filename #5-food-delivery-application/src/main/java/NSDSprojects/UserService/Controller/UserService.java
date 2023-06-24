package NSDSprojects.UserService.Controller;

import NSDSprojects.Common.User;
import NSDSprojects.Common.UserEntity;
import NSDSprojects.UserService.Model.UserOutbox;
import NSDSprojects.UserService.Repository.UserOutboxRepository;
import NSDSprojects.UserService.Repository.UserRepository;
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
        UserEntity userSaved = userRepository.save(new UserEntity(user.getName(), user.getAddress()));
        logger.debug("User to send: " + user.getName());
        UserOutbox userOutbox = new UserOutbox(userSaved.getId(), userSaved.getName(), userSaved.getAddress());
        userOutboxRepository.save(userOutbox);
        logger.debug("User saved in outbox");
    }

    public boolean doesUserExist(String name) {
        return userRepository.existsByName(name);
    }
}
