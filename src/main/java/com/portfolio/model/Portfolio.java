package com.portfolio.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "portfolios")
public class Portfolio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @ManyToOne
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

}
