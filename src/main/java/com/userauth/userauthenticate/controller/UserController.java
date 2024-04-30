package com.userauth.userauthenticate.controller;

import com.userauth.userauthenticate.dtos.LoginRequestDto;
import com.userauth.userauthenticate.dtos.SignupRequestDto;
import com.userauth.userauthenticate.model.Token;
import com.userauth.userauthenticate.model.User;
import com.userauth.userauthenticate.services.UserService;
import jakarta.ws.rs.Path;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController {

    @Autowired
    private UserService userservice;

    @PostMapping("/signup")
    public User signup(@RequestBody SignupRequestDto requestDto){
        return userservice.signup(requestDto.getEmail(), requestDto.getPassword(), requestDto.getName());
    }

    @PostMapping("/login")
    public Token login(@RequestBody LoginRequestDto requestDto){
        return userservice.login(requestDto.getEmail(), requestDto.getPassword());
    }

    @PostMapping("/logout/{token}")
    public ResponseEntity<Void> logout(@PathVariable("token") String token){
        userservice.logout(token);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/validateToken/{token}")
    public boolean validate(@PathVariable("token") String token){
        return userservice.validateToken(token);
    }

    @GetMapping("/validateEmail/{email}")
    public boolean getuseremail(@PathVariable("email") String email){
        return userservice.validateEmail(email);
    }

}
