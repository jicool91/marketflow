package com.marketflow.service;

import com.marketflow.model.Metric;
import com.marketflow.model.MetricDTO;
import com.marketflow.model.MetricSummary;
import com.marketflow.repository.MetricsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class MetricsService {

    private final MetricsRepository metricsRepository;

    public List<MetricDTO> getAllMetrics() {
        log.info("Fetching all metrics");
        return metricsRepository.findAll().stream()
                .map(MetricDTO::fromEntity)
                .collect(Collectors.toList());
    }

    public List<MetricDTO> getMetricsByType(String type) {
        log.info("Fetching metrics for source: {}", type);
        return metricsRepository.findBySource(type).stream()
                .map(MetricDTO::fromEntity)
                .collect(Collectors.toList());
    }

    public List<MetricDTO> getMetricsByDateRange(LocalDate startDate, LocalDate endDate) {
        log.info("Fetching metrics between {} and {}", startDate, endDate);
        return metricsRepository.findByDateBetween(startDate, endDate).stream()
                .map(MetricDTO::fromEntity)
                .collect(Collectors.toList());
    }

    public MetricSummary getMetricsSummary() {
        log.info("Calculating metrics summary");

        return MetricSummary.builder()
                .totalClicks(metricsRepository.getTotalClicks())
                .totalImpressions(metricsRepository.getTotalImpressions())
                .totalCost(metricsRepository.getTotalCost())
                .averageCtr(metricsRepository.getAverageCtr())
                .averageCpc(metricsRepository.getAverageCpc())
                .roi(calculateRoi())
                .build();
    }

    @Transactional
    public MetricDTO saveMetric(Metric metric) {
        log.info("Saving new metric: {}", metric);
        Metric savedMetric = metricsRepository.save(metric);
        return MetricDTO.fromEntity(savedMetric);
    }

    @Transactional
    public void deleteMetric(Long id) {
        log.info("Deleting metric with id: {}", id);
        metricsRepository.deleteById(id);
    }

    private Double calculateRoi() {
        // Здесь может быть более сложная логика расчета ROI
        Double totalCost = metricsRepository.getTotalCost();
        // Предположим, что доход составляет 2x от стоимости как пример
        Double estimatedRevenue = totalCost != null ? totalCost * 2 : 0;

        if (totalCost != null && totalCost > 0) {
            return (estimatedRevenue - totalCost) / totalCost * 100;
        }
        return 0.0;
    }
}