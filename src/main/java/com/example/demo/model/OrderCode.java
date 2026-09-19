package com.example.demo.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "order_code")
public class OrderCode {

    @Id
    @Column(name = "code_date")
    private LocalDate codeDate;

    @Column(name = "last_code", nullable = false)
    private long lastCode;

}