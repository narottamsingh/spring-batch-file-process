//package com.bce;
//
//import org.springframework.batch.core.Job;
//import org.springframework.batch.core.JobParameters;
//import org.springframework.batch.core.JobParametersBuilder;
//import org.springframework.batch.core.launch.JobLauncher;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.stereotype.Component;
//
//@Component
//public class CommandStartup implements CommandLineRunner {
//    @Autowired(required = true)
//    private JobLauncher jobLauncher;
//
//    @Autowired
//    private Job jobwithFilePartictionCsv;
//
//    @Override
//    public void run(String... args) throws Exception {
//        // Launch the job with parameters here
//        JobParameters jobParameters = new JobParametersBuilder()
//                .addString("param1", "value1") // Add your job parameters here
//                .addLong("param2", 123L)
//                .toJobParameters();
//
//        jobLauncher.run(jobwithFilePartictionCsv, jobParameters);
//    }
//
//}
