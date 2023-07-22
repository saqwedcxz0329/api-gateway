package com.example.gateway.config;

import lombok.Data;

import java.util.List;

@Data
public class Route {
    String name;
    String listenPath;
    String strategy;
    List<String> targets;

    @Override
    public String toString() {
        return "Route{" +
                "name='" + name + '\'' +
                ", listenPath='" + listenPath + '\'' +
                ", strategy='" + strategy + '\'' +
                ", targets=" + targets +
                '}';
    }
}
