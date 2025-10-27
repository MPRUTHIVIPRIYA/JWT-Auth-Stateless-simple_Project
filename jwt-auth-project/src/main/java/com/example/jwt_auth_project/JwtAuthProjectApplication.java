package com.example.jwt_auth_project;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// Ensure the list of packages includes the 'repository' package
@SpringBootApplication
public class JwtAuthProjectApplication {

    public static void main(String[] args) {
        SpringApplication.run(JwtAuthProjectApplication.class, args);
    }
}