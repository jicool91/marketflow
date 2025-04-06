package com.marketflow.repository;

import com.marketflow.model.strategy.Strategy;
import com.marketflow.model.strategy.StrategyStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface StrategyRepository extends JpaRepository<Strategy, Long> {

    List<Strategy> findByStatus(StrategyStatus status);

    List<Strategy> findBySourceAndStatus(String source, StrategyStatus status);

    List<Strategy> findByGeneratedAtBetween(LocalDateTime start, LocalDateTime end);

    List<Strategy> findTop5ByStatusOrderByGeneratedAtDesc(StrategyStatus status);
}