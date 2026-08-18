package com.test.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @GetMapping("/")
    public String sayHello() {
        return new String("Hello Admin");
    }

    @PostMapping("/")
    public String createSomething() {
        return new String("Something created");
    }

}
