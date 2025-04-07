package com.marketflow.strategy.util;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Утилитарный класс для выполнения различных расчетов над маркетинговыми метриками.
 */
@Slf4j
public class MetricsCalculator {

    /**
     * Расчет эффективности рекламного источника
     * @param ctr показатель CTR в процентах
     * @param cpc стоимость клика
     * @param clicks количество кликов
     * @param cost общая стоимость
     * @return рейтинг эффективности (от 0 до 1)
     */
    public double calculateEfficiencyRating(double ctr, double cpc, double clicks, double cost) {
        if (clicks == 0 || cost == 0) {
            return 0.0;
        }

        // Нормализованные значения (чем выше CTR и ниже CPC, тем лучше)
        double normalizedCtr = Math.min(ctr / 10.0, 1.0); // Предполагаем, что 10% CTR - это отлично
        double normalizedCpc = Math.max(0, 1.0 - (cpc / 100.0)); // Предполагаем, что 100 - это высокий CPC

        // Объем трафика влияет на надежность рейтинга
        double volumeFactor = Math.min(clicks / 1000.0, 1.0);

        // Итоговый рейтинг - взвешенная сумма факторов
        return (normalizedCtr * 0.6 + normalizedCpc * 0.3) * (0.7 + 0.3 * volumeFactor);
    }

    /**
     * Расчет тренда по временному ряду данных
     * @param values список значений метрики за последовательные периоды
     * @return коэффициент тренда (положительный - рост, отрицательный - падение)
     */
    public double calculateTrend(List<Double> values) {
        if (values == null || values.size() < 2) {
            return 0.0;
        }

        int n = values.size();

        // Расчет методом наименьших квадратов
        double sumX = 0.0;
        double sumY = 0.0;
        double sumXY = 0.0;
        double sumXX = 0.0;

        for (int i = 0; i < n; i++) {
            double x = i + 1; // Номер периода
            double y = values.get(i);

            sumX += x;
            sumY += y;
            sumXY += x * y;
            sumXX += x * x;
        }

        // Коэффициент наклона линии тренда
        double slope = (n * sumXY - sumX * sumY) / (n * sumXX - sumX * sumX);

        // Нормализация для получения значимого коэффициента
        double avgY = sumY / n;
        double normalizedSlope = avgY != 0 ? slope / avgY : slope;

        return normalizedSlope;
    }

    /**
     * Определение выбросов (аномалий) в данных с использованием метода IQR
     * @param values список значений для анализа
     * @return список индексов значений, определенных как выбросы
     */
    public List<Integer> detectOutliers(List<Double> values) {
        if (values == null || values.size() < 4) {
            return Collections.emptyList();
        }

        List<Integer> outlierIndices = new ArrayList<>();
        List<Double> sortedValues = new ArrayList<>(values);
        Collections.sort(sortedValues);

        // Расчет квартилей
        int n = sortedValues.size();
        double q1 = sortedValues.get(n / 4);
        double q3 = sortedValues.get((3 * n) / 4);

        // Межквартильный размах
        double iqr = q3 - q1;

        // Границы для определения выбросов
        double lowerBound = q1 - 1.5 * iqr;
        double upperBound = q3 + 1.5 * iqr;

        // Поиск выбросов
        for (int i = 0; i < values.size(); i++) {
            double value = values.get(i);
            if (value < lowerBound || value > upperBound) {
                outlierIndices.add(i);
            }
        }

        return outlierIndices;
    }

    /**
     * Расчет среднего значения списка
     * @param values список значений
     * @return среднее арифметическое
     */
    public double calculateMean(List<Double> values) {
        if (values == null || values.isEmpty()) {
            return 0.0;
        }

        double sum = 0.0;
        for (Double value : values) {
            sum += value;
        }

        return sum / values.size();
    }

    /**
     * Расчет стандартного отклонения
     * @param values список значений
     * @return стандартное отклонение
     */
    public double calculateStdDev(List<Double> values) {
        if (values == null || values.isEmpty()) {
            return 0.0;
        }

        double mean = calculateMean(values);
        double variance = 0.0;

        for (Double value : values) {
            variance += Math.pow(value - mean, 2);
        }

        return Math.sqrt(variance / values.size());
    }

    /**
     * Расчет коэффициента вариации
     * @param values список значений
     * @return коэффициент вариации
     */
    public double calculateCoefficientOfVariation(List<Double> values) {
        if (values == null || values.isEmpty()) {
            return 0.0;
        }

        double mean = calculateMean(values);
        if (mean == 0) {
            return 0.0;
        }

        double stdDev = calculateStdDev(values);

        return (stdDev / mean) * 100.0;
    }
}