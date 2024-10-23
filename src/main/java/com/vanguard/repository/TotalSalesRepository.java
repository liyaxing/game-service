package com.vanguard.repository;

import com.vanguard.entity.TotalSalesEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface TotalSalesRepository extends JpaRepository<TotalSalesEntity, String> {

    @Query(value = """
            WITH RECURSIVE date_range AS (
                SELECT :startDate AS date
                UNION ALL
                SELECT DATE_ADD(date, INTERVAL 1 DAY)
                FROM date_range
                WHERE DATE_ADD(date, INTERVAL 1 DAY) <= :endDate
            )
            SELECT
                DATE(dr.date) AS temporal,
                COALESCE(COUNT(gs.id), 0) AS games_sold,
                COALESCE(SUM(gs.sale_price), 0) AS sales_total
            FROM
                date_range dr
            LEFT JOIN
                game_sales gs ON DATE(gs.date_of_sale) = dr.date
                AND (:gameId IS NULL OR gs.game_no = :gameId)
            GROUP BY
                temporal
            ORDER BY
                temporal;
            """,
            nativeQuery = true
    )
    List<TotalSalesEntity> getDailyTotal(LocalDate startDate, LocalDate endDate, Integer gameId);

    @Query(value = """
            WITH RECURSIVE date_range AS (
                SELECT :startDate AS date
                UNION ALL
                SELECT DATE_ADD(date, INTERVAL 1 WEEK)
                FROM date_range
                WHERE DATE_ADD(date, INTERVAL 1 WEEK) <= :endDate
            )
            SELECT
                YEARWEEK(dr.date, 1) AS temporal,
                COALESCE(COUNT(gs.id), 0) AS games_sold,
                COALESCE(SUM(gs.sale_price), 0) AS sales_total
            FROM
                date_range dr
            LEFT JOIN
                game_sales gs ON YEARWEEK(gs.date_of_sale, 1) = YEARWEEK(dr.date, 1)
                AND (:gameId IS NULL OR gs.game_no = :gameId)
            GROUP BY
                temporal
            ORDER BY
                temporal;
            """,
            nativeQuery = true
    )
    List<TotalSalesEntity> getWeeklyTotal(LocalDate startDate, LocalDate endDate, Integer gameId);

    @Query(value = """
            WITH RECURSIVE date_range AS (
                SELECT :startDate AS date
                UNION ALL
                SELECT DATE_ADD(date, INTERVAL 1 MONTH)
                FROM date_range
                WHERE DATE_ADD(date, INTERVAL 1 MONTH) <= :endDate
            )
            SELECT
            	DATE_FORMAT(dr.date, '%Y-%m') AS temporal,
                COALESCE(COUNT(gs.id), 0) AS games_sold,
                COALESCE(SUM(gs.sale_price), 0) AS sales_total
            FROM
                date_range dr
            LEFT JOIN
                game_sales gs ON (DATE_FORMAT(gs.date_of_sale, '%Y-%m') = DATE_FORMAT(dr.date, '%Y-%m'))
                AND (:gameId IS NULL OR gs.game_no = :gameId)
            GROUP BY
                temporal
            ORDER BY
                temporal;
            """,
            nativeQuery = true
    )
    List<TotalSalesEntity> getMonthlyTotal(LocalDate startDate, LocalDate endDate, Integer gameId);
}
