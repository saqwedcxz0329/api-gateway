package com.example.gateway;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.eclipse.jetty.server.Dispatcher;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.ErrorHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class CustomJsonErrorHandler extends ErrorHandler {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void handle(String target, Request baseRequest, HttpServletRequest request,
                       HttpServletResponse response) throws IOException {
        // Set the response content type to JSON
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        // Get the exception attribute from the request (if available)
        String message = (String) request.getAttribute(Dispatcher.ERROR_MESSAGE);
        if (message == null) {
            message = baseRequest.getResponse().getReason();
        }


        // Create a JSON representation of the error response
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("status", response.getStatus());
        errorResponse.put("message", message);

        // Write the JSON response to the output stream
        response.getWriter().println(objectMapper.writeValueAsString(errorResponse));

        // Mark the response as handled
        baseRequest.setHandled(true);
    }
}