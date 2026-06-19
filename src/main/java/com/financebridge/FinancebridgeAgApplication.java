package com.financebridge;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class FinancebridgeAgApplication {

    public static void main(String[] args) {
        SpringApplication.run(FinancebridgeAgApplication.class, args);
    }

}
