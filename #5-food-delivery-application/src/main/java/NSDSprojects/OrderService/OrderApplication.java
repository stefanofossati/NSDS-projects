package NSDSprojects.OrderService;

import NSDSprojects.Common.OrderEntity;
import NSDSprojects.OrderService.Model.OrderOutbox;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.util.Collections;

@SpringBootApplication(scanBasePackages = {"NSDSprojects.OrderService"})
@EnableJpaRepositories("NSDSprojects.OrderService.Repository")
@EntityScan(basePackageClasses = {OrderEntity.class, OrderOutbox.class})
public class OrderApplication {
	public static void main(String[] args) {
		SpringApplication app = new SpringApplication(OrderApplication.class);
		app.setDefaultProperties(Collections.singletonMap("spring.config.name", "order_application"));
		app.run(args);
	}

}
