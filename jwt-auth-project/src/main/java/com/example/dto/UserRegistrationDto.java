package com.example.dto;

public class UserRegistrationDto {
    private String username;
    private String password;

    // 1. CRITICAL: Add the default no-argument constructor
    public UserRegistrationDto() {
        // Required for JSON deserialization by Spring/Jackson
    }

    // 2. Add the all-argument constructor (optional, but good practice)
    public UserRegistrationDto(String username, String password) {
        this.username = username;
        this.password = password;
    }

    // 3. Add Getters and Setters (CRITICAL for Jackson to map fields)
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}