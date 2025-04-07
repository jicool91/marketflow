package com.marketflow.strategy.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * Модель данных маркетинговых метрик для анализа.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MetricData {
    private Long id;
    private String source;         // Рекламная платформа (yandex, google, vk и т.д.)
    private String campaignId;     // Идентификатор рекламной кампании
    private LocalDate date;        // Дата сбора метрик
    private Integer impressions;   // Количество показов
    private Integer clicks;        // Количество кликов
    private Double cost;           // Затраты на рекламу
    private Double ctr;            // Click-Through Rate (%)
    private Double cpc;            // Cost Per Click
    private Integer conversions;   // Количество конверсий (если доступно)
    private Double conversionRate; // Коэффициент конверсии (если доступно)
    private Double roas;           // Return On Ad Spend (если доступно)

    /**
     * Расчет CTR на основе имеющихся данных
     * @return значение CTR в процентах
     */
    public Double calculateCtr() {
        if (impressions != null && impressions > 0 && clicks != null) {
            return (double) clicks / impressions * 100;
        }
        return 0.0;
    }

    /**
     * Расчет CPC на основе имеющихся данных
     * @return значение CPC
     */
    public Double calculateCpc() {
        if (clicks != null && clicks > 0 && cost != null) {
            return cost / clicks;
        }
        return 0.0;
    }
}