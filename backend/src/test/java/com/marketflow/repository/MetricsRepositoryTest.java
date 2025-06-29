package com.marketflow.repository;

import com.marketflow.model.Metric;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
public class MetricsRepositoryTest {

    @Autowired
    private MetricsRepository metricsRepository;

    @Test
    @Sql("/test-data/metrics.sql")
    public void testFindBySource() {
        List<Metric> metrics = metricsRepository.findBySource("yandex");

        assertNotNull(metrics);
        assertFalse(metrics.isEmpty());
        metrics.forEach(metric -> assertEquals("yandex", metric.getSource()));
    }

    @Test
    @Sql("/test-data/metrics.sql")
    public void testFindByDateBetween() {
        LocalDate startDate = LocalDate.now().minusDays(7);
        LocalDate endDate = LocalDate.now();

        List<Metric> metrics = metricsRepository.findByDateBetween(startDate, endDate);

        assertNotNull(metrics);
        metrics.forEach(metric -> {
            assertTrue(
                    !metric.getDate().isBefore(startDate) &&
                            !metric.getDate().isAfter(endDate)
            );
        });
    }

    @Test
    @Sql("/test-data/metrics.sql")
    public void testAggregationMethods() {
        assertNotNull(metricsRepository.getTotalClicks());
        assertNotNull(metricsRepository.getTotalImpressions());
        assertNotNull(metricsRepository.getTotalCost());
        assertNotNull(metricsRepository.getAverageCtr());
        assertNotNull(metricsRepository.getAverageCpc());
    }
}
