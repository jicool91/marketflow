package com.marketflow.strategy.util;

import com.marketflow.strategy.model.StrategyType;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Движок генерации рекомендаций на основе анализа метрик.
 */
@Slf4j
public class RecommendationEngine {

    /**
     * Генерация списка рекомендаций на основе метрик и выбранного типа стратегии
     * @param source источник (рекламная платформа)
     * @param strategyType тип стратегии
     * @param metrics карта метрик
     * @param anomalies список обнаруженных аномалий
     * @return список рекомендаций
     */
    public List<String> generateRecommendations(
            String source, StrategyType strategyType,
            Map<String, Double> metrics, List<String> anomalies) {

        List<String> recommendations = new ArrayList<>();

        // Добавление базовых рекомендаций в зависимости от типа стратегии
        switch (strategyType) {
            case CONVERSION_OPTIMIZATION:
                addConversionOptimizationRecommendations(recommendations, source, metrics);
                break;
            case REACH_EXPANSION:
                addReachExpansionRecommendations(recommendations, source, metrics);
                break;
            case COST_REDUCTION:
                addCostReductionRecommendations(recommendations, source, metrics);
                break;
            case RETARGETING:
                addRetargetingRecommendations(recommendations, source, metrics);
                break;
            case AUDIENCE_SEGMENTATION:
                addAudienceSegmentationRecommendations(recommendations, source, metrics);
                break;
            case CROSS_PLATFORM:
                addCrossPlatformRecommendations(recommendations, source, metrics);
                break;
            case SEASONAL:
                addSeasonalRecommendations(recommendations, source, metrics);
                break;
            case COMPETITIVE:
                addCompetitiveRecommendations(recommendations, source, metrics);
                break;
            default:
                recommendations.add("Проведите общий аудит рекламных кампаний на платформе " + source);
        }

        // Добавление рекомендаций на основе трендов
        Double ctrTrend = metrics.getOrDefault("ctr_trend", 0.0);
        Double cpcTrend = metrics.getOrDefault("cpc_trend", 0.0);

        addTrendBasedRecommendations(recommendations, source, ctrTrend, cpcTrend);

        // Добавление рекомендаций на основе аномалий
        if (!anomalies.isEmpty()) {
            recommendations.add("Обратите внимание на обнаруженные аномалии в метриках:");
            for (int i = 0; i < Math.min(3, anomalies.size()); i++) {
                recommendations.add("- " + anomalies.get(i));
            }

            if (anomalies.size() > 3) {
                recommendations.add("- И еще " + (anomalies.size() - 3) + " аномалий. Проведите детальный анализ.");
            }
        }

        log.info("Generated {} recommendations for source {} with strategy type {}",
                recommendations.size(), source, strategyType);

        return recommendations;
    }

    /**
     * Добавление рекомендаций для стратегии оптимизации конверсии
     */
    private void addConversionOptimizationRecommendations(
            List<String> recommendations, String source, Map<String, Double> metrics) {

        recommendations.add("Оптимизируйте целевые страницы для увеличения конверсии в " + source);
        recommendations.add("Проведите A/B тестирование рекламных объявлений для выявления наиболее эффективных вариантов");
        recommendations.add("Улучшите таргетинг на более конверсионные сегменты аудитории");
        recommendations.add("Пересмотрите ключевые слова, фокусируясь на тех, которые приносят более качественный трафик");
        recommendations.add("Внедрите ретаргетинг для возврата посетителей, которые не совершили целевое действие");
    }

    /**
     * Добавление рекомендаций для стратегии расширения охвата
     */
    private void addReachExpansionRecommendations(
            List<String> recommendations, String source, Map<String, Double> metrics) {

        recommendations.add("Расширьте семантическое ядро с фокусом на поисковые запросы с высоким потенциалом охвата");
        recommendations.add("Увеличьте бюджет для наиболее успешных кампаний в " + source);
        recommendations.add("Разработайте новые рекламные креативы для привлечения внимания новых аудиторий");
        recommendations.add("Исследуйте смежные сегменты аудитории для расширения охвата рекламной кампании");
        recommendations.add("Используйте автоматические стратегии для максимизации охвата с оптимальным бюджетом");
    }

    /**
     * Добавление рекомендаций для стратегии снижения стоимости
     */
    private void addCostReductionRecommendations(
            List<String> recommendations, String source, Map<String, Double> metrics) {

        recommendations.add("Оптимизируйте ставки для снижения средней стоимости клика в " + source);
        recommendations.add("Исключите неэффективные ключевые слова с высокой стоимостью клика и низкой конверсией");
        recommendations.add("Скорректируйте время показа объявлений для фокусировки на периоды с наименьшей конкуренцией");
        recommendations.add("Улучшите показатель качества объявлений для снижения необходимой ставки");
        recommendations.add("Перераспределите бюджет от кампаний с высоким CPC к кампаниям с более низким CPC");
    }

    /**
     * Добавление рекомендаций для стратегии ретаргетинга
     */
    private void addRetargetingRecommendations(
            List<String> recommendations, String source, Map<String, Double> metrics) {

        recommendations.add("Настройте сегменты ретаргетинга по глубине просмотра сайта и интересу к продукту");
        recommendations.add("Создайте специальные предложения для возврата пользователей, которые добавили товар в корзину, но не завершили покупку");
        recommendations.add("Настройте цепочки email-маркетинга для комплексного воздействия на аудиторию ретаргетинга");
        recommendations.add("Ограничьте частоту показа ретаргетинговых объявлений для предотвращения баннерной слепоты");
        recommendations.add("Используйте динамический ретаргетинг для показа пользователям просмотренных ими товаров");
    }

    /**
     * Добавление рекомендаций для стратегии сегментации аудитории
     */
    private void addAudienceSegmentationRecommendations(
            List<String> recommendations, String source, Map<String, Double> metrics) {

        recommendations.add("Разделите рекламные кампании по демографическим сегментам для более точного таргетинга");
        recommendations.add("Создайте отдельные стратегии для новых и существующих клиентов");
        recommendations.add("Адаптируйте рекламные сообщения под различные сегменты аудитории");
        recommendations.add("Проанализируйте поведенческие паттерны целевой аудитории для более точной сегментации");
        recommendations.add("Используйте функции Look-alike аудиторий для расширения эффективных сегментов");
    }

    /**
     * Добавление рекомендаций для кросс-платформенной стратегии
     */
    private void addCrossPlatformRecommendations(
            List<String> recommendations, String source, Map<String, Double> metrics) {

        recommendations.add("Создайте единую стратегию коммуникации на всех рекламных платформах");
        recommendations.add("Адаптируйте креативы под особенности каждой рекламной платформы, сохраняя общий стиль");
        recommendations.add("Внедрите кросс-канальное отслеживание конверсий для более точного анализа эффективности");
        recommendations.add("Распределите бюджет между платформами на основе эффективности каждого канала");
        recommendations.add("Координируйте время публикации рекламы на разных площадках для усиления общего эффекта");
    }

    /**
     * Добавление рекомендаций для сезонной стратегии
     */
    private void addSeasonalRecommendations(
            List<String> recommendations, String source, Map<String, Double> metrics) {

        recommendations.add("Подготовьте заранее сезонные рекламные кампании с учетом пиковых периодов спроса");
        recommendations.add("Увеличьте бюджет перед началом сезонного спроса для захвата максимальной доли рынка");
        recommendations.add("Разработайте специальные сезонные предложения и акции");
        recommendations.add("Адаптируйте ключевые слова под сезонные запросы и интересы аудитории");
        recommendations.add("Проанализируйте данные прошлых сезонов для оптимизации текущей стратегии");
    }

    /**
     * Добавление рекомендаций для конкурентной стратегии
     */
    private void addCompetitiveRecommendations(
            List<String> recommendations, String source, Map<String, Double> metrics) {

        recommendations.add("Проведите анализ рекламных кампаний конкурентов для выявления их сильных и слабых сторон");
        recommendations.add("Рассмотрите возможность использования брендовых запросов конкурентов для привлечения их аудитории");
        recommendations.add("Подчеркните в рекламных сообщениях уникальные преимущества вашего предложения по сравнению с конкурентами");
        recommendations.add("Мониторьте ценовую политику конкурентов для своевременной корректировки собственных предложений");
        recommendations.add("Создайте контент, сравнивающий ваш продукт с конкурентами, подчеркивая ваши преимущества");
    }

    /**
     * Добавление рекомендаций на основе трендов метрик
     */
    private void addTrendBasedRecommendations(
            List<String> recommendations, String source, Double ctrTrend, Double cpcTrend) {

        // Рекомендации на основе тренда CTR
        if (ctrTrend < -0.1) {
            recommendations.add("Наблюдается негативный тренд CTR. Рекомендуется обновить креативы и пересмотреть таргетинг аудитории.");
        } else if (ctrTrend > 0.1) {
            recommendations.add("Отмечен положительный тренд CTR. Рекомендуется увеличить бюджет на успешные кампании для масштабирования результатов.");
        }

        // Рекомендации на основе тренда CPC
        if (cpcTrend > 0.1) {
            recommendations.add("Наблюдается рост стоимости клика. Рекомендуется оптимизировать ставки и улучшить показатель качества объявлений.");
        } else if (cpcTrend < -0.1) {
            recommendations.add("Отмечено снижение стоимости клика. Рекомендуется использовать этот момент для расширения охвата рекламной кампании.");
        }

        // Комбинированные рекомендации
        if (ctrTrend < -0.05 && cpcTrend > 0.05) {
            recommendations.add("Критическая ситуация: снижение CTR при росте CPC. Необходим срочный пересмотр стратегии рекламной кампании.");
        } else if (ctrTrend > 0.05 && cpcTrend < -0.05) {
            recommendations.add("Идеальная ситуация: рост CTR при снижении CPC. Рекомендуется максимально использовать этот период для масштабирования.");
        }
    }
}