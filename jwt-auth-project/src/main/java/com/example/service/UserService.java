package com.example.service;
import java.util.Collections;

import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.dto.UserRegistrationDto;
import com.example.model.User;
import com.example.repository.UserRepository;

@Service
public class UserService implements UserDetailsService { 
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder; 
    private final ModelMapper modelMapper; 

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.modelMapper = modelMapper;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 1. Fetch User entity from the database
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
        
        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(), // This must be the BCRYPTED password hash
                Collections.emptyList() 
        );
    }

    public User registerNewUser(UserRegistrationDto registrationDto) {
        if (userRepository.findByUsername(registrationDto.getUsername()).isPresent()) {
            throw new RuntimeException("Username already exists!"); 
        }

        User newUser = modelMapper.map(registrationDto, User.class);
        
        newUser.setPassword(passwordEncoder.encode(registrationDto.getPassword()));
        
        return userRepository.save(newUser);
    }
}
