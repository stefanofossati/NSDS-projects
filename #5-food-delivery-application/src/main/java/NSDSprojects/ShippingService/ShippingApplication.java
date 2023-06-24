package NSDSprojects.ShippingService;

import NSDSprojects.Common.Order;
import NSDSprojects.Common.User;
import NSDSprojects.Common.UserEntity;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.util.Collections;

@SpringBootApplication(scanBasePackages = {"NSDSprojects.ShippingService"})
@EnableJpaRepositories("NSDSprojects.ShippingService.Repository")
@EntityScan(basePackageClasses = {Order.class})
public class ShippingApplication {
	public static void main(String[] args) {
		SpringApplication app = new SpringApplication(ShippingApplication.class);
		app.setDefaultProperties(Collections.singletonMap("spring.config.name", "shipping_application"));
		app.run(args);

	}

}
