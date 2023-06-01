package NSDSprojects.UserService.Model;

import NSDSprojects.User;
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


    public void createUser(String user) {
        userRepository.save(new User(user));
        System.out.println("User to send: " + user);
        userProducer.send(user);
    }
}
