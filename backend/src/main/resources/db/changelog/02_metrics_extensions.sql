-- Добавляем новые поля к таблице metrics
ALTER TABLE metrics ADD COLUMN IF NOT EXISTS source VARCHAR(50);
ALTER TABLE metrics ADD COLUMN IF NOT EXISTS campaign_id VARCHAR(100);

-- Создаем индексы для ускорения поиска
CREATE INDEX IF NOT EXISTS idx_metrics_date ON metrics(date);
CREATE INDEX IF NOT EXISTS idx_metrics_source ON metrics(source);
CREATE INDEX IF NOT EXISTS idx_metrics_campaign_id ON metrics(campaign_id);

-- Создаем представление для агрегированных данных
CREATE OR REPLACE VIEW metrics_daily_summary AS
SELECT
    date,
    sum(clicks) as total_clicks,
    sum(impressions) as total_impressions,
    sum(cost) as total_cost,
    CASE
    WHEN sum(impressions) > 0 THEN sum(clicks)::float / sum(impressions) * 100
    ELSE 0
END as avg_ctr,
    CASE
        WHEN sum(clicks) > 0 THEN sum(cost) / sum(clicks)
        ELSE 0
END as avg_cpc
FROM
    metrics
GROUP BY
    date
ORDER BY
    date DESC;