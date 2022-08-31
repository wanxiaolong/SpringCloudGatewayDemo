package com.demo.second.controller;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/second")
public class SecondController {
    @GetMapping("/message")
    public String message() {
        return "Hello Second Service";
    }
}
