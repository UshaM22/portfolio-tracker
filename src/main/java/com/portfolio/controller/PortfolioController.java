package com.portfolio.controller;

import com.portfolio.model.Portfolio;
import com.portfolio.service.PortfolioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("/api/portfolios")
public class PortfolioController {

    @Autowired
    private PortfolioService portfolioService;

    @PostMapping
    public ResponseEntity<Portfolio> save(@RequestBody Portfolio portfolio){
        return ResponseEntity.ok(portfolioService.save(portfolio));
    }
    @GetMapping
    public ResponseEntity<List<Portfolio>> findAll(){
        return ResponseEntity.ok(portfolioService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Portfolio> findById(@PathVariable Long id){
        return ResponseEntity.ok(portfolioService.findById(id));
    }
    
    @GetMapping("/client/{clientId}")
    public ResponseEntity<List<Portfolio>> findByClientId(@PathVariable Long clientId){
        return ResponseEntity.ok(portfolioService.findByClientId(clientId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Portfolio> update(@PathVariable Long id, @RequestBody Portfolio portfolio){
        return ResponseEntity.ok(portfolioService.update(id,portfolio));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePortfolio(@PathVariable Long id){
       portfolioService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
