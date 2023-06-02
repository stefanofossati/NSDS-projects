package NSDSprojects.UserService.Model;

import NSDSprojects.Common.Kafka.UserKafka;
import NSDSprojects.Common.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserProducer userProducer;

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserProducer userProducer, UserRepository userRepository) {
        this.userProducer = userProducer;
        this.userRepository = userRepository;
    }

    public void createUser(User user) {
        User userSaved = userRepository.save(user);
        System.out.println("User to send: " + user.getName());
        userProducer.send(userSaved.getId().toString(), new UserKafka(userSaved.getName(), userSaved.getAddress()));
    }

    public boolean doesUserExist(String name) {
        return userRepository.existsByName(name);
    }
}
