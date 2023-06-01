package NSDSprojects.UserService.Model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping(path = "/create")
    public void createUser(@RequestBody String user) {
        userService.createUser(user);
        System.out.println("User created");
    }
}
