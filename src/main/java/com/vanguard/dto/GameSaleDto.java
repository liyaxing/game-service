package com.vanguard.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class GameSaleDto {
    private Long id;

    private int rowId;

    private int gameNo;

    private String gameName;

    private String gameCode;

    private int type;

    private BigDecimal costPrice;

    private BigDecimal tax;

    private BigDecimal salePrice;

    private LocalDateTime dateOfSale;

    private IdRef file;
}
