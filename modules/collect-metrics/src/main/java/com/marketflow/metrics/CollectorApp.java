package com.marketflow.metrics;

import com.marketflow.metrics.service.CollectorService;
import com.marketflow.metrics.config.CollectorConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CollectorApp {
    private static final Logger log = LoggerFactory.getLogger(CollectorApp.class);

    public static void main(String[] args) {
        log.info("Starting MarketFlow Metrics Collector v{}", CollectorApp.class.getPackage().getImplementationVersion());

        // Загрузка конфигурации
        CollectorConfig config = CollectorConfig.fromEnv();
        log.info("Configuration loaded: {}", config);

        try {
            // Инициализация сервиса
            CollectorService service = new CollectorService(config);

            // Запуск процесса сбора
            int collected = service.collectAndStore();
            log.info("Metrics collection completed. Collected {} metrics.", collected);

        } catch (Exception e) {
            log.error("Error during metrics collection", e);
            System.exit(1);
        }

        log.info("Metrics Collector job finished successfully");
    }
}