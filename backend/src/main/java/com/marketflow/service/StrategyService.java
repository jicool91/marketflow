package com.marketflow.service;

import com.marketflow.model.strategy.StrategyDTO;
import com.marketflow.model.strategy.Strategy;
import com.marketflow.model.strategy.StrategyStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface StrategyService {

    List<StrategyDTO> getAllStrategies();

    Optional<StrategyDTO> getStrategyById(Long id);

    List<StrategyDTO> getStrategiesByStatus(StrategyStatus status);

    List<StrategyDTO> getStrategiesBySourceAndStatus(String source, StrategyStatus status);

    List<StrategyDTO> getStrategiesByDateRange(LocalDateTime start, LocalDateTime end);

    StrategyDTO createStrategy(Strategy strategy);

    StrategyDTO updateStrategy(Long id, Strategy strategy);

    void deleteStrategy(Long id);

    List<StrategyDTO> getLatestActiveStrategies(int count);
}