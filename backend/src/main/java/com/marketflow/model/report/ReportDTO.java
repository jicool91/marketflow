package com.marketflow.model.report;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReportDTO {
    private Long id;
    private String title;
    private String filePath;
    private String fileType;
    private Long sizeBytes;
    private LocalDateTime createdAt;
    private ReportType reportType;
    private String parameters;

    public static ReportDTO fromEntity(Report report) {
        return ReportDTO.builder()
                .id(report.getId())
                .title(report.getTitle())
                .filePath(report.getFilePath())
                .fileType(report.getFileType())
                .sizeBytes(report.getSizeBytes())
                .createdAt(report.getCreatedAt())
                .reportType(report.getReportType())
                .parameters(report.getParameters())
                .build();
    }
}