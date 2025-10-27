package com.example.controller;
import com.example.model.User;

import java.security.Principal;

import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.dto.UserRegistrationDto;
import com.example.dto.UserResponseDto;
import com.example.service.UserService;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;
    private final ModelMapper modelMapper;

    public UserController(UserService userService, ModelMapper modelMapper) {
        this.userService = userService;
        this.modelMapper = modelMapper;
}
@PostMapping("/register")
public ResponseEntity<UserResponseDto> registerUser(@RequestBody UserRegistrationDto userRegistrationDto){
    User user = userService.registerNewUser(userRegistrationDto);
 UserResponseDto responseDto = modelMapper.map(user,UserResponseDto.class);
 return new ResponseEntity<>(responseDto,HttpStatus.OK);
}
@GetMapping("/authenticated")
public ResponseEntity<String> getAuthenticatedUser(Principal principal){
    return ResponseEntity.ok("Hello"+principal.getName()+"successfully accessed a protected resource using jwt");
    
}
}
