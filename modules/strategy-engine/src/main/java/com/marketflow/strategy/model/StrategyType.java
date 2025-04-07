package com.marketflow.strategy.model;

import lombok.Getter;

/**
 * Перечисление типов маркетинговых стратегий.
 */
@Getter
public enum StrategyType {
    CONVERSION_OPTIMIZATION("Оптимизация конверсии"),
    REACH_EXPANSION("Расширение охвата"),
    COST_REDUCTION("Снижение стоимости"),
    RETARGETING("Ретаргетинг"),
    AUDIENCE_SEGMENTATION("Сегментация аудитории"),
    CROSS_PLATFORM("Кросс-платформенная стратегия"),
    SEASONAL("Сезонная кампания"),
    COMPETITIVE("Конкурентная стратегия");

    private final String description;

    StrategyType(String description) {
        this.description = description;
    }

}