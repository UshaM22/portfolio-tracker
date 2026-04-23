package com.portfolio.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "holdings")
@Data
public class Holding {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "portfolio_id", nullable = false)
    private Portfolio portfolio;

    @Column(nullable = false)
    private String instrumentName;

    @Column(nullable = false)
    private int quantity;

    @Column(nullable = false)
    private double averageBuyPrice;

    @Column
    private double currentPrice;
}
