package com.portfolio.service;

import com.portfolio.exception.HoldingNotFoundException;
import com.portfolio.model.Holding;
import com.portfolio.repository.HoldingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HoldingService {

    @Autowired
    private HoldingRepository holdingRepository;

    public List<Holding> findByPortfolioId(Long portfolioId){
        return holdingRepository.findByPortfolioId(portfolioId);
    }

    public Holding findById(Long id){
        return holdingRepository.findById(id).orElseThrow(() -> new HoldingNotFoundException("Holding not find with id" +id));
    }

    public double getCurrentPortfolioValue(Long portfolioId) {
        List<Holding> holdings = holdingRepository.findByPortfolioId(portfolioId);

        return holdings.stream()
                .mapToDouble(h -> h.getQuantity() * h.getCurrentPrice())
                .sum();
    }
}
