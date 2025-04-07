package com.marketflow.strategy.service;

import com.marketflow.strategy.config.EngineConfig;
import com.marketflow.strategy.model.MetricData;
import com.marketflow.strategy.model.StrategyResult;
import lombok.extern.slf4j.Slf4j;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Сервис для работы с базой данных
 */
@Slf4j
public class DatabaseService {
    private final EngineConfig config;

    public DatabaseService(EngineConfig config) {
        this.config = config;
    }

    /**
     * Получение метрик за указанный период
     * @param daysBack количество дней назад для анализа
     * @return список метрик
     */
    public List<MetricData> getMetricsForPeriod(int daysBack) {
        LocalDate endDate = LocalDate.now();
        LocalDate startDate = endDate.minusDays(daysBack);

        List<MetricData> metrics = new ArrayList<>();

        String sql = "SELECT id, date, clicks, impressions, cost, source, campaign_id " +
                "FROM metrics WHERE date BETWEEN ? AND ? ORDER BY date ASC";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setObject(1, startDate);
            stmt.setObject(2, endDate);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    MetricData metric = MetricData.builder()
                            .id(rs.getLong("id"))
                            .date(rs.getObject("date", LocalDate.class))
                            .clicks(rs.getInt("clicks"))
                            .impressions(rs.getInt("impressions"))
                            .cost(rs.getDouble("cost"))
                            .source(rs.getString("source"))
                            .campaignId(rs.getString("campaign_id"))
                            .build();

                    // Расчет дополнительных метрик
                    metric.setCtr(metric.calculateCtr());
                    metric.setCpc(metric.calculateCpc());

                    metrics.add(metric);
                }
            }

            log.info("Loaded {} metrics from database for period {} to {}",
                    metrics.size(), startDate, endDate);

        } catch (SQLException e) {
            log.error("Error loading metrics from database", e);
            throw new RuntimeException("Database error when loading metrics", e);
        }

        return metrics;
    }

    /**
     * Сохранение результата стратегии в базу данных
     * @param strategyResult результат для сохранения
     * @return сохраненный результат с присвоенным ID
     */
    public StrategyResult saveStrategyResult(StrategyResult strategyResult) {
        String sql = "INSERT INTO strategies (name, description, source, generated_at, " +
                "recommendations, status, metrics_period) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?) RETURNING id";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, strategyResult.getName());
            stmt.setString(2, strategyResult.getDescription());
            stmt.setString(3, strategyResult.getSource());
            stmt.setObject(4, strategyResult.getGeneratedAt() != null ?
                    strategyResult.getGeneratedAt() : LocalDateTime.now());
            stmt.setString(5, strategyResult.getFormattedRecommendations());
            stmt.setString(6, "DRAFT"); // Изначальный статус
            stmt.setInt(7, strategyResult.getMetricsPeriod());

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Long id = rs.getLong(1);
                    strategyResult.setId(id);
                    log.info("Saved strategy with ID: {}", id);
                }
            }

        } catch (SQLException e) {
            log.error("Error saving strategy to database", e);
            throw new RuntimeException("Database error when saving strategy", e);
        }

        return strategyResult;
    }

    /**
     * Получение агрегированных метрик по источникам
     * @param daysBack количество дней для анализа
     * @return Карта с агрегированными метриками по источникам
     */
    public Map<String, Map<String, Double>> getAggregatedMetricsBySource(int daysBack) {
        LocalDate endDate = LocalDate.now();
        LocalDate startDate = endDate.minusDays(daysBack);

        Map<String, Map<String, Double>> result = new HashMap<>();

        String sql = "SELECT source, " +
                "SUM(clicks) as total_clicks, " +
                "SUM(impressions) as total_impressions, " +
                "SUM(cost) as total_cost, " +
                "SUM(clicks)*100.0/NULLIF(SUM(impressions),0) as avg_ctr, " +
                "SUM(cost)/NULLIF(SUM(clicks),0) as avg_cpc " +
                "FROM metrics WHERE date BETWEEN ? AND ? " +
                "GROUP BY source";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setObject(1, startDate);
            stmt.setObject(2, endDate);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    String source = rs.getString("source");
                    Map<String, Double> metrics = new HashMap<>();

                    metrics.put("clicks", rs.getDouble("total_clicks"));
                    metrics.put("impressions", rs.getDouble("total_impressions"));
                    metrics.put("cost", rs.getDouble("total_cost"));
                    metrics.put("ctr", rs.getDouble("avg_ctr"));
                    metrics.put("cpc", rs.getDouble("avg_cpc"));

                    result.put(source, metrics);
                }
            }

            log.info("Loaded aggregated metrics for {} sources", result.size());

        } catch (SQLException e) {
            log.error("Error loading aggregated metrics", e);
            throw new RuntimeException("Database error when loading aggregated metrics", e);
        }

        return result;
    }

    /**
     * Получение соединения с базой данных
     */
    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(
                config.getDbUrl(),
                config.getDbUsername(),
                config.getDbPassword());
    }
}