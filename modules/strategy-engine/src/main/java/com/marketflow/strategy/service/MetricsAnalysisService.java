package com.marketflow.strategy.service;

import com.marketflow.strategy.model.MetricData;
import com.marketflow.strategy.util.MetricsCalculator;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Сервис для анализа маркетинговых метрик
 */
@Slf4j
public class MetricsAnalysisService {

    private final DatabaseService databaseService;
    private final MetricsCalculator calculator;

    public MetricsAnalysisService(DatabaseService databaseService) {
        this.databaseService = databaseService;
        this.calculator = new MetricsCalculator();
    }

    /**
     * Анализ эффективности рекламных источников
     * @param daysBack количество дней для анализа
     * @return карта с рейтингами эффективности источников
     */
    public Map<String, Double> analyzeSourceEfficiency(int daysBack) {
        log.info("Analyzing source efficiency for the last {} days", daysBack);

        Map<String, Map<String, Double>> aggregatedMetrics =
                databaseService.getAggregatedMetricsBySource(daysBack);

        Map<String, Double> efficiencyRatings = new HashMap<>();

        // Проход по всем источникам и расчет эффективности
        for (Map.Entry<String, Map<String, Double>> entry : aggregatedMetrics.entrySet()) {
            String source = entry.getKey();
            Map<String, Double> metrics = entry.getValue();

            double ctr = metrics.getOrDefault("ctr", 0.0);
            double cpc = metrics.getOrDefault("cpc", 0.0);
            double clicks = metrics.getOrDefault("clicks", 0.0);
            double cost = metrics.getOrDefault("cost", 0.0);

            // Расчет рейтинга эффективности (упрощенная формула)
            // Низкий CPC и высокий CTR дают высокий рейтинг
            double efficiency = calculator.calculateEfficiencyRating(ctr, cpc, clicks, cost);

            efficiencyRatings.put(source, efficiency);
            log.debug("Source {} efficiency: {}", source, efficiency);
        }

        log.info("Completed efficiency analysis for {} sources", efficiencyRatings.size());
        return efficiencyRatings;
    }

    /**
     * Анализ трендов в метриках за указанный период
     * @param daysBack количество дней для анализа
     * @return карта трендов по метрикам
     */
    public Map<String, Double> analyzeMetricTrends(int daysBack) {
        log.info("Analyzing metric trends for the last {} days", daysBack);

        List<MetricData> metrics = databaseService.getMetricsForPeriod(daysBack);

        // Группировка метрик по датам для расчета ежедневных значений
        Map<LocalDate, List<MetricData>> metricsByDate = metrics.stream()
                .collect(Collectors.groupingBy(MetricData::getDate));

        // Сортировка дат для анализа тренда
        List<LocalDate> sortedDates = new ArrayList<>(metricsByDate.keySet());
        Collections.sort(sortedDates);

        // Расчет ежедневных агрегированных значений
        List<Double> dailyCtr = new ArrayList<>();
        List<Double> dailyCpc = new ArrayList<>();

        for (LocalDate date : sortedDates) {
            List<MetricData> dailyMetrics = metricsByDate.get(date);

            // Расчет дневных CTR и CPC
            double totalClicks = dailyMetrics.stream()
                    .mapToDouble(m -> m.getClicks() != null ? m.getClicks() : 0)
                    .sum();

            double totalImpressions = dailyMetrics.stream()
                    .mapToDouble(m -> m.getImpressions() != null ? m.getImpressions() : 0)
                    .sum();

            double totalCost = dailyMetrics.stream()
                    .mapToDouble(m -> m.getCost() != null ? m.getCost() : 0)
                    .sum();

            double ctr = totalImpressions > 0 ? (totalClicks / totalImpressions) * 100 : 0;
            double cpc = totalClicks > 0 ? (totalCost / totalClicks) : 0;

            dailyCtr.add(ctr);
            dailyCpc.add(cpc);
        }

        // Анализ трендов
        Map<String, Double> trends = new HashMap<>();
        trends.put("ctr_trend", calculator.calculateTrend(dailyCtr));
        trends.put("cpc_trend", calculator.calculateTrend(dailyCpc));

        log.info("Completed trend analysis. CTR trend: {}, CPC trend: {}",
                trends.get("ctr_trend"), trends.get("cpc_trend"));

        return trends;
    }

    /**
     * Определение аномалий в метриках
     * @param daysBack количество дней для анализа
     * @return список обнаруженных аномалий
     */
    public List<String> detectAnomalies(int daysBack) {
        log.info("Detecting anomalies for the last {} days", daysBack);

        List<MetricData> metrics = databaseService.getMetricsForPeriod(daysBack);
        List<String> anomalies = new ArrayList<>();

        // Группировка метрик по источникам
        Map<String, List<MetricData>> metricsBySource = metrics.stream()
                .collect(Collectors.groupingBy(m ->
                        m.getSource() != null ? m.getSource() : "unknown"));

        // Проверка аномалий для каждого источника
        for (Map.Entry<String, List<MetricData>> entry : metricsBySource.entrySet()) {
            String source = entry.getKey();
            List<MetricData> sourceMetrics = entry.getValue();

            // Анализ на выбросы в CTR
            List<Double> ctrValues = sourceMetrics.stream()
                    .map(MetricData::getCtr)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());

            if (!ctrValues.isEmpty()) {
                List<Integer> outliers = calculator.detectOutliers(ctrValues);
                for (Integer index : outliers) {
                    if (index < sourceMetrics.size()) {
                        MetricData anomaly = sourceMetrics.get(index);
                        anomalies.add(String.format(
                                "Аномально %s CTR (%.2f%%) для источника %s на дату %s",
                                anomaly.getCtr() > calculator.calculateMean(ctrValues) ? "высокий" : "низкий",
                                anomaly.getCtr(),
                                source,
                                anomaly.getDate()
                        ));
                    }
                }
            }

            // Анализ на выбросы в CPC
            List<Double> cpcValues = sourceMetrics.stream()
                    .map(MetricData::getCpc)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());

            if (!cpcValues.isEmpty()) {
                List<Integer> outliers = calculator.detectOutliers(cpcValues);
                for (Integer index : outliers) {
                    if (index < sourceMetrics.size()) {
                        MetricData anomaly = sourceMetrics.get(index);
                        anomalies.add(String.format(
                                "Аномально %s CPC (%.2f) для источника %s на дату %s",
                                anomaly.getCpc() > calculator.calculateMean(cpcValues) ? "высокий" : "низкий",
                                anomaly.getCpc(),
                                source,
                                anomaly.getDate()
                        ));
                    }
                }
            }
        }

        log.info("Detected {} anomalies", anomalies.size());
        return anomalies;
    }
}