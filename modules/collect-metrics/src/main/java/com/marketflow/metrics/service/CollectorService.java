package com.marketflow.metrics.service;

import com.marketflow.metrics.config.CollectorConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class CollectorService {
    private static final Logger log = LoggerFactory.getLogger(CollectorService.class);
    private final CollectorConfig config;
    private final Random random = new Random();

    public CollectorService(CollectorConfig config) {
        this.config = config;
    }

    /**
     * Основной метод для сбора и сохранения метрик
     * @return количество собранных метрик
     */
    public int collectAndStore() throws Exception {
        log.info("Starting metrics collection process");

        // В реальности здесь был бы вызов API рекламных платформ
        Map<String, Double> metrics = collectMetricsFromPlatforms();
        log.info("Collected {} raw metrics from platforms", metrics.size());

        // Сохраняем в БД
        int stored = storeMetrics(metrics);
        log.info("Successfully stored {} metrics in database", stored);

        return stored;
    }

    /**
     * Имитация сбора метрик из рекламных платформ
     */
    private Map<String, Double> collectMetricsFromPlatforms() {
        log.debug("Collecting metrics from platforms: {}", config.getPlatforms());

        Map<String, Double> metrics = new HashMap<>();

        // Метрики для Яндекс Директ (имитация)
        if (config.getPlatforms().contains("yandex")) {
            log.info("Collecting metrics from Yandex Direct");
            metrics.put("yandex_clicks", (double) random.nextInt(1000));
            metrics.put("yandex_impressions", (double) random.nextInt(10000));
            metrics.put("yandex_ctr", random.nextDouble() * 10.0);
            metrics.put("yandex_cost", random.nextDouble() * 5000.0);
        }

        // Метрики для Google Ads (имитация)
        if (config.getPlatforms().contains("google")) {
            log.info("Collecting metrics from Google Ads");
            metrics.put("google_clicks", (double) random.nextInt(1500));
            metrics.put("google_impressions", (double) random.nextInt(15000));
            metrics.put("google_ctr", random.nextDouble() * 12.0);
            metrics.put("google_cost", random.nextDouble() * 6000.0);
        }

        // Метрики для VK Ads (имитация)
        if (config.getPlatforms().contains("vk")) {
            log.info("Collecting metrics from VK Ads");
            metrics.put("vk_clicks", (double) random.nextInt(800));
            metrics.put("vk_impressions", (double) random.nextInt(8000));
            metrics.put("vk_ctr", random.nextDouble() * 8.0);
            metrics.put("vk_cost", random.nextDouble() * 3000.0);
        }

        return metrics;
    }

    /**
     * Сохранение метрик в базу данных
     */
    private int storeMetrics(Map<String, Double> metrics) throws Exception {
        if (metrics.isEmpty()) {
            log.warn("No metrics to store");
            return 0;
        }

        int count = 0;
        LocalDate today = LocalDate.now();

        try (Connection conn = DriverManager.getConnection(
                config.getDbUrl(), config.getDbUser(), config.getDbPass())) {

            // Сохраняем каждый тип метрики отдельно
            try (PreparedStatement clicksStmt = conn.prepareStatement(
                    "INSERT INTO metrics(date, clicks, impressions, cost) VALUES (?, ?, ?, ?)")) {

                // Метрики для Яндекс
                if (metrics.containsKey("yandex_clicks")) {
                    clicksStmt.setObject(1, today);
                    clicksStmt.setInt(2, metrics.get("yandex_clicks").intValue());
                    clicksStmt.setInt(3, metrics.get("yandex_impressions").intValue());
                    clicksStmt.setDouble(4, metrics.get("yandex_cost"));

                    clicksStmt.executeUpdate();
                    count++;

                    log.debug("Stored Yandex metrics for date {}", today);
                }

                // Метрики для Google
                if (metrics.containsKey("google_clicks")) {
                    clicksStmt.setObject(1, today);
                    clicksStmt.setInt(2, metrics.get("google_clicks").intValue());
                    clicksStmt.setInt(3, metrics.get("google_impressions").intValue());
                    clicksStmt.setDouble(4, metrics.get("google_cost"));

                    clicksStmt.executeUpdate();
                    count++;

                    log.debug("Stored Google metrics for date {}", today);
                }

                // Метрики для VK
                if (metrics.containsKey("vk_clicks")) {
                    clicksStmt.setObject(1, today);
                    clicksStmt.setInt(2, metrics.get("vk_clicks").intValue());
                    clicksStmt.setInt(3, metrics.get("vk_impressions").intValue());
                    clicksStmt.setDouble(4, metrics.get("vk_cost"));

                    clicksStmt.executeUpdate();
                    count++;

                    log.debug("Stored VK metrics for date {}", today);
                }
            }
        }

        return count;
    }
}