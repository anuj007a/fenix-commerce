package com.fenix.commerce;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class FenixCommerceApplication {

    public static void main(String[] args) {
        SpringApplication.run(FenixCommerceApplication.class, args);
    }

}
