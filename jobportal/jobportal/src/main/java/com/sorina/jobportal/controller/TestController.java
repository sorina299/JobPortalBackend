package com.sorina.jobportal.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class TestController {

    @GetMapping("/test")
    public Map<String, String> testEndpoint() {
        Map<String, String> response = new HashMap<>();
        response.put("message", "Hello from the backend!");
        return response;
    }
}

