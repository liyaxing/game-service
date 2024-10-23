package com.vanguard.service;

import com.vanguard.CsvFileImportEvent;
import com.vanguard.FileStatus;
import com.vanguard.Frequency;
import com.vanguard.dto.CSVFileDto;
import com.vanguard.dto.GameSaleDto;
import com.vanguard.dto.IdRef;
import com.vanguard.entity.CSVFileEntity;
import com.vanguard.entity.GameSaleEntity;
import com.vanguard.entity.TotalSalesEntity;
import com.vanguard.exception.FileValidationException;
import com.vanguard.exception.ResourceNotFoundException;
import com.vanguard.repository.CSVFileRepository;
import com.vanguard.repository.GameSaleRepository;
import com.vanguard.repository.TotalSalesRepository;
import com.vanguard.web.PaginatedResponse;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.*;
import java.util.List;

@Service
public class GameService {
    private final GameSaleRepository gameSaleRepository;
    private final TotalSalesRepository totalSalesRepository;
    private final CSVFileRepository csvFileRepository;
    private final FileStorageService fileStorageService;
    private final ApplicationEventPublisher eventPublisher;


    public GameService(GameSaleRepository repository, TotalSalesRepository totalSalesRepository, CSVFileRepository csvFileRepository, FileStorageService fileStorageService, ApplicationEventPublisher eventPublisher) {
        this.gameSaleRepository = repository;
        this.totalSalesRepository = totalSalesRepository;
        this.csvFileRepository = csvFileRepository;
        this.fileStorageService = fileStorageService;
        this.eventPublisher = eventPublisher;
    }

    public CSVFileDto saveCsvFile(MultipartFile file) {
        if (file.isEmpty()) {
            throw new FileValidationException("The uploaded file is empty");
        }

        var path = fileStorageService.store(file);
        var name = file.getOriginalFilename();
        var entity = createFileMetadata(path, name);

        CsvFileImportEvent event = new CsvFileImportEvent(this, entity.getId(), path);

        eventPublisher.publishEvent(event);

        return toDto(entity);
    }

    private CSVFileEntity createFileMetadata(String path, String name) {
        var now = Instant.now();

        var entity = CSVFileEntity.builder()
                .filePath(path)
                .fileName(name)
                .processedRecords(0)
                .importDate(now)
                .lastUpdated(now)
                .status(FileStatus.PENDING)
                .build();

        return csvFileRepository.save(entity);
    }

    public PaginatedResponse<GameSaleDto> getGameSales(Pageable pageable, LocalDate startDate, LocalDate endDate, List<String> filters) {
        var spec = Specification.<GameSaleEntity>where(null);

        if (startDate != null && endDate != null) {
            var startTime = startDate.atStartOfDay().toInstant(ZoneOffset.UTC);
            var endTime = endDate.atTime(LocalTime.MAX).toInstant(ZoneOffset.UTC);

            spec = spec.and(GameSaleRepository.Specs.byTimeBetween(startTime, endTime));
        }

        if (filters != null && !filters.isEmpty()) {
            spec = spec.and(GameSaleRepository.Specs.bySearchFilters(filters));
        }

        var entities = gameSaleRepository.findAll(spec, pageable);

        var dtos = entities.stream().map(this::toDto).toList();
        var page = new PaginatedResponse.Page(entities.getSize(), entities.getNumber(), entities.getTotalPages(), entities.getTotalElements());

        return new PaginatedResponse<>(dtos, page);
    }

    public List<TotalSalesEntity> getTotalSales(LocalDate startDate, LocalDate endDate, Integer gameId, Frequency frequency) {
        return switch (frequency) {
            case DAILY -> totalSalesRepository.getDailyTotal(startDate, endDate, gameId);
            case WEEKLY -> totalSalesRepository.getWeeklyTotal(startDate, endDate, gameId);
            case MONTHLY -> totalSalesRepository.getMonthlyTotal(startDate, endDate, gameId);
        };
    }

    public CSVFileDto getFileStatus(Long fileId) {
        var entity = csvFileRepository.findById(fileId).orElseThrow(ResourceNotFoundException::new);
        return toDto(entity);
    }

    private GameSaleDto toDto(GameSaleEntity entity) {
        return GameSaleDto.builder()
                .id(entity.getId())
                .rowId(entity.getRowId())
                .gameNo(entity.getGameNo())
                .gameName(entity.getGameName())
                .gameCode(entity.getGameCode())
                .type(entity.getType())
                .costPrice(entity.getCostPrice())
                .tax(entity.getTax())
                .salePrice(entity.getSalePrice())
                .dateOfSale(LocalDateTime.ofInstant(entity.getDateOfSale(), ZoneOffset.UTC))
                .file(new IdRef(entity.getCsvFile().getId()))
                .build();
    }

    private CSVFileDto toDto(CSVFileEntity entity) {
        return CSVFileDto.builder()
                .id(entity.getId())
                .importDate(LocalDateTime.ofInstant(entity.getImportDate(), ZoneOffset.UTC))
                .fileName(entity.getFileName())
                .processedRecords(entity.getProcessedRecords())
                .status(entity.getStatus())
                .errorMessage(entity.getErrorMessage())
                .lastUpdated(LocalDateTime.ofInstant(entity.getLastUpdated(), ZoneOffset.UTC))
                .build();
    }
}
