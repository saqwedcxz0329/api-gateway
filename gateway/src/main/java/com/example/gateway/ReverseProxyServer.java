package com.example.gateway;

import com.example.gateway.config.RouteConfig;
import com.example.gateway.config.RouteDefinitionsConfig;
import com.example.gateway.model.Route;
import com.example.gateway.strategy.StrategyFactory;
import com.example.gateway.servlet.CustomProxyServlet;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.yaml.snakeyaml.Yaml;

import java.io.IOException;
import java.io.InputStream;

public class ReverseProxyServer {
    public static void main(String[] args) throws Exception {
        Server server = new Server(8080);

        ServletContextHandler contextHandler = new ServletContextHandler(ServletContextHandler.SESSIONS);
        contextHandler.setContextPath("/");

        RouteDefinitionsConfig config = loadConfig();
        StrategyFactory factory = new StrategyFactory();
        for (RouteConfig routeConfig : config.getDefinitions()) {
            Route route = new Route(routeConfig.getName(), routeConfig.getListenPath(),
                                    factory.getStrategy(routeConfig.getStrategy()), routeConfig.getTargets());
            contextHandler.addServlet(new ServletHolder(new CustomProxyServlet(route)), route.getListenPath());
        }


        server.setHandler(contextHandler);

        server.start();

        server.join();
    }

    private static RouteDefinitionsConfig loadConfig() {
        Yaml yaml = new Yaml();
        RouteDefinitionsConfig config;
        try (InputStream inputStream = ReverseProxyServer.class.getResourceAsStream("/route-definitions.yml")) {
            config = yaml.loadAs(inputStream, RouteDefinitionsConfig.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return config;
    }
}