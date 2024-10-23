package com.vanguard.entity;

import com.vanguard.FileStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "csv_file")
public class CSVFileEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false)
    private Long id;

    @Column(name = "file_name", nullable = false)
    private String fileName;

    @Column(name = "file_path", nullable = false)
    private String filePath;

    @Column(name = "import_date", nullable = false)
    private Instant importDate;

    @Column(name = "processed_records")
    private Integer processedRecords;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private FileStatus status;

    @Column(name = "error_message")
    private String errorMessage;

    @Column(name = "last_updated")
    private Instant lastUpdated;
}
