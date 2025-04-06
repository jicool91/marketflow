package com.marketflow.service;

import com.marketflow.model.Metric;
import com.marketflow.model.MetricDTO;
import com.marketflow.model.MetricSummary;
import com.marketflow.repository.MetricsRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

public class MetricsServiceTest {

    @Mock
    private MetricsRepository metricsRepository;

    @InjectMocks
    private MetricsService metricsService;

    private Metric testMetric;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        testMetric = Metric.builder()
                .id(1L)
                .date(LocalDate.now())
                .clicks(100)
                .impressions(1000)
                .cost(50.0)
                .source("yandex")
                .campaignId("test123")
                .build();
    }

    @Test
    public void testGetAllMetrics() {
        when(metricsRepository.findAll()).thenReturn(Arrays.asList(testMetric));

        List<MetricDTO> result = metricsService.getAllMetrics();

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(metricsRepository, times(1)).findAll();
    }

    @Test
    public void testGetMetricsByType() {
        when(metricsRepository.findBySource(anyString())).thenReturn(Arrays.asList(testMetric));

        List<MetricDTO> result = metricsService.getMetricsByType("yandex");

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("yandex", result.get(0).getSource());
        verify(metricsRepository, times(1)).findBySource("yandex");
    }

    @Test
    public void testGetMetricsSummary() {
        when(metricsRepository.getTotalClicks()).thenReturn(1000L);
        when(metricsRepository.getTotalImpressions()).thenReturn(10000L);
        when(metricsRepository.getTotalCost()).thenReturn(500.0);
        when(metricsRepository.getAverageCtr()).thenReturn(10.0);
        when(metricsRepository.getAverageCpc()).thenReturn(0.5);

        MetricSummary summary = metricsService.getMetricsSummary();

        assertNotNull(summary);
        assertEquals(1000L, summary.getTotalClicks());
        assertEquals(10000L, summary.getTotalImpressions());
        assertEquals(500.0, summary.getTotalCost());
        assertEquals(10.0, summary.getAverageCtr());
        assertEquals(0.5, summary.getAverageCpc());
        // Проверяем ROI (500 * 2 - 500) / 500 * 100 = 100%
        assertEquals(100.0, summary.getRoi());
    }

    @Test
    public void testSaveMetric() {
        when(metricsRepository.save(any(Metric.class))).thenReturn(testMetric);

        MetricDTO savedDto = metricsService.saveMetric(testMetric);

        assertNotNull(savedDto);
        assertEquals(testMetric.getId(), savedDto.getId());
        verify(metricsRepository, times(1)).save(testMetric);
    }
}