-- Очистка таблицы перед вставкой тестовых данных
DELETE FROM metrics;

-- Тестовые данные для Яндекс
INSERT INTO metrics (date, clicks, impressions, cost, source, campaign_id)
VALUES
    (CURRENT_DATE - 1, 100, 1000, 50.0, 'yandex', 'yandex-camp-1'),
    (CURRENT_DATE - 2, 120, 1200, 60.0, 'yandex', 'yandex-camp-1'),
    (CURRENT_DATE - 3, 90, 900, 45.0, 'yandex', 'yandex-camp-2');

-- Тестовые данные для Google
INSERT INTO metrics (date, clicks, impressions, cost, source, campaign_id)
VALUES
    (CURRENT_DATE - 1, 150, 1500, 75.0, 'google', 'google-camp-1'),
    (CURRENT_DATE - 2, 160, 1600, 80.0, 'google', 'google-camp-1'),
    (CURRENT_DATE - 3, 140, 1400, 70.0, 'google', 'google-camp-2');

-- Тестовые данные для VK
INSERT INTO metrics (date, clicks, impressions, cost, source, campaign_id)
VALUES
    (CURRENT_DATE - 1, 80, 800, 40.0, 'vk', 'vk-camp-1'),
    (CURRENT_DATE - 2, 70, 700, 35.0, 'vk', 'vk-camp-1'),
    (CURRENT_DATE - 3, 60, 600, 30.0, 'vk', 'vk-camp-2');