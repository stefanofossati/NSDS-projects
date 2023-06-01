package NSDSprojects.ShippingService;

import NSDSprojects.User;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

import java.util.Collections;

@SpringBootApplication()
@EntityScan(basePackageClasses = {User.class})
public class ShippingApplication {
	public static void main(String[] args) {
		SpringApplication app = new SpringApplication(ShippingApplication.class);
		app.setDefaultProperties(Collections.singletonMap("spring.config.name", "shipping_application"));
		app.run(args);

	}

}
