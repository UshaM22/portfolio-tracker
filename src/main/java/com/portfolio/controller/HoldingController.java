package com.portfolio.controller;

import com.portfolio.model.Holding;
import com.portfolio.service.HoldingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/holdings")
public class HoldingController {

    @Autowired
    private HoldingService holdingService;

    @GetMapping("/portfolio/{portfolioId}")
    public ResponseEntity<List<Holding>> findByPortfolioId(@PathVariable Long portfolioId){
        return ResponseEntity.ok(holdingService.findByPortfolioId(portfolioId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Holding> findById(@PathVariable Long id){
        return ResponseEntity.ok(holdingService.findById(id));
    }

    @GetMapping("/portfolio/{portfolioId}/value")
    public ResponseEntity<Double> getPortfolioValue(@PathVariable Long portfolioId){
        return ResponseEntity.ok(holdingService.getCurrentPortfolioValue(portfolioId));
    }

}
