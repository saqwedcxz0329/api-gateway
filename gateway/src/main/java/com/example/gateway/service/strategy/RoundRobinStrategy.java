package com.example.gateway.service.strategy;

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

        if(this.current >= targets.size()) {
            this.current = 0;
        }


        return getHost(targets);
    }

    private synchronized String getHost(List<String> targets) {
        String result = targets.get(this.current);
        this.current++;
        return result;
    }
}
