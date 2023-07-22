package com.example.gateway.strategy;

import java.util.List;

public interface Strategy {
    String elect(List<String> targets);
}
