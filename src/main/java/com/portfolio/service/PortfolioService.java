package com.portfolio.service;

import com.portfolio.exception.PortfolioNotFoundException;
import com.portfolio.model.Portfolio;
import com.portfolio.repository.PortfolioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PortfolioService {

    @Autowired
    private PortfolioRepository portfolioRepository;

    public Portfolio save(Portfolio portfolio){
        return portfolioRepository.save(portfolio);
    }

    public Portfolio findById(Long id){
        return portfolioRepository.findById(id).orElseThrow(() -> new PortfolioNotFoundException("Portfolio not found with id" +id));
    }

    public List<Portfolio> findAll(){
        return portfolioRepository.findAll();
    }

    public List<Portfolio> findByClientId(Long clientId){
        return portfolioRepository.findByClientId(clientId);
    }

    public Portfolio update(Long id, Portfolio portfolio){
        Portfolio existing = findById(id);
        existing.setName(portfolio.getName());
        existing.setClient(portfolio.getClient());
        return portfolioRepository.save(existing);
    }

    public void delete(Long id){
        Portfolio portfolio = portfolioRepository.findById(id).orElseThrow(() -> new PortfolioNotFoundException("Portfolio not found with id" +id));
        portfolioRepository.deleteById(id);
    }
}
