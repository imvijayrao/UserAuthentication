package com.userauth.userauthenticate.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @PostMapping("/hi")
    public String sayhello(){
        return "Hello";
    }
}
