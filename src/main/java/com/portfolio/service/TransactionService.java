package com.portfolio.service;

import com.portfolio.exception.HoldingNotFoundException;
import com.portfolio.exception.InsufficientHoldingsException;
import com.portfolio.model.Holding;
import com.portfolio.model.Transaction;
import com.portfolio.repository.HoldingRepository;
import com.portfolio.repository.PortfolioRepository;
import com.portfolio.repository.TransactionRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private PortfolioRepository portfolioRepository;

    @Autowired
    private HoldingRepository holdingRepository;

    public Transaction findById(Long id){
        return transactionRepository.findById(id).orElseThrow(() -> new RuntimeException());
    }

    public List<Transaction> findByPortfolioId(Long PortfolioId){
        return transactionRepository.findByPortfolioId(PortfolioId);

    }

    @Transactional
    public Transaction executeTransaction(Transaction transaction){

        if(transaction.getType().equals("BUY")){
            Optional<Holding> existingHolding = holdingRepository.findByPortfolioIdAndInstrumentName(transaction.getPortfolio()
                    .getId(), transaction.getInstrumentName());
            if(existingHolding.isPresent()){
                Holding holding = existingHolding.get();

                int oldQty = holding.getQuantity();
                double oldAvgPrice = holding.getAverageBuyPrice();
                int newQty = transaction.getQuantity();
                double newPrice = transaction.getPrice();
                double newAvgPrice =(oldQty * oldAvgPrice + newQty * newPrice) / (oldQty + newQty);
                holding.setQuantity(oldQty + newQty);
                holding.setAverageBuyPrice(newAvgPrice);
                holdingRepository.save(holding);

            }else{
                 Holding holding = new Holding();
                 holding.setPortfolio(transaction.getPortfolio());
                 holding.setInstrumentName(transaction.getInstrumentName());
                 holding.setQuantity(transaction.getQuantity());
                 holding.setAverageBuyPrice(transaction.getPrice());
                 holding.setCurrentPrice(transaction.getPrice());
                 holdingRepository.save(holding);
            }

        }else if (transaction.getType().equals("SELL")) {
            Holding holding = holdingRepository.findByPortfolioIdAndInstrumentName(transaction.getPortfolio().
                    getId(), transaction.getInstrumentName()).orElseThrow(() -> new HoldingNotFoundException("Holding not find with Id"));
            if(holding.getQuantity()< transaction.getQuantity()){
                throw new InsufficientHoldingsException("Holdings are insufficient");
            }else {
               int reduce = holding.getQuantity() - transaction.getQuantity();
               if(reduce == 0){
                 holdingRepository.delete(holding);
               }else{
                   holding.setQuantity(reduce);
                   holdingRepository.save(holding);
               }

            }
        }

        transaction.setTransactionDate(LocalDateTime.now());
        return transactionRepository.save(transaction);

    }

}
