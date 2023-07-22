package com.example.gateway.service.strategy;

import java.util.List;

public class DefaultStrategy implements Strategy {
    @Override
    public String elect(List<String> targets) {
        if(targets.isEmpty()) {
            return "";
        }
        return targets.get(0);
    }
}
