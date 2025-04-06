package com.marketflow.service.impl;

import com.marketflow.model.strategy.Strategy;
import com.marketflow.model.strategy.StrategyDTO;
import com.marketflow.model.strategy.StrategyStatus;
import com.marketflow.repository.StrategyRepository;
import com.marketflow.service.StrategyService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class StrategyServiceImpl implements StrategyService {

    private final StrategyRepository strategyRepository;

    @Override
    public List<StrategyDTO> getAllStrategies() {
        log.info("Fetching all strategies");
        return strategyRepository.findAll().stream()
                .map(StrategyDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<StrategyDTO> getStrategyById(Long id) {
        log.info("Fetching strategy with id: {}", id);
        return strategyRepository.findById(id)
                .map(StrategyDTO::fromEntity);
    }

    @Override
    public List<StrategyDTO> getStrategiesByStatus(StrategyStatus status) {
        log.info("Fetching strategies with status: {}", status);
        return strategyRepository.findByStatus(status).stream()
                .map(StrategyDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public List<StrategyDTO> getStrategiesBySourceAndStatus(String source, StrategyStatus status) {
        log.info("Fetching strategies for source: {} with status: {}", source, status);
        return strategyRepository.findBySourceAndStatus(source, status).stream()
                .map(StrategyDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public List<StrategyDTO> getStrategiesByDateRange(LocalDateTime start, LocalDateTime end) {
        log.info("Fetching strategies between {} and {}", start, end);
        return strategyRepository.findByGeneratedAtBetween(start, end).stream()
                .map(StrategyDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public StrategyDTO createStrategy(Strategy strategy) {
        log.info("Creating new strategy: {}", strategy);
        if (strategy.getGeneratedAt() == null) {
            strategy.setGeneratedAt(LocalDateTime.now());
        }
        if (strategy.getStatus() == null) {
            strategy.setStatus(StrategyStatus.DRAFT);
        }
        Strategy savedStrategy = strategyRepository.save(strategy);
        return StrategyDTO.fromEntity(savedStrategy);
    }

    @Override
    @Transactional
    public StrategyDTO updateStrategy(Long id, Strategy strategy) {
        log.info("Updating strategy with id: {}", id);

        Strategy existingStrategy = strategyRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Strategy not found with id: " + id));

        existingStrategy.setName(strategy.getName());
        existingStrategy.setDescription(strategy.getDescription());
        existingStrategy.setSource(strategy.getSource());
        existingStrategy.setRecommendations(strategy.getRecommendations());
        existingStrategy.setStatus(strategy.getStatus());
        existingStrategy.setMetricsPeriod(strategy.getMetricsPeriod());

        Strategy updatedStrategy = strategyRepository.save(existingStrategy);
        return StrategyDTO.fromEntity(updatedStrategy);
    }

    @Override
    @Transactional
    public void deleteStrategy(Long id) {
        log.info("Deleting strategy with id: {}", id);
        strategyRepository.deleteById(id);
    }

    @Override
    public List<StrategyDTO> getLatestActiveStrategies(int count) {
        log.info("Fetching top {} active strategies", count);
        return strategyRepository.findTop5ByStatusOrderByGeneratedAtDesc(StrategyStatus.ACTIVE).stream()
                .map(StrategyDTO::fromEntity)
                .limit(count)
                .collect(Collectors.toList());
    }
}