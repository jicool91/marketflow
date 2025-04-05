package com.marketflow.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/strategy")
public class StrategyController {

    @GetMapping
    public ResponseEntity<String> getStrategy() {
        return ResponseEntity.ok("{ \"strategy\": \"default\" }");
    }
}
