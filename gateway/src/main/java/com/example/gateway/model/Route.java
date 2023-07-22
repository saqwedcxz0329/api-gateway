package com.example.gateway.model;

import com.example.gateway.strategy.Strategy;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class Route {
    String name;
    String listenPath;
    Strategy strategy;
    List<String> targets;
}
