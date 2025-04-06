package com.marketflow.model.strategy;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "strategies")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Strategy {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description", length = 2000)
    private String description;

    @Column(name = "source")
    private String source;

    @Column(name = "recommendations", length = 5000)
    private String recommendations;

    @Column(name = "generated_at")
    private LocalDateTime generatedAt;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private StrategyStatus status;

    @Column(name = "metrics_period")
    private Integer metricsPeriod;
}