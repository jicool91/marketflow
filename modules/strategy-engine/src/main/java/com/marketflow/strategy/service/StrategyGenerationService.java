package com.marketflow.strategy.service;

import com.marketflow.strategy.model.StrategyResult;
import com.marketflow.strategy.model.StrategyType;
import com.marketflow.strategy.util.RecommendationEngine;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Сервис для генерации маркетинговых стратегий
 */
@Slf4j
public class StrategyGenerationService {

    private final DatabaseService databaseService;
    private final MetricsAnalysisService analysisService;
    private final RecommendationEngine recommendationEngine;

    public StrategyGenerationService(DatabaseService databaseService) {
        this.databaseService = databaseService;
        this.analysisService = new MetricsAnalysisService(databaseService);
        this.recommendationEngine = new RecommendationEngine();
    }

    /**
     * Генерация стратегии для указанного источника
     * @param source источник для генерации стратегии
     * @param daysBack количество дней для анализа
     * @return результат генерации стратегии
     */
    public StrategyResult generateStrategy(String source, int daysBack) {
        log.info("Generating strategy for source: {} based on {} days of data", source, daysBack);

        // Анализ метрик
        Map<String, Double> sourceEfficiency = analysisService.analyzeSourceEfficiency(daysBack);
        Map<String, Double> trends = analysisService.analyzeMetricTrends(daysBack);
        List<String> anomalies = analysisService.detectAnomalies(daysBack);

        // Определение типа стратегии на основе метрик
        StrategyType strategyType = determineStrategyType(source, sourceEfficiency, trends);

        // Сбор всех метрик для анализа
        Map<String, Double> allMetrics = new HashMap<>();
        allMetrics.putAll(sourceEfficiency);
        allMetrics.putAll(trends);

        // Создание результата
        StrategyResult result = StrategyResult.builder()
                .name("Стратегия для " + source + " на основе данных за " + daysBack + " дней")
                .description(generateDescription(source, strategyType, trends))
                .strategyType(strategyType)
                .source(source)
                .generatedAt(LocalDateTime.now())
                .metrics(allMetrics)
                .metricsPeriod(daysBack)
                .confidenceScore(calculateConfidenceScore(source, allMetrics, anomalies.size()))
                .build();

        // Генерация рекомендаций
        List<String> recommendations = recommendationEngine.generateRecommendations(
                source, strategyType, allMetrics, anomalies);

        recommendations.forEach(result::addRecommendation);

        // Сохранение результата
        return databaseService.saveStrategyResult(result);
    }

    /**
     * Определение наиболее подходящего типа стратегии
     * @param source источник
     * @param efficiency данные эффективности
     * @param trends данные трендов
     * @return тип стратегии
     */
    private StrategyType determineStrategyType(
            String source, Map<String, Double> efficiency, Map<String, Double> trends) {

        // Получение эффективности конкретного источника
        Double sourceEfficiency = efficiency.getOrDefault(source, 0.0);
        Double ctrTrend = trends.getOrDefault("ctr_trend", 0.0);
        Double cpcTrend = trends.getOrDefault("cpc_trend", 0.0);

        // Логика определения типа стратегии на основе метрик
        if (sourceEfficiency < 0.3) {
            // Низкая эффективность
            if (cpcTrend > 0.1) {
                return StrategyType.COST_REDUCTION;
            } else {
                return StrategyType.CONVERSION_OPTIMIZATION;
            }
        } else if (sourceEfficiency < 0.7) {
            // Средняя эффективность
            if (ctrTrend < -0.1) {
                return StrategyType.AUDIENCE_SEGMENTATION;
            } else {
                return StrategyType.RETARGETING;
            }
        } else {
            // Высокая эффективность
            if (ctrTrend > 0.1) {
                return StrategyType.REACH_EXPANSION;
            } else {
                return StrategyType.CROSS_PLATFORM;
            }
        }
    }

    /**
     * Генерация описания стратегии
     * @param source источник
     * @param type тип стратегии
     * @param trends данные трендов
     * @return текстовое описание
     */
    private String generateDescription(String source, StrategyType type, Map<String, Double> trends) {
        StringBuilder description = new StringBuilder();
        description.append("Стратегия типа \"")
                .append(type.getDescription())
                .append("\" для рекламного источника ")
                .append(source)
                .append(".\n\n");

        // Добавление описания на основе трендов
        Double ctrTrend = trends.getOrDefault("ctr_trend", 0.0);
        Double cpcTrend = trends.getOrDefault("cpc_trend", 0.0);

        description.append("Анализ трендов показывает ");

        if (ctrTrend > 0.05) {
            description.append("положительную динамику CTR, ");
        } else if (ctrTrend < -0.05) {
            description.append("отрицательную динамику CTR, ");
        } else {
            description.append("стабильный CTR, ");
        }

        if (cpcTrend > 0.05) {
            description.append("с ростом стоимости клика. ");
        } else if (cpcTrend < -0.05) {
            description.append("со снижением стоимости клика. ");
        } else {
            description.append("при стабильной стоимости клика. ");
        }

        description.append("\n\nСтратегия разработана для оптимизации рекламной кампании ")
                .append("с учетом текущих показателей эффективности и динамики изменения метрик.");

        return description.toString();
    }

    /**
     * Расчет оценки уверенности в стратегии
     * @param source источник
     * @param metrics данные метрик
     * @param anomalyCount количество обнаруженных аномалий
     * @return оценка уверенности (0-100)
     */
    private Integer calculateConfidenceScore(String source, Map<String, Double> metrics, int anomalyCount) {
        // Базовый уровень уверенности
        int baseScore = 70;

        // Снижение за каждую аномалию
        int anomalyPenalty = Math.min(anomalyCount * 5, 30);

        // Бонус за положительные тренды
        Double ctrTrend = metrics.getOrDefault("ctr_trend", 0.0);
        int trendBonus = (int) (ctrTrend > 0 ? 10 * ctrTrend : 0);

        // Итоговая оценка
        int score = baseScore - anomalyPenalty + trendBonus;

        // Ограничение диапазоном 0-100
        return Math.max(0, Math.min(100, score));
    }
}