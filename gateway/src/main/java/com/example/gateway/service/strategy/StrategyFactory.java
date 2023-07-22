package com.example.gateway.service.strategy;

import org.springframework.stereotype.Component;

@Component
public class StrategyFactory {
    public Strategy getStrategy(String name) {
        if (name.equals("round-robin")) {
            return new RoundRobinStrategy();
        } else {
            return new DefaultStrategy();
        }
    }
}
