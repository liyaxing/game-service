package com.vanguard.web;

import com.vanguard.Frequency;
import com.vanguard.dto.CSVFileDto;
import com.vanguard.dto.GameSaleDto;
import com.vanguard.entity.TotalSalesEntity;
import com.vanguard.service.GameService;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.time.LocalDate;
import java.util.List;

@RestController
public class GameController {
    private final Timer getGameSalesTimer;
    private final Timer getTotalSalesTimer;
    private final GameService gameService;

    public GameController(GameService gameService, MeterRegistry meterRegistry){
        this.gameService = gameService;
        this.getGameSalesTimer = meterRegistry.timer("getGameSales.requests");
        this.getTotalSalesTimer = meterRegistry.timer("getTotalSales.requests");
    }

    @PostMapping("/import")
    public ResponseEntity<CSVFileDto> uploadCSV(@RequestParam("file") MultipartFile file) {
        var dto = gameService.saveCsvFile(file);

        var path = ServletUriComponentsBuilder.fromCurrentRequestUri().path("/{id}").buildAndExpand(dto.getId()).toUriString();

        var headers = new HttpHeaders();
        headers.set("Location", path);

        return new ResponseEntity<>(dto, headers, HttpStatus.CREATED);
    }

    @GetMapping("/files/{fileId}")
    public ResponseEntity<CSVFileDto> getFileStatus(@PathVariable Long fileId) {
        var dto = gameService.getFileStatus(fileId);
        return new ResponseEntity<>(dto, HttpStatus.OK);

    }

    @GetMapping("/getGameSales")
    public ResponseEntity<PaginatedResponse<GameSaleDto>> getGameSales(LocalDate startDate, LocalDate endDate, @PageableDefault(value = 100) Pageable pageable, @RequestParam(name = "search", required = false) List<String> filters) {
        return getGameSalesTimer.record(() -> {
            var dtos = gameService.getGameSales(pageable, startDate, endDate, filters);
            return new ResponseEntity<>(dtos, HttpStatus.OK);
        });

    }

    @GetMapping("/getTotalSales")
    public ResponseEntity<List<TotalSalesEntity>> getTotalSales(@RequestParam LocalDate startDate, @RequestParam LocalDate endDate, Integer gameNo, @RequestParam(required = false, defaultValue = "DAILY") Frequency frequency) {
        return getTotalSalesTimer.record(() -> {
            var dtos = gameService.getTotalSales(startDate, endDate, gameNo, frequency);
            return new ResponseEntity<>(dtos, HttpStatus.OK);
        });
    }
}
