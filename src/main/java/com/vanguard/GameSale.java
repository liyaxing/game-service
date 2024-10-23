package com.vanguard;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GameSale {
    @CsvBindByName(column = "id")
    private int id;

    @CsvBindByName(column = "game_no")
    private int gameNo;

    @CsvBindByName(column = "game_name")
    private String gameName;

    @CsvBindByName(column = "game_code")
    private String gameCode;

    @CsvBindByName(column = "type")
    private int type;

    @CsvBindByName(column = "cost_price")
    private BigDecimal costPrice;

    @CsvBindByName(column = "tax")
    private BigDecimal tax;

    @CsvBindByName(column = "sale_price")
    private BigDecimal salePrice;

    @CsvBindByName(column = "date_of_sale")
    @CsvDate("yyyy-MM-dd HH:mm:ss")
    private LocalDateTime dateOfSale;
}
