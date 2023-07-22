package com.example.gateway.service.strategy;

import java.util.List;

public class NOPStrategy implements Strategy {
    @Override
    public String elect(List<String> targets) {
        return targets.get(0);
    }
}
