package com.portfolio.batch;

import com.portfolio.model.Holding;
import com.portfolio.repository.HoldingRepository;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.data.RepositoryItemReader;
import org.springframework.batch.item.data.builder.RepositoryItemReaderBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Sort;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.Map;
import java.util.Random;

@Configuration
public class NavRecalculationJob {

    @Autowired
    private RedisTemplate<String, Double> redisTemplate;

    @Autowired
    private HoldingRepository holdingRepository;


    @Bean
    public RepositoryItemReader<Holding> holdingReader(){
        return new RepositoryItemReaderBuilder<Holding>()
                .name("holdingReader")
                .repository(holdingRepository)
                .methodName("findAll")
                .pageSize(10)
                .sorts(Map.of("id", Sort.Direction.ASC))
                .build();
    }

    @Bean
    public ItemProcessor<Holding, Holding> priceUpdateProcessor() {
        return holding -> {
            Random random = new Random();
            double currentPrice = holding.getCurrentPrice();
            double change = currentPrice * (random.nextDouble() * 0.1 - 0.05);
            double newPrice = currentPrice + change;
            holding.setCurrentPrice(newPrice);
            // redis cache
            redisTemplate.opsForValue().set("price:" + holding.getInstrumentName(), newPrice);
            return holding;
        };
    }

    @Bean
    public ItemWriter<Holding> holdingWriter() {
        return holdings -> holdingRepository.saveAll(holdings.getItems());
    }

    @Bean
    public Step navRecalculationStep(JobRepository jobRepository,
                                     PlatformTransactionManager transactionManager) {
        return new StepBuilder("navRecalculationStep", jobRepository)
                .<Holding, Holding>chunk(10, transactionManager)
                .reader(holdingReader())
                .processor(priceUpdateProcessor())
                .writer(holdingWriter())
                .build();
    }

    @Bean
    public Job navPriceUpdateJob(JobRepository jobRepository,
                                   Step navRecalculationStep) {
        return new JobBuilder("navPriceUpdateJob", jobRepository)
                .start(navRecalculationStep)
                .build();
    }
}

