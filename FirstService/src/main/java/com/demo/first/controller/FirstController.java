package com.demo.first.controller;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/first")
public class FirstController {
    @GetMapping("/message")
    public String message() {
        return "Hello First Service";
    }
}
