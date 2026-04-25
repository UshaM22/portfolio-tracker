package com.portfolio.controller;

import com.portfolio.model.Transaction;
import com.portfolio.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @PostMapping
    public ResponseEntity<Transaction> buyOrSell(@RequestBody Transaction transaction){
        return ResponseEntity.ok(transactionService.executeTransaction(transaction));
    }

    @GetMapping("/portfolio/{portfolioId}")
    public ResponseEntity<List<Transaction>> findAllByPortfolioId(@PathVariable Long portfolioId) {
        return ResponseEntity.ok(transactionService.findByPortfolioId(portfolioId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Transaction> findById(@PathVariable Long id) {
        return ResponseEntity.ok(transactionService.findById(id));
    }
}
