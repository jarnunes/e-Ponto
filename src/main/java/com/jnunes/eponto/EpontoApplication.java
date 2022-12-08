package com.jnunes.eponto;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = {"com.jnunes"})
@EntityScan(basePackages = {"com.jnunes"})
@EnableJpaRepositories(basePackages = {"com.jnunes"})
public class EpontoApplication {

    public static void main(String[] args) {
        SpringApplication.run(EpontoApplication.class, args);
    }

}
