package com.example.gateway.service.strategy;

import java.util.List;

public interface Strategy {
    String elect(List<String> targets);
}
