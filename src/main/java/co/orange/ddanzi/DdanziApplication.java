package co.orange.ddanzi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;


@EnableJpaAuditing
@SpringBootApplication
public class DdanziApplication {

    public static void main(String[] args) {
        SpringApplication.run(DdanziApplication.class, args);
    }

}
