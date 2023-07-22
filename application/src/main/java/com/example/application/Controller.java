package com.example.application;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController()
@RequestMapping("/api/v1")
public class Controller {
    @Value("${spring.application.name}")
    private String name;
    @GetMapping("/ping")
    public String ping() {
        return "pong";
    }

    @PostMapping("/reflect")
    public ResponseEntity<Map<String,Object>> reflect(@RequestBody Map<String, Object> body) {
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set("X-Application-Name", name);

        return ResponseEntity.ok()
                .headers(responseHeaders)
                .body(body);
    }
}
