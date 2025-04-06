package com.marketflow.controller;

import com.marketflow.model.strategy.Strategy;
import com.marketflow.model.strategy.StrategyDTO;
import com.marketflow.model.strategy.StrategyStatus;
import com.marketflow.service.StrategyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/strategy")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Strategy API", description = "Операции со стратегиями маркетинга")
public class StrategyController {

    private final StrategyService strategyService;

    @GetMapping
    @Operation(summary = "Получить все стратегии")
    public ResponseEntity<List<StrategyDTO>> getAllStrategies() {
        log.info("REST request to get all strategies");
        return ResponseEntity.ok(strategyService.getAllStrategies());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получить стратегию по ID")
    public ResponseEntity<StrategyDTO> getStrategyById(@PathVariable Long id) {
        log.info("REST request to get strategy with id: {}", id);
        return strategyService.getStrategyById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/status/{status}")
    @Operation(summary = "Получить стратегии по статусу")
    public ResponseEntity<List<StrategyDTO>> getStrategiesByStatus(@PathVariable StrategyStatus status) {
        log.info("REST request to get strategies with status: {}", status);
        return ResponseEntity.ok(strategyService.getStrategiesByStatus(status));
    }

    @GetMapping("/source/{source}/status/{status}")
    @Operation(summary = "Получить стратегии по источнику и статусу")
    public ResponseEntity<List<StrategyDTO>> getStrategiesBySourceAndStatus(
            @PathVariable String source, @PathVariable StrategyStatus status) {
        log.info("REST request to get strategies for source: {} with status: {}", source, status);
        return ResponseEntity.ok(strategyService.getStrategiesBySourceAndStatus(source, status));
    }

    @GetMapping("/date-range")
    @Operation(summary = "Получить стратегии в диапазоне дат")
    public ResponseEntity<List<StrategyDTO>> getStrategiesByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {
        log.info("REST request to get strategies between {} and {}", start, end);
        return ResponseEntity.ok(strategyService.getStrategiesByDateRange(start, end));
    }

    @PostMapping
    @Operation(summary = "Создать новую стратегию")
    public ResponseEntity<StrategyDTO> createStrategy(@RequestBody Strategy strategy) {
        log.info("REST request to save strategy: {}", strategy);
        return new ResponseEntity<>(strategyService.createStrategy(strategy), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Обновить существующую стратегию")
    public ResponseEntity<StrategyDTO> updateStrategy(@PathVariable Long id, @RequestBody Strategy strategy) {
        log.info("REST request to update strategy with id: {}", id);
        return ResponseEntity.ok(strategyService.updateStrategy(id, strategy));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Удалить стратегию")
    public ResponseEntity<Void> deleteStrategy(@PathVariable Long id) {
        log.info("REST request to delete strategy with id: {}", id);
        strategyService.deleteStrategy(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/latest/{count}")
    @Operation(summary = "Получить последние активные стратегии")
    public ResponseEntity<List<StrategyDTO>> getLatestActiveStrategies(@PathVariable int count) {
        log.info("REST request to get latest {} active strategies", count);
        return ResponseEntity.ok(strategyService.getLatestActiveStrategies(count));
    }
}