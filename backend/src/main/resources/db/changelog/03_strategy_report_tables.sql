-- Изменение таблицы стратегий
ALTER TABLE strategies ADD COLUMN IF NOT EXISTS description TEXT;
ALTER TABLE strategies ADD COLUMN IF NOT EXISTS source VARCHAR(50);
ALTER TABLE strategies ADD COLUMN IF NOT EXISTS recommendations TEXT;
ALTER TABLE strategies ADD COLUMN IF NOT EXISTS status VARCHAR(20);
ALTER TABLE strategies ADD COLUMN IF NOT EXISTS metrics_period INTEGER;

-- Изменение таблицы отчетов
ALTER TABLE report_log ADD COLUMN IF NOT EXISTS title VARCHAR(255);
ALTER TABLE report_log ADD COLUMN IF NOT EXISTS file_type VARCHAR(20);
ALTER TABLE report_log ADD COLUMN IF NOT EXISTS size_bytes BIGINT;
ALTER TABLE report_log ADD COLUMN IF NOT EXISTS report_type VARCHAR(20);
ALTER TABLE report_log ADD COLUMN IF NOT EXISTS parameters TEXT;

-- Индексы для ускорения поиска
CREATE INDEX IF NOT EXISTS idx_strategies_status ON strategies(status);
CREATE INDEX IF NOT EXISTS idx_strategies_source ON strategies(source);
CREATE INDEX IF NOT EXISTS idx_strategies_generated_at ON strategies(generated_at);

CREATE INDEX IF NOT EXISTS idx_report_log_created_at ON report_log(created_at);
CREATE INDEX IF NOT EXISTS idx_report_log_report_type ON report_log(report_type);