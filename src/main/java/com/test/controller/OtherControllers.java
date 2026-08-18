package com.test.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class OtherControllers {

    @GetMapping("/hello")
    public String sayHello() {
        return new String("Hello User");
    }
}
