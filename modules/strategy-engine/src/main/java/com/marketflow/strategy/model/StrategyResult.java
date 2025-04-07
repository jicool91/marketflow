package com.marketflow.strategy.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Результат генерации маркетинговой стратегии.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StrategyResult {
    private Long id;
    private String name;
    private String description;
    private StrategyType strategyType;
    private String source;
    private LocalDateTime generatedAt;
    private Map<String, Double> metrics;

    @Builder.Default
    private List<String> recommendations = new ArrayList<>();

    private Integer confidenceScore; // Оценка уверенности (0-100)
    private Integer metricsPeriod; // Период анализа метрик в днях

    /**
     * Добавить рекомендацию в список
     * @param recommendation текст рекомендации
     */
    public void addRecommendation(String recommendation) {
        if (recommendations == null) {
            recommendations = new ArrayList<>();
        }
        recommendations.add(recommendation);
    }

    /**
     * Форматирует список рекомендаций в единую строку
     * @return форматированная строка со всеми рекомендациями
     */
    public String getFormattedRecommendations() {
        if (recommendations == null || recommendations.isEmpty()) {
            return "Рекомендации отсутствуют";
        }

        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < recommendations.size(); i++) {
            builder.append(i + 1)
                    .append(". ")
                    .append(recommendations.get(i))
                    .append("\n");
        }
        return builder.toString();
    }
}