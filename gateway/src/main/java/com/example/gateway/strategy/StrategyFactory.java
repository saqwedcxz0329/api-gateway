package com.example.gateway.strategy;

public class StrategyFactory {
    public Strategy getStrategy(String name) {
        if (name.equals("round-robin")) {
            return new RoundRobinStrategy();
        } else {
            return new DefaultStrategy();
        }
    }
}
