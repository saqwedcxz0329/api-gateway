package com.example.gateway.service;

import com.example.gateway.config.RouteConfig;
import com.example.gateway.config.RouteDefinitionsConfig;
import com.example.gateway.model.Route;
import com.example.gateway.service.strategy.StrategyFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ReverseProxy {
    List<Route> routes;

    @Autowired
    public ReverseProxy(RouteDefinitionsConfig routeDefinitions, StrategyFactory factory) {
        this.routes = new ArrayList<>();
        for (RouteConfig routeConfig : routeDefinitions.getDefinitions()) {
            routes.add(new Route(routeConfig.getName(), routeConfig.getListenPath(),
                                 factory.getStrategy(routeConfig.getStrategy()), routeConfig.getTargets()));
        }
    }

    public String handle(String path) {
        Route matchRoute = this.match(path);
        if (matchRoute == null) {
            // FIXME: bad request
            return "";
        }
        // FIXME: if elect result is empty string => Internal Server error
        return matchRoute.getStrategy().elect(matchRoute.getTargets());
    }

    private Route match(String path) {
        for (Route route : this.routes) {
            if (route.getListenPath().equals(path)) {
                return route;
            }
        }

        return null;
    }
}
