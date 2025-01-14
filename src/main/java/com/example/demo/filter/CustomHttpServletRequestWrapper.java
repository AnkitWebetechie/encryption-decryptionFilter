package com.example.demo.filter;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;

public class CustomHttpServletRequestWrapper extends HttpServletRequestWrapper {

    public CustomHttpServletRequestWrapper(HttpServletRequest request) {
        super(request);
    }

    @Override
    public String getParameter(String name) {
        // Example: Modify a specific request parameter
        if ("exampleParam".equals(name)) {
            return "ModifiedValue";
        }
        return super.getParameter(name);
    }

    @Override
    public String getContentType() {
        return "application/json";
    }

    // Add more overrides as needed
}
