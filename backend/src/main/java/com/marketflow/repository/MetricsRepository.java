package com.marketflow.repository;

import com.marketflow.model.Metric;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface MetricsRepository extends JpaRepository<Metric, Long> {

    List<Metric> findBySource(String source);

    List<Metric> findByDateBetween(LocalDate startDate, LocalDate endDate);

    List<Metric> findBySourceAndDateBetween(String source, LocalDate startDate, LocalDate endDate);

    @Query("SELECT SUM(m.clicks) FROM Metric m")
    Long getTotalClicks();

    @Query("SELECT SUM(m.impressions) FROM Metric m")
    Long getTotalImpressions();

    @Query("SELECT SUM(m.cost) FROM Metric m")
    Double getTotalCost();

    @Query("SELECT AVG(m.clicks * 100.0 / m.impressions) FROM Metric m WHERE m.impressions > 0")
    Double getAverageCtr();

    @Query("SELECT AVG(m.cost / m.clicks) FROM Metric m WHERE m.clicks > 0")
    Double getAverageCpc();
}