package com.hyperwallet.todoservice.application;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * The entry point for the app.
 */
@SpringBootApplication
@ComponentScan(basePackages = "com.hyperwallet.todoservice")
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}