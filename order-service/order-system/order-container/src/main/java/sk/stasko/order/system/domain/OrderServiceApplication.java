package sk.stasko.order.system.domain;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories(basePackages = "sk.stasko.order.system.dataaccess")
@EntityScan(basePackages = "sk.stasko.order.system.dataaccess")
@SpringBootApplication(scanBasePackages = "sk.stasko.order.system")
public class OrderServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(OrderServiceApplication.class, args);
    }
}
