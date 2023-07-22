package com.example.gateway;

import com.example.gateway.service.ReverseProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController()
@RequestMapping("/api/v1")
public class Controller {
    @Autowired
    ReverseProxy reverseProxy;

    @GetMapping("/*")
    public String handle (HttpServletRequest request) {
        String requestPath = request.getRequestURI();
        return reverseProxy.handle(requestPath);
    }
}
