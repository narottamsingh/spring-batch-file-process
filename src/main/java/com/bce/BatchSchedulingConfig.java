//package com.bce;
//
//import org.springframework.batch.core.Job;
//import org.springframework.batch.core.JobParameters;
//import org.springframework.batch.core.JobParametersBuilder;
//import org.springframework.batch.core.JobParametersInvalidException;
//import org.springframework.batch.core.launch.JobLauncher;
//import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
//import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
//import org.springframework.batch.core.repository.JobRestartException;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.scheduling.annotation.EnableScheduling;
//import org.springframework.scheduling.annotation.Scheduled;
//
//@Configuration
//@EnableScheduling
//public class BatchSchedulingConfig {
//	@Autowired
//	@Qualifier("jobwithFilePartictionCsv")
//	private Job jobwithFilePartictionCsv;
//	
//    @Autowired
//    private JobLauncher jobLauncher;
//
//    @Scheduled(cron = "0 * * * * ?")
//	public void runJob1() {
//		 JobParameters jobParameters = new JobParametersBuilder()
//	                .addLong("startAt", System.currentTimeMillis()).toJobParameters();
//	        try {
//	            jobLauncher.run(jobwithFilePartictionCsv, jobParameters);
//	        } catch (JobExecutionAlreadyRunningException | JobRestartException | JobInstanceAlreadyCompleteException | JobParametersInvalidException e) {
//	            e.printStackTrace();
//	        }
//	}
//
//}
