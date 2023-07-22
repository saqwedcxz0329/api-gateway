package com.example.gateway;

import com.example.gateway.config.RouteDefinitions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController()
@RequestMapping("/api/v1")
public class Controller {
    @Autowired
    RouteDefinitions routeDefinitions;
    @GetMapping("/ping")
    public String ping() {
        System.out.println(routeDefinitions.toString());
        return "pong";
    }
}
