package com.marketflow.repository;

import com.marketflow.model.report.Report;
import com.marketflow.model.report.ReportType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ReportRepository extends JpaRepository<Report, Long> {

    List<Report> findByReportType(ReportType reportType);

    List<Report> findByCreatedAtBetween(LocalDateTime start, LocalDateTime end);

    List<Report> findTop10ByOrderByCreatedAtDesc();

    List<Report> findByFileTypeOrderByCreatedAtDesc(String fileType);
}