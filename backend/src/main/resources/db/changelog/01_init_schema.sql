-- Создание основных таблиц (можно скопировать логику из collect-metrics)

CREATE TABLE IF NOT EXISTS metrics (
                                       id SERIAL PRIMARY KEY,
                                       date DATE NOT NULL,
                                       clicks INTEGER,
                                       impressions INTEGER,
                                       cost NUMERIC,
                                       source VARCHAR(50),
    campaign_id VARCHAR(100)
    );

CREATE TABLE IF NOT EXISTS strategies (
                                          id SERIAL PRIMARY KEY,
                                          name TEXT,
                                          generated_at TIMESTAMP
);

CREATE TABLE IF NOT EXISTS report_log (
                                          id SERIAL PRIMARY KEY,
                                          file_path TEXT,
                                          created_at TIMESTAMP
);