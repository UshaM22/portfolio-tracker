package com.portfolio.service;

import com.portfolio.exception.HoldingNotFoundException;
import com.portfolio.model.Holding;
import com.portfolio.repository.HoldingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HoldingService {

    @Autowired
    private HoldingRepository holdingRepository;

    @Autowired
    private RedisTemplate<String, Double> redisTemplate;

    public List<Holding> findByPortfolioId(Long portfolioId){
        return holdingRepository.findByPortfolioId(portfolioId);
    }

    public Holding findById(Long id){
        return holdingRepository.findById(id).orElseThrow(() -> new HoldingNotFoundException("Holding not find with id" +id));
    }

//    public double getCurrentPortfolioValue(Long portfolioId) {
//        List<Holding> holdings = holdingRepository.findByPortfolioId(portfolioId);
//
//        return holdings.stream()
//                .mapToDouble(h -> h.getQuantity() * h.getCurrentPrice())
//                .sum();
//    }
//
     public double getCurrentPortfolioValue(Long portfolioId) {
        return holdingRepository.findByPortfolioId(portfolioId)
                .stream()
                .mapToDouble(h -> {
                    String key = "price:" + h.getInstrumentName();
                    Double cachedPrice = (Double) redisTemplate.opsForValue().get(key);
                    if (cachedPrice != null) {
                        return h.getQuantity() * cachedPrice;
                    }
                    // not in cache — use database price and cache it
                    redisTemplate.opsForValue().set(key, h.getCurrentPrice());
                    return h.getQuantity() * h.getCurrentPrice();
                })
                .sum();
    }
}
