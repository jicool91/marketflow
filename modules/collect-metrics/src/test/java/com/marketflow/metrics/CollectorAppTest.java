package com.marketflow.metrics;

import com.marketflow.metrics.config.CollectorConfig;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class CollectorAppTest {

    @Test
    public void testConfigFromEnv() {
        // Тест для проверки создания конфигурации из переменных окружения
        CollectorConfig config = CollectorConfig.fromEnv();

        assertNotNull(config);
        assertNotNull(config.getDbUrl());
        assertNotNull(config.getDbUser());
        assertNotNull(config.getDbPass());
        assertNotNull(config.getPlatforms());
    }

    @Test
    public void testCollectorConfig() {
        // Простая проверка конфигурации
        CollectorConfig config = new CollectorConfig();
        config.setDbUrl("jdbc:postgresql://localhost:5432/test");
        config.setDbUser("test_user");
        config.setDbPass("test_pass");
        config.setPlatforms(Arrays.asList("yandex", "google"));

        assertEquals("jdbc:postgresql://localhost:5432/test", config.getDbUrl());
        assertEquals("test_user", config.getDbUser());
        assertEquals("test_pass", config.getDbPass());
        assertEquals(2, config.getPlatforms().size());
        assertEquals("yandex", config.getPlatforms().get(0));
        assertEquals("google", config.getPlatforms().get(1));
    }
}