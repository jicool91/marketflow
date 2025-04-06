package com.marketflow.model.strategy;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StrategyDTO {
    private Long id;
    private String name;
    private String description;
    private String source;
    private String recommendations;
    private LocalDateTime generatedAt;
    private StrategyStatus status;
    private Integer metricsPeriod;

    public static StrategyDTO fromEntity(Strategy strategy) {
        return StrategyDTO.builder()
                .id(strategy.getId())
                .name(strategy.getName())
                .description(strategy.getDescription())
                .source(strategy.getSource())
                .recommendations(strategy.getRecommendations())
                .generatedAt(strategy.getGeneratedAt())
                .status(strategy.getStatus())
                .metricsPeriod(strategy.getMetricsPeriod())
                .build();
    }
}