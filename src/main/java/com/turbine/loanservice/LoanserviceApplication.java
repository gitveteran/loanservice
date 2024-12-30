package com.turbine.loanservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.retry.annotation.EnableRetry;

@SpringBootApplication
@EnableRetry
public class LoanserviceApplication {
    public static void main(String[] args) {
        SpringApplication.run(LoanserviceApplication.class, args);
    }
}
