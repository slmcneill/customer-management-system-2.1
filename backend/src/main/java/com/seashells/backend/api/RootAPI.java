package com.seashells.backend.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class RootAPI {
    @GetMapping
    public String root() {
        return "Customer Management API is up and running.";
    }
}
