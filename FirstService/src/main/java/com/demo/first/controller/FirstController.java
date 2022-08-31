package com.demo.first.controller;

import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/first")
public class FirstController {
    @GetMapping("/message")
    public String message(HttpServletRequest request) {
        return "Hello First Service";
    }
}
