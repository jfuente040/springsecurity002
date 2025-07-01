package com.jfuente040.springsecurity001.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class TestAuthController {


    @GetMapping("/hello")
    public String hello() {
        return "Hello from TestAuthController";
    }

    @GetMapping("/hello-secured")
    public String helloSecured() {
        return "Hello from TestAuthController secured";
    }

    @GetMapping("/others")
    public String others() {
        return "Hello from TestAuthController others";
    }




}
