package com.marketflow.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "metrics")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Metric {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate date;

    private Integer clicks;

    private Integer impressions;

    private Double cost;

    private String source;

    private String campaignId;

    @Transient
    public Double getCtr() {
        if (impressions != null && impressions > 0 && clicks != null) {
            return (double) clicks / impressions * 100;
        }
        return 0.0;
    }

    @Transient
    public Double getCpc() {
        if (clicks != null && clicks > 0 && cost != null) {
            return cost / clicks;
        }
        return 0.0;
    }
}