package com.marketflow.controller;

import com.marketflow.model.report.Report;
import com.marketflow.model.report.ReportDTO;
import com.marketflow.model.report.ReportType;
import com.marketflow.service.ReportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/report")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Report API", description = "Операции с отчетами")
public class ReportController {

    private final ReportService reportService;

    @GetMapping
    @Operation(summary = "Получить все отчеты")
    public ResponseEntity<List<ReportDTO>> getAllReports() {
        log.info("REST request to get all reports");
        return ResponseEntity.ok(reportService.getAllReports());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получить отчет по ID")
    public ResponseEntity<ReportDTO> getReportById(@PathVariable Long id) {
        log.info("REST request to get report with id: {}", id);
        return reportService.getReportById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/type/{type}")
    @Operation(summary = "Получить отчеты по типу")
    public ResponseEntity<List<ReportDTO>> getReportsByType(@PathVariable ReportType type) {
        log.info("REST request to get reports with type: {}", type);
        return ResponseEntity.ok(reportService.getReportsByType(type));
    }

    @GetMapping("/date-range")
    @Operation(summary = "Получить отчеты в диапазоне дат")
    public ResponseEntity<List<ReportDTO>> getReportsByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {
        log.info("REST request to get reports between {} and {}", start, end);
        return ResponseEntity.ok(reportService.getReportsByDateRange(start, end));
    }

    @PostMapping
    @Operation(summary = "Создать новый отчет")
    public ResponseEntity<ReportDTO> createReport(@RequestBody Report report) {
        log.info("REST request to save report: {}", report);
        return new ResponseEntity<>(reportService.createReport(report), HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Удалить отчет")
    public ResponseEntity<Void> deleteReport(@PathVariable Long id) {
        log.info("REST request to delete report with id: {}", id);
        reportService.deleteReport(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/latest/{count}")
    @Operation(summary = "Получить последние отчеты")
    public ResponseEntity<List<ReportDTO>> getLatestReports(@PathVariable int count) {
        log.info("REST request to get latest {} reports", count);
        return ResponseEntity.ok(reportService.getLatestReports(count));
    }

    @GetMapping("/download/{id}")
    @Operation(summary = "Скачать отчет")
    public ResponseEntity<byte[]> downloadReport(@PathVariable Long id) {
        log.info("REST request to download report with id: {}", id);

        ReportDTO reportDTO = reportService.getReportById(id)
                .orElseThrow(() -> new RuntimeException("Report not found"));

        byte[] reportBytes = reportService.downloadReport(id);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("attachment", reportDTO.getTitle() + ".pdf");
        headers.setContentLength(reportBytes.length);

        return new ResponseEntity<>(reportBytes, headers, HttpStatus.OK);
    }
}