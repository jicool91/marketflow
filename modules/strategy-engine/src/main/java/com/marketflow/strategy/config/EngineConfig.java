package com.marketflow.strategy.config;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Класс конфигурации для модуля strategy-engine.
 * Загружает настройки из переменных окружения или файла application.properties.
 */
@Data
@Slf4j
public class EngineConfig {
    private String dbUrl;
    private String dbUsername;
    private String dbPassword;
    private int analysisDefaultPeriod = 30; // Период анализа в днях по умолчанию
    private boolean saveGeneratedStrategies = true; // Сохранять ли результаты в БД
    private String outputDirectory = "reports"; // Директория для выходных файлов

    // Планировщик
    private boolean schedulerEnabled = false;
    private String schedulerCron = "0 0 1 * * ?"; // Раз в день в 1:00 по умолчанию

    // Интеграция
    private boolean apiIntegrationEnabled = false;
    private String apiEndpoint;
    private String apiKey;

    /**
     * Создает экземпляр конфигурации с настройками по умолчанию
     */
    public EngineConfig() {
        // Пустой конструктор с настройками по умолчанию
    }

    /**
     * Загружает конфигурацию из переменных окружения и application.properties
     * @return инициализированный объект конфигурации
     */
    public static EngineConfig load() {
        EngineConfig config = new EngineConfig();

        // Сначала загружаем из файла properties
        config.loadFromProperties();

        // Затем переопределяем из переменных окружения (если они есть)
        config.loadFromEnvironment();

        log.info("Configuration loaded: {}", config);
        return config;
    }

    /**
     * Загружает конфигурацию из файла application.properties
     */
    private void loadFromProperties() {
        Properties properties = new Properties();

        try (InputStream input = getClass().getClassLoader().getResourceAsStream("application.properties")) {
            if (input != null) {
                properties.load(input);

                // База данных
                dbUrl = properties.getProperty("db.url", dbUrl);
                dbUsername = properties.getProperty("db.username", dbUsername);
                dbPassword = properties.getProperty("db.password", dbPassword);

                // Общие настройки
                analysisDefaultPeriod = Integer.parseInt(
                        properties.getProperty("analysis.default.period", String.valueOf(analysisDefaultPeriod)));
                saveGeneratedStrategies = Boolean.parseBoolean(
                        properties.getProperty("save.generated.strategies", String.valueOf(saveGeneratedStrategies)));
                outputDirectory = properties.getProperty("output.directory", outputDirectory);

                // Планировщик
                schedulerEnabled = Boolean.parseBoolean(
                        properties.getProperty("scheduler.enabled", String.valueOf(schedulerEnabled)));
                schedulerCron = properties.getProperty("scheduler.cron", schedulerCron);

                // Интеграция
                apiIntegrationEnabled = Boolean.parseBoolean(
                        properties.getProperty("api.integration.enabled", String.valueOf(apiIntegrationEnabled)));
                apiEndpoint = properties.getProperty("api.endpoint", apiEndpoint);
                apiKey = properties.getProperty("api.key", apiKey);

                log.debug("Configuration loaded from application.properties");
            } else {
                log.warn("application.properties file not found, using default values and environment variables");
            }
        } catch (IOException e) {
            log.error("Error loading application.properties", e);
        } catch (NumberFormatException e) {
            log.error("Error parsing numeric property", e);
        }
    }

    /**
     * Загружает конфигурацию из переменных окружения
     */
    private void loadFromEnvironment() {
        // База данных
        String envDbUrl = System.getenv("DB_URL");
        if (envDbUrl != null && !envDbUrl.isEmpty()) {
            dbUrl = envDbUrl;
        }

        String envDbUsername = System.getenv("DB_USERNAME");
        if (envDbUsername != null && !envDbUsername.isEmpty()) {
            dbUsername = envDbUsername;
        }

        String envDbPassword = System.getenv("DB_PASSWORD");
        if (envDbPassword != null && !envDbPassword.isEmpty()) {
            dbPassword = envDbPassword;
        }

        // Общие настройки
        String envPeriod = System.getenv("ANALYSIS_PERIOD");
        if (envPeriod != null && !envPeriod.isEmpty()) {
            try {
                analysisDefaultPeriod = Integer.parseInt(envPeriod);
            } catch (NumberFormatException e) {
                log.warn("Invalid ANALYSIS_PERIOD environment variable: {}", envPeriod);
            }
        }

        String envSaveStrategies = System.getenv("SAVE_STRATEGIES");
        if (envSaveStrategies != null && !envSaveStrategies.isEmpty()) {
            saveGeneratedStrategies = Boolean.parseBoolean(envSaveStrategies);
        }

        String envOutputDir = System.getenv("OUTPUT_DIRECTORY");
        if (envOutputDir != null && !envOutputDir.isEmpty()) {
            outputDirectory = envOutputDir;
        }

        // Планировщик
        String envSchedulerEnabled = System.getenv("SCHEDULER_ENABLED");
        if (envSchedulerEnabled != null && !envSchedulerEnabled.isEmpty()) {
            schedulerEnabled = Boolean.parseBoolean(envSchedulerEnabled);
        }

        String envSchedulerCron = System.getenv("SCHEDULER_CRON");
        if (envSchedulerCron != null && !envSchedulerCron.isEmpty()) {
            schedulerCron = envSchedulerCron;
        }

        // Интеграция
        String envApiEnabled = System.getenv("API_INTEGRATION_ENABLED");
        if (envApiEnabled != null && !envApiEnabled.isEmpty()) {
            apiIntegrationEnabled = Boolean.parseBoolean(envApiEnabled);
        }

        String envApiEndpoint = System.getenv("API_ENDPOINT");
        if (envApiEndpoint != null && !envApiEndpoint.isEmpty()) {
            apiEndpoint = envApiEndpoint;
        }

        String envApiKey = System.getenv("API_KEY");
        if (envApiKey != null && !envApiKey.isEmpty()) {
            apiKey = envApiKey;
        }

        log.debug("Configuration loaded from environment variables");
    }

    /**
     * Проверяет, валидна ли текущая конфигурация
     * @return true, если конфигурация валидна
     */
    public boolean isValid() {
        if (dbUrl == null || dbUrl.isEmpty()) {
            log.error("Database URL is missing");
            return false;
        }

        if (dbUsername == null || dbUsername.isEmpty()) {
            log.error("Database username is missing");
            return false;
        }

        if (dbPassword == null || dbPassword.isEmpty()) {
            log.error("Database password is missing");
            return false;
        }

        if (analysisDefaultPeriod <= 0) {
            log.error("Analysis period must be positive");
            return false;
        }

        return true;
    }

    @Override
    public String toString() {
        return "EngineConfig{" +
                "dbUrl='" + dbUrl + '\'' +
                ", dbUsername='" + dbUsername + '\'' +
                ", dbPassword='***'" +
                ", analysisDefaultPeriod=" + analysisDefaultPeriod +
                ", saveGeneratedStrategies=" + saveGeneratedStrategies +
                ", outputDirectory='" + outputDirectory + '\'' +
                ", schedulerEnabled=" + schedulerEnabled +
                ", schedulerCron='" + schedulerCron + '\'' +
                ", apiIntegrationEnabled=" + apiIntegrationEnabled +
                ", apiEndpoint='" + apiEndpoint + '\'' +
                ", apiKey='***'" +
                '}';
    }
}