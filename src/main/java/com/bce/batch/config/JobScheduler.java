package com.bce.batch.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class JobScheduler {

	private static final Logger logger = LoggerFactory.getLogger(JobScheduler.class);

	@Autowired
	private JobLauncher jobLauncher;

	@Autowired
	private Job jobwithFilePartictionCsv;

	@Scheduled(cron = "0 0/1 * * * ?") // Run every minute
	public void runJob() {
		try {
			logger.info("Starting job at {}", System.currentTimeMillis());
			JobParameters jobParameters = new JobParametersBuilder().addLong("time", System.currentTimeMillis())
					.toJobParameters();
			jobLauncher.run(jobwithFilePartictionCsv, jobParameters);
			logger.info("Job completed successfully at {}", System.currentTimeMillis());
		} catch (Exception e) {
			logger.error("Job failed with exception", e);
		}
	}
}
