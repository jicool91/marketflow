package com.marketflow.metrics.config;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class CollectorConfig {
    private String dbUrl;
    private String dbUser;
    private String dbPass;
    private List<String> platforms;

    // Конструктор по умолчанию
    public CollectorConfig() {
        this.platforms = Collections.emptyList();
    }

    /**
     * Создает конфигурацию на основе переменных окружения
     */
    public static CollectorConfig fromEnv() {
        CollectorConfig config = new CollectorConfig();

        // Database config
        config.setDbUrl(getEnv("DB_URL", "jdbc:postgresql://localhost:5432/marketflow"));
        config.setDbUser(getEnv("DB_USER", "mf_user"));
        config.setDbPass(getEnv("DB_PASS", "mf_pass"));

        // Platforms
        String platformsStr = getEnv("PLATFORMS", "yandex,google,vk");
        config.setPlatforms(Arrays.asList(platformsStr.split(",")));

        return config;
    }

    private static String getEnv(String key, String defaultValue) {
        String value = System.getenv(key);
        return (value != null && !value.isEmpty()) ? value : defaultValue;
    }

    // Геттеры и сеттеры

    public String getDbUrl() {
        return dbUrl;
    }

    public void setDbUrl(String dbUrl) {
        this.dbUrl = dbUrl;
    }

    public String getDbUser() {
        return dbUser;
    }

    public void setDbUser(String dbUser) {
        this.dbUser = dbUser;
    }

    public String getDbPass() {
        return dbPass;
    }

    public void setDbPass(String dbPass) {
        this.dbPass = dbPass;
    }

    public List<String> getPlatforms() {
        return platforms;
    }

    public void setPlatforms(List<String> platforms) {
        this.platforms = platforms;
    }

    @Override
    public String toString() {
        return "CollectorConfig{" +
                "dbUrl='" + dbUrl + '\'' +
                ", dbUser='" + dbUser + '\'' +
                ", dbPass='***'" +
                ", platforms=" + platforms +
                '}';
    }
}