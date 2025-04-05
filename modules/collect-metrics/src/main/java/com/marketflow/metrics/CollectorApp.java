package com.marketflow.metrics;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.time.Instant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CollectorApp {
    private static final Logger log = LoggerFactory.getLogger(CollectorApp.class);

    public static void main(String[] args) throws Exception {
        String dbUrl = System.getenv().getOrDefault("DB_URL", "jdbc:postgresql://localhost:5432/marketflow");
        String dbUser = System.getenv().getOrDefault("DB_USER", "postgres");
        String dbPass = System.getenv().getOrDefault("DB_PASS", "postgres");

        try (Connection conn = DriverManager.getConnection(dbUrl, dbUser, dbPass)) {
            PreparedStatement ps = conn.prepareStatement(
                    "INSERT INTO metrics(timestamp, value, type) VALUES (?, ?, ?)"
            );
            ps.setObject(1, Instant.now());
            ps.setDouble(2, Math.random() * 100);
            ps.setString(3, "clicks_fake");
            ps.executeUpdate();

            log.info("✅ Metric inserted successfully.");
        } catch (Exception e) {
            log.error("❌ Failed to insert metric", e);
        }
    }
}
