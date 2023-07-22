package com.example.gateway;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController()
@RequestMapping("/api/v1")
public class Controller {
    private String name;
    @GetMapping("/ping")
    public String ping() {
        return "pong";
    }
}
