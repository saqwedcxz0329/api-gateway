package com.example.gateway.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import java.util.List;

@Configuration
@ConfigurationProperties(prefix = "")
@PropertySource(value = "classpath:route_definitions.yml", factory = YamlPropertySourceFactory.class)
@Data
public class RouteDefinitionsConfig {
    @NestedConfigurationProperty
    List<RouteConfig> definitions;

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (RouteConfig definition : definitions) {
            sb.append(definition.toString());

        }
        return "RouteDefinitions{" +
                "definitions=" + sb +
                '}';
    }
}
