package com.marketflow.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/metrics")
public class MetricsController {

    @GetMapping
    public ResponseEntity<String> getMetrics() {
        return ResponseEntity.ok("[]"); // заглушка
    }
}
