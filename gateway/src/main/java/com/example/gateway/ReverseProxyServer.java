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
        // Create a new Jetty server instance
        Server server = new Server(8080); // You can specify the desired port number

        // Create a ServletContextHandler
        ServletContextHandler contextHandler = new ServletContextHandler(ServletContextHandler.SESSIONS);
        contextHandler.setContextPath("/"); // Set the root context path

        RouteDefinitionsConfig config = loadConfig();
        StrategyFactory factory = new StrategyFactory();
        for (RouteConfig routeConfig : config.getDefinitions()) {
            Route route = new Route(routeConfig.getName(), routeConfig.getListenPath(),
                                    factory.getStrategy(routeConfig.getStrategy()), routeConfig.getTargets());
            // Add servlets or other handlers to the contextHandler
            contextHandler.addServlet(new ServletHolder(new CustomProxyServlet(route)), route.getListenPath());
        }


        // Set the contextHandler as the handler for the server
        server.setHandler(contextHandler);

        // Start the server
        server.start();

        // Optionally, you can wait for the server to finish (e.g., when testing)
        server.join();
    }

    private static RouteDefinitionsConfig loadConfig() {
        Yaml yaml = new Yaml();
        RouteDefinitionsConfig config;
        try (InputStream inputStream = ReverseProxyServer.class.getResourceAsStream("/route_definitions.yml")) {
            config = yaml.loadAs(inputStream, RouteDefinitionsConfig.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return config;
    }
}