package com.example.gateway.servlet;

import com.example.gateway.model.Route;
import org.eclipse.jetty.proxy.ProxyServlet;

import javax.servlet.http.HttpServletRequest;

public class CustomProxyServlet extends ProxyServlet {
    private final Route route;

    public CustomProxyServlet(Route route) {
        this.route = route;
    }

    @Override
    protected String rewriteTarget(HttpServletRequest request) {
        String targetURI = request.getRequestURI();
        String query = request.getQueryString();
        if (query != null) {
            targetURI = targetURI + "?" + query;
        }

        // NOTE: get targets from db/service discovery
        String host = route.getStrategy().elect(route.getTargets());
        if(host.isEmpty()) {
            return null;
        }
        return host + targetURI;
    }
}
