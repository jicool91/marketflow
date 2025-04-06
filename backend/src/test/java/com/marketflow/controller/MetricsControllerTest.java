package com.marketflow.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.marketflow.model.Metric;
import com.marketflow.model.MetricDTO;
import com.marketflow.model.MetricSummary;
import com.marketflow.service.MetricsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(MetricsController.class)
public class MetricsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MetricsService metricsService;

    @Autowired
    private ObjectMapper objectMapper;

    private MetricDTO testMetricDto;
    private Metric testMetric;
    private MetricSummary testSummary;

    @BeforeEach
    public void setup() {
        testMetric = Metric.builder()
                .id(1L)
                .date(LocalDate.now())
                .clicks(100)
                .impressions(1000)
                .cost(50.0)
                .source("yandex")
                .campaignId("test123")
                .build();

        testMetricDto = MetricDTO.fromEntity(testMetric);

        testSummary = MetricSummary.builder()
                .totalClicks(1000L)
                .totalImpressions(10000L)
                .totalCost(500.0)
                .averageCtr(10.0)
                .averageCpc(0.5)
                .roi(100.0)
                .build();
    }

    @Test
    public void testGetAllMetrics() throws Exception {
        List<MetricDTO> metrics = Arrays.asList(testMetricDto);
        when(metricsService.getAllMetrics()).thenReturn(metrics);

        mockMvc.perform(get("/api/metrics")
                        .header("X-API-KEY", "123456"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(testMetricDto.getId()))
                .andExpect(jsonPath("$[0].source").value(testMetricDto.getSource()));
    }

    @Test
    public void testGetMetricsByType() throws Exception {
        List<MetricDTO> metrics = Arrays.asList(testMetricDto);
        when(metricsService.getMetricsByType(anyString())).thenReturn(metrics);

        mockMvc.perform(get("/api/metrics/yandex")
                        .header("X-API-KEY", "123456"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].source").value("yandex"));
    }

    @Test
    public void testGetMetricsSummary() throws Exception {
        when(metricsService.getMetricsSummary()).thenReturn(testSummary);

        mockMvc.perform(get("/api/metrics/summary")
                        .header("X-API-KEY", "123456"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.totalClicks").value(1000))
                .andExpect(jsonPath("$.roi").value(100.0));
    }

    @Test
    public void testCreateMetric() throws Exception {
        when(metricsService.saveMetric(any(Metric.class))).thenReturn(testMetricDto);

        mockMvc.perform(post("/api/metrics")
                        .header("X-API-KEY", "123456")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testMetric)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(testMetricDto.getId()));
    }

    @Test
    public void testDeleteMetric() throws Exception {
        doNothing().when(metricsService).deleteMetric(anyLong());

        mockMvc.perform(delete("/api/metrics/1")
                        .header("X-API-KEY", "123456"))
                .andExpect(status().isNoContent());

        verify(metricsService, times(1)).deleteMetric(1L);
    }
}