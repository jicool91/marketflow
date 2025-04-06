package com.marketflow.service;

import com.marketflow.model.report.Report;
import com.marketflow.model.report.ReportDTO;
import com.marketflow.model.report.ReportType;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ReportService {

    List<ReportDTO> getAllReports();

    Optional<ReportDTO> getReportById(Long id);

    List<ReportDTO> getReportsByType(ReportType reportType);

    List<ReportDTO> getReportsByDateRange(LocalDateTime start, LocalDateTime end);

    ReportDTO createReport(Report report);

    void deleteReport(Long id);

    List<ReportDTO> getLatestReports(int count);

    byte[] downloadReport(Long id);
}