package com.marketflow.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MetricSummary {
    private Long totalClicks;
    private Long totalImpressions;
    private Double totalCost;
    private Double averageCtr;
    private Double averageCpc;
    private Double roi;
}