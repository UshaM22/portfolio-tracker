package com.portfolio.batch;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@EnableScheduling
public class NavRecalculationScheduler {

    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    private Job navPriceUpdateJob;

    @Scheduled(cron = "0 0 0 * * *") //runs every night at midnight
    public void runNavRecalculation() throws Exception{
        JobParameters params = new JobParametersBuilder().addLong("time", System.currentTimeMillis()).toJobParameters();

        jobLauncher.run(navPriceUpdateJob, params);
    }

}
