package com.marketflow.controller;

import com.marketflow.model.Metric;
import com.marketflow.model.MetricDTO;
import com.marketflow.model.MetricSummary;
import com.marketflow.service.MetricsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/metrics")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Metrics API", description = "Операции с маркетинговыми метриками")
public class MetricsController {

    private final MetricsService metricsService;

    @GetMapping
    @Operation(summary = "Получить все метрики")
    public ResponseEntity<List<MetricDTO>> getAllMetrics() {
        log.info("REST request to get all metrics");
        return ResponseEntity.ok(metricsService.getAllMetrics());
    }

    @GetMapping("/{type}")
    @Operation(summary = "Получить метрики по типу (источнику)")
    public ResponseEntity<List<MetricDTO>> getMetricsByType(@PathVariable String type) {
        log.info("REST request to get metrics by type: {}", type);
        return ResponseEntity.ok(metricsService.getMetricsByType(type));
    }

    @GetMapping("/date-range")
    @Operation(summary = "Получить метрики в диапазоне дат")
    public ResponseEntity<List<MetricDTO>> getMetricsByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        log.info("REST request to get metrics between {} and {}", startDate, endDate);
        return ResponseEntity.ok(metricsService.getMetricsByDateRange(startDate, endDate));
    }

    @GetMapping("/summary")
    @Operation(summary = "Получить сводку по метрикам")
    public ResponseEntity<MetricSummary> getMetricsSummary() {
        log.info("REST request to get metrics summary");
        return ResponseEntity.ok(metricsService.getMetricsSummary());
    }

    @PostMapping
    @Operation(summary = "Сохранить новую метрику")
    public ResponseEntity<MetricDTO> createMetric(@RequestBody Metric metric) {
        log.info("REST request to save Metric: {}", metric);
        return new ResponseEntity<>(metricsService.saveMetric(metric), HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Удалить метрику")
    public ResponseEntity<Void> deleteMetric(@PathVariable Long id) {
        log.info("REST request to delete Metric: {}", id);
        metricsService.deleteMetric(id);
        return ResponseEntity.noContent().build();
    }
}