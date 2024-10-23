package com.vanguard.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TotalSalesEntity {
    @Id
    private String temporal;
    private Long gamesSold;
    private BigDecimal salesTotal;
}
