package com.marketflow.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/report")
public class ReportController {

    @GetMapping
    public ResponseEntity<String> getReport() {
        return ResponseEntity.ok("{ \"pdf\": \"report.pdf\" }");
    }
}
