package com.vanguard.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "game_sales")
public class GameSaleEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "row_id")
    private Integer rowId;

    @Column(name = "game_no")
    private Integer gameNo;

    @Column(name = "game_name", length = 20)
    private String gameName;

    @Column(name = "game_code", length = 5)
    private String gameCode;

    @Column(name = "type")
    private int type;

    @Column(name = "cost_price", precision = 5, scale = 2)
    private BigDecimal costPrice;

    @Column(name = "tax", precision = 3, scale = 2)
    private BigDecimal tax;

    @Column(name = "sale_price", precision = 5, scale = 2)
    private BigDecimal salePrice;

    @Column(name = "date_of_sale")
    private Instant dateOfSale;

    @ManyToOne
    @JoinColumn(name = "csv_file_id", foreignKey = @ForeignKey(name = "fk_csv_file"))
    private CSVFileEntity csvFile;
}
