package com.example.gateway.strategy;

import java.util.List;

public class RoundRobinStrategy implements Strategy {
    int current;

    public RoundRobinStrategy() {
        this.current = 0;
    }

    @Override
    public String elect(List<String> targets) {
        if(targets.isEmpty()) {
            return "";
        }
        if(targets.size() == 1) {
            return targets.get(0);
        }
        return getHost(targets);
    }

    private synchronized String getHost(List<String> targets) {
        if(this.current >= targets.size()) {
            this.current = 0;
        }

        return targets.get(this.current++);
    }
}
