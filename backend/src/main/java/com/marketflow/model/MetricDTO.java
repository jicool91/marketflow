package com.marketflow.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MetricDTO {
    private Long id;
    private LocalDate date;
    private Integer clicks;
    private Integer impressions;
    private Double cost;
    private String source;
    private String campaignId;
    private Double ctr;
    private Double cpc;

    public static MetricDTO fromEntity(Metric metric) {
        return MetricDTO.builder()
                .id(metric.getId())
                .date(metric.getDate())
                .clicks(metric.getClicks())
                .impressions(metric.getImpressions())
                .cost(metric.getCost())
                .source(metric.getSource())
                .campaignId(metric.getCampaignId())
                .ctr(metric.getCtr())
                .cpc(metric.getCpc())
                .build();
    }
}