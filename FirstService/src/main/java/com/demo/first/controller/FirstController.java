package com.demo.first.controller;

import com.demo.first.dto.Log;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/first")
public class FirstController {
    @GetMapping("/message")
    public String message() {
        return "Hello First Service";
    }

    @GetMapping("/{id}")
    public String get(@PathVariable("id") String id) {
        return "GET: " + id;
    }

    @PostMapping("/{id}")
    public String post(@PathVariable("id") String id) {
        return "POST: " + id;
    }

    @PutMapping("/{id}")
    public String put(@PathVariable("id") String id) {
        return "PUT: " + id;
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") String id) {
        return "DELETE: " + id;
    }

    @GetMapping("/getLog")
    public Log logObject() {
        return new Log(1, "Log ID", "Log Message");
    }
}
