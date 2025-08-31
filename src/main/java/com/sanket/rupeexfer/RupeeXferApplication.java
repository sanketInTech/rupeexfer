package com.sanket.rupeexfer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
public class RupeeXferApplication {
    public static void main(String[] args) {
        SpringApplication.run(RupeeXferApplication.class, args);
    }
}
