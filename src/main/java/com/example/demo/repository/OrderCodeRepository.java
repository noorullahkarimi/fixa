package com.example.demo.repository;

import com.example.demo.model.OrderCode;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;

public interface OrderCodeRepository
        extends JpaRepository<OrderCode, LocalDate> {

    @Modifying
    @Transactional
    @Query(value = """
        MERGE INTO order_code (code_date, last_code)
        KEY (code_date)
        VALUES (:date, COALESCE(
            (SELECT last_code + 1
             FROM order_code
             WHERE code_date = :date),
            1
        ))
        """, nativeQuery = true)
    int incrementSequence(@Param("date") LocalDate date);

    @Query("""
        SELECT o.lastCode
        FROM OrderCode o
        WHERE o.codeDate = :date
        """)
    Long findLastCode(@Param("date") LocalDate date);
}
