package com.marketflow.strategy;

import com.marketflow.strategy.config.EngineConfig;
import com.marketflow.strategy.model.StrategyResult;
import com.marketflow.strategy.service.DatabaseService;
import com.marketflow.strategy.service.StrategyGenerationService;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.List;

/**
 * Основной класс приложения strategy-engine.
 * Отвечает за инициализацию компонентов и запуск процесса генерации стратегий.
 */
@Slf4j
public class EngineApp {

    public static void main(String[] args) {
        log.info("Starting Strategy Engine Application");

        try {
            // Загрузка конфигурации
            EngineConfig config = EngineConfig.load();
            if (!config.isValid()) {
                log.error("Invalid configuration. Exiting application.");
                System.exit(1);
            }

            // Инициализация сервисов
            DatabaseService databaseService = new DatabaseService(config);
            StrategyGenerationService strategyService = new StrategyGenerationService(databaseService);

            // Обработка аргументов командной строки
            int analysisPeriod = config.getAnalysisDefaultPeriod();
            List<String> sources = getSourcesFromArgs(args);

            if (sources.isEmpty()) {
                log.info("No specific sources provided. Using default sources.");
                sources = Arrays.asList("yandex", "google", "vk");
            }

            log.info("Generating strategies for sources: {} with period: {} days", sources, analysisPeriod);

            // Генерация стратегий для каждого источника
            for (String source : sources) {
                try {
                    log.info("Generating strategy for source: {}", source);
                    StrategyResult result = strategyService.generateStrategy(source, analysisPeriod);
                    log.info("Strategy generated successfully for source: {} with ID: {}", source, result.getId());

                    // Вывод результата
                    log.info("Strategy type: {}", result.getStrategyType());
                    log.info("Confidence score: {}", result.getConfidenceScore());
                    log.info("Number of recommendations: {}",
                            result.getRecommendations() != null ? result.getRecommendations().size() : 0);
                } catch (Exception e) {
                    log.error("Error generating strategy for source: {}", source, e);
                }
            }

            log.info("Strategy Engine completed successfully");

        } catch (Exception e) {
            log.error("Unexpected error occurred", e);
            System.exit(1);
        }
    }

    /**
     * Извлекает список источников из аргументов командной строки
     * @param args аргументы командной строки
     * @return список источников
     */
    private static List<String> getSourcesFromArgs(String[] args) {
        if (args.length > 0) {
            String sourcesArg = args[0];
            if (sourcesArg.contains(",")) {
                return Arrays.asList(sourcesArg.split(","));
            } else {
                return Arrays.asList(args);
            }
        }
        return Arrays.asList();
    }
}