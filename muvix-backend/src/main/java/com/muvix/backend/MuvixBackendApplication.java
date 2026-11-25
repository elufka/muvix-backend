package com.muvix.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class MuvixBackendApplication {
    public static void main(String[] args) {
        SpringApplication.run(MuvixBackendApplication.class, args);
    }
}
