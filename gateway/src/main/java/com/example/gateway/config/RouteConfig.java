package com.example.gateway.config;

import lombok.Data;

import java.util.List;

@Data
public class RouteConfig {
    String name;
    String listenPath;
    String strategy;
    List<String> targets;
}
