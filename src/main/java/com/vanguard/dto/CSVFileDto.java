package com.vanguard.dto;

import com.vanguard.FileStatus;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class CSVFileDto {
    private Long id;

    private String fileName;

    private LocalDateTime importDate;

    private Integer processedRecords;

    private FileStatus status;

    private String errorMessage;

    private LocalDateTime lastUpdated;
}
