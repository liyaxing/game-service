package com.vanguard;

import com.opencsv.CSVReader;
import com.opencsv.bean.CsvToBeanBuilder;
import com.vanguard.entity.CSVFileEntity;
import com.vanguard.entity.GameSaleEntity;
import com.vanguard.exception.FileIOException;
import com.vanguard.repository.CSVFileRepository;
import com.vanguard.repository.GameSaleRepository;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
public class CsvFileImportListener {
    @Value("${batch.size}")
    private int batchSize;
    private static final int THREAD_COUNT = 10;
    private final CSVFileRepository csvFileRepository;
    private final GameSaleRepository gameSaleRepository;

    public CsvFileImportListener(CSVFileRepository csvFileRepository, GameSaleRepository gameSaleRepository) {
        this.csvFileRepository = csvFileRepository;
        this.gameSaleRepository = gameSaleRepository;
    }

    @Async
    @EventListener
    public void handleCsvFileImportEvent(CsvFileImportEvent event) {
        var fileId = event.getFileId();

        var metadata = csvFileRepository.findById(fileId).orElseThrow();

        metadata = updateProgress(metadata, 0, FileStatus.IN_PROGRESS);

        var path = Paths.get(event.getFilePath());
        int totalRecords = 0;
        try (ExecutorService executorService = Executors.newFixedThreadPool(THREAD_COUNT)) {
            try (BufferedReader reader = Files.newBufferedReader(path)) {

                var csvReader = new CSVReader(reader);
                var csvToBean = new CsvToBeanBuilder<GameSale>(csvReader)
                        .withType(GameSale.class)
                        .withIgnoreLeadingWhiteSpace(true)
                        .build();

                var rows = new ArrayList<GameSale>();
                for (GameSale gameSale : csvToBean) {
                    rows.add(gameSale);
                    ++totalRecords;

                    if (rows.size() == batchSize) {
                        var batch = new ArrayList<>(rows); // Create a new list for the batch
                        executorService.submit(() -> gameSaleRepository.saveAll(batch.stream().map(row -> toEntity(fileId, row)).toList()));
                        metadata = updateProgress(metadata, totalRecords, FileStatus.IN_PROGRESS);
                        rows.clear();
                    }
                }
                if (!rows.isEmpty()) {
                    var finalBatch = new ArrayList<>(rows); // Create a new list for the final batch
                    executorService.submit(() -> gameSaleRepository.saveAll(finalBatch.stream().map(row -> toEntity(fileId, row)).toList()));
                }

                metadata = updateProgress(metadata, totalRecords, FileStatus.COMPLETED);
            } catch (IOException e) {
                updateProgress(metadata, totalRecords, FileStatus.FAILED);
                throw new FileIOException("Error processing csv file.");
            }
        }
    }

    private CSVFileEntity updateProgress(CSVFileEntity entity, int processedRecords, FileStatus fileStatus) {
        entity.setStatus(fileStatus);
        entity.setProcessedRecords(processedRecords);
        entity.setLastUpdated(Instant.now());

        return csvFileRepository.save(entity);
    }

    private GameSaleEntity toEntity(Long fileId, GameSale row) {
        return GameSaleEntity.builder()
                .csvFile(csvFileRepository.getReferenceById(fileId))
                .rowId(row.getId())
                .gameNo(row.getGameNo())
                .gameName(row.getGameName())
                .gameCode(row.getGameCode())
                .type(row.getType())
                .costPrice(row.getCostPrice())
                .tax(row.getTax())
                .salePrice(row.getSalePrice())
                .dateOfSale(row.getDateOfSale().toInstant(ZoneOffset.UTC))
                .build();
    }
}
