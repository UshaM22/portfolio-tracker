package com.portfolio.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "transactions")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "portfolio_id", nullable = false)
    private Portfolio portfolio;

    @Column(nullable = false)
    private String instrumentName;

    @Column(nullable = false)
    private String type;

    @Column(nullable = false)
    private int quantity;

    @Column(nullable = false)
    private double price;

    @Column
    private LocalDateTime transactionDate;
}
