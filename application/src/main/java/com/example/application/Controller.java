package com.example.application;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController()
@RequestMapping("/api/v1")
public class Controller {
    @GetMapping("/ping")
    public String ping() {
        return "pong";
    }

    @PostMapping("/reflect")
    public Map<String,Object> reflect(@RequestBody Map<String, Object> body) {
        return body;
    }
}
