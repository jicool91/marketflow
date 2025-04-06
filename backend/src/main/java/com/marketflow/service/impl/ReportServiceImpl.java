package com.marketflow.service.impl;

import com.marketflow.model.report.Report;
import com.marketflow.model.report.ReportDTO;
import com.marketflow.model.report.ReportType;
import com.marketflow.repository.ReportRepository;
import com.marketflow.service.ReportService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReportServiceImpl implements ReportService {

    private final ReportRepository reportRepository;

    @Override
    public List<ReportDTO> getAllReports() {
        log.info("Fetching all reports");
        return reportRepository.findAll().stream()
                .map(ReportDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<ReportDTO> getReportById(Long id) {
        log.info("Fetching report with id: {}", id);
        return reportRepository.findById(id)
                .map(ReportDTO::fromEntity);
    }

    @Override
    public List<ReportDTO> getReportsByType(ReportType reportType) {
        log.info("Fetching reports with type: {}", reportType);
        return reportRepository.findByReportType(reportType).stream()
                .map(ReportDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public List<ReportDTO> getReportsByDateRange(LocalDateTime start, LocalDateTime end) {
        log.info("Fetching reports between {} and {}", start, end);
        return reportRepository.findByCreatedAtBetween(start, end).stream()
                .map(ReportDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ReportDTO createReport(Report report) {
        log.info("Creating new report: {}", report);
        if (report.getCreatedAt() == null) {
            report.setCreatedAt(LocalDateTime.now());
        }
        Report savedReport = reportRepository.save(report);
        return ReportDTO.fromEntity(savedReport);
    }

    @Override
    @Transactional
    public void deleteReport(Long id) {
        log.info("Deleting report with id: {}", id);
        Report report = reportRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Report not found with id: " + id));

        // Удаляем файл перед удалением записи из БД
        try {
            File file = new File(report.getFilePath());
            if (file.exists()) {
                file.delete();
            }
        } catch (Exception e) {
            log.error("Error deleting report file: {}", e.getMessage());
        }

        reportRepository.deleteById(id);
    }

    @Override
    public List<ReportDTO> getLatestReports(int count) {
        log.info("Fetching latest {} reports", count);
        return reportRepository.findTop10ByOrderByCreatedAtDesc().stream()
                .limit(count)
                .map(ReportDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public byte[] downloadReport(Long id) {
        log.info("Downloading report with id: {}", id);
        Report report = reportRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Report not found with id: " + id));

        try {
            return Files.readAllBytes(Paths.get(report.getFilePath()));
        } catch (IOException e) {
            log.error("Error reading report file: {}", e.getMessage());
            throw new RuntimeException("Could not read file", e);
        }
    }
}