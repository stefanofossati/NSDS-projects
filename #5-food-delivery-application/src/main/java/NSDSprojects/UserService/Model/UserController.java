package NSDSprojects.UserService.Model;

import NSDSprojects.Common.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

    private Logger logger = LoggerFactory.getLogger(UserController.class);
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping(path = "/create")
    public ResponseEntity<String> createUser(@RequestBody UserRequest user) {
        if(userService.doesUserExist(user.getName())) {
            // Return a response with a 400 Bad Request status code
            return ResponseEntity.badRequest().body("User already existing!");
        } else {
            userService.createUser(new User(user.getName(), user.getAddress()));
            logger.debug("User created");
            return ResponseEntity.ok("User created");
        }
    }
}
