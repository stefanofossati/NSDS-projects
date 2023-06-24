package NSDSprojects.UserService;

import NSDSprojects.Common.User;
import NSDSprojects.Common.UserEntity;
import NSDSprojects.UserService.Model.UserOutbox;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.util.Collections;

@SpringBootApplication(scanBasePackages = {"NSDSprojects.UserService"})
@EnableJpaRepositories("NSDSprojects.UserService.Repository")
@EntityScan(basePackageClasses = {UserEntity.class, UserOutbox.class})
public class UserApplication {
	public static void main(String[] args) {
		SpringApplication app = new SpringApplication(UserApplication.class);
		app.setDefaultProperties(Collections.singletonMap("spring.config.name", "user_application"));
		app.run(args);
	}

}
