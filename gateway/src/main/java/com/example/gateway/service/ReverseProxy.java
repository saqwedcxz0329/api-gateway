package com.example.gateway.service;

import com.example.gateway.config.RouteConfig;
import com.example.gateway.config.RouteDefinitionsConfig;
import com.example.gateway.model.Route;
import com.example.gateway.service.strategy.NOPStrategy;
import com.example.gateway.service.strategy.RoundRobinStrategy;
import com.example.gateway.service.strategy.Strategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ReverseProxy {
    List<Route> routes;
    RouteDefinitionsConfig routeDefinitions;

    @Autowired
    public ReverseProxy(RouteDefinitionsConfig routeDefinitions) {
        this.routeDefinitions = routeDefinitions;
        this.routes = new ArrayList<>();
        for (RouteConfig routeConfig : this.routeDefinitions.getDefinitions()) {
            Strategy strategy = new NOPStrategy();
            if (routeConfig.getStrategy().equals("round-robin")) {
                strategy = new RoundRobinStrategy();
            }
            routes.add(new Route(routeConfig.getName(), routeConfig.getListenPath(), strategy, routeConfig.getTargets()));
        }
    }

    public String handle(String path) {
        Route matchRoute = this.match(path);
        if (matchRoute == null) {
            return "";
        }
        return matchRoute.getStrategy().elect(matchRoute.getTargets());
    }

    private Route match(String path) {
        for (Route route : this.routes) {
            if(route.getListenPath().equals(path)) {
                return route;
            }
        }

        return null;
    }
}
