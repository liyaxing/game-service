package com.vanguard.repository;

import com.vanguard.entity.CSVFileEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CSVFileRepository extends JpaRepository<CSVFileEntity, Long> {
}
