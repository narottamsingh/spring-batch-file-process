package com.bce.batch.config;

import java.io.IOException;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
@EnableBatchProcessing
public class PartitionSample {

	@Autowired
	private JobBuilderFactory jobs;

	@Autowired
	private StepBuilderFactory steps;

	@Autowired
	private ResourcePatternResolver resourcePatternResolver;

	
	@Bean
	public Step slaveStep() {
		return steps.get("slaveStep").tasklet(getTasklet(null, null)).build();
	}

	@Bean
	public Step masterStep() {
		return steps.get("masterStep").partitioner("slavepatition", partitioner()).step(slaveStep())
				.taskExecutor(taskExecutorThread()).build();
	}

	@Bean
	public ThreadPoolTaskExecutor taskExecutorThread() {
		ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
		threadPoolTaskExecutor.setCorePoolSize(10);
		threadPoolTaskExecutor.setQueueCapacity(20);
		threadPoolTaskExecutor.setThreadNamePrefix("testmultithread");
		return threadPoolTaskExecutor;
	}

	@Bean
	public CustomMultiResourcePartitioner partitioner() {
		CustomMultiResourcePartitioner partitioner = new CustomMultiResourcePartitioner();
		Resource[] resources;
		try {
			resources = resourcePatternResolver.getResources("file:src/main/resources/*.csv");
		} catch (IOException e) {
			throw new RuntimeException("I/O problems when resolving" + " the input file pattern.", e);
		}
		partitioner.setResources(resources);
		return partitioner;
	}


	@Bean
	@StepScope
	public Tasklet getTasklet(@Value("#{stepExecutionContext['filename']}") String partitionData,
			@Value("#{stepExecutionContext['check']}") String check) {
		return (contribution, chunkContext) -> {
			System.out.println(Thread.currentThread().getName() + " processing partitionData = " + partitionData
					+ " check " + check);
			return RepeatStatus.FINISHED;
		};
	}

	@Bean
	public Job jobwithFilePartiction() {
		return jobs.get("jobwithFilePartiction").start(masterStep()).build();
	}

}