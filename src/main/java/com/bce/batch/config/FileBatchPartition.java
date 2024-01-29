package com.bce.batch.config;

import java.io.IOException;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import com.bce.batch.dto.Customer;
import com.bce.batch.readerwritterprocessor.CustomerProcessor;
import com.bce.batch.readerwritterprocessor.CustomerWritter;

@Configuration
public class FileBatchPartition {

	@Autowired
	private JobBuilderFactory jobs;

	@Autowired
	private StepBuilderFactory steps;

	@Autowired
	private ResourcePatternResolver resourcePatternResolver;
	
	private final String filePathDir="/home/narottam/project/springboot/springbatch/spring-boot-batch/src/main/resources/"; 

	@Bean
	public Step slaveStep1() {
		return steps.get("slaveStep1").<Customer, Customer>chunk(10).reader(itemReader1(null)).processor(processor1())
				.writer(writer1()).taskExecutor(taskExecutorThread1()).build();
	}

	@Bean
	public Step masterStep1() {
		return steps.get("masterStep1").partitioner("slavepatition1", partitioner1()).step(slaveStep1())
				.taskExecutor(taskExecutorThread1()).build();
	}

	@Bean
	@StepScope
	public FlatFileItemReader<Customer> itemReader1(@Value("#{stepExecutionContext['filename']}") String filePath) {
		FlatFileItemReader<Customer> itemReader = new FlatFileItemReader<>();
		itemReader.setResource(new FileSystemResource(filePathDir+filePath));
		itemReader.setName("csvReader");
		itemReader.setLinesToSkip(1);
		itemReader.setLineMapper(lineMapper1());
		return itemReader;
	}

	@Bean
	@StepScope
	public LineMapper<Customer> lineMapper1() {
		DefaultLineMapper<Customer> lineMapper = new DefaultLineMapper<>();

		DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer();
		lineTokenizer.setDelimiter(",");
		lineTokenizer.setStrict(false);
		lineTokenizer.setNames("id", "firstName", "lastName", "email", "gender", "contactNo", "country", "dob");

		BeanWrapperFieldSetMapper<Customer> fieldSetMapper = new BeanWrapperFieldSetMapper<>();
		fieldSetMapper.setTargetType(Customer.class);

		lineMapper.setLineTokenizer(lineTokenizer);
		lineMapper.setFieldSetMapper(fieldSetMapper);
		return lineMapper;

	}

	@Bean
	@StepScope
	public CustomerProcessor processor1() {
		return new CustomerProcessor();
	}

	@Bean
	@StepScope
	public CustomerWritter<Customer> writer1() {
		return new CustomerWritter<Customer>();
	}

	@Bean
	public ThreadPoolTaskExecutor taskExecutorThread1() {
		ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
		threadPoolTaskExecutor.setCorePoolSize(10);
		threadPoolTaskExecutor.setQueueCapacity(20);
		threadPoolTaskExecutor.setThreadNamePrefix("testmultithread");
		return threadPoolTaskExecutor;
	}

	@Bean
	public CustomMultiResourcePartitioner partitioner1() {
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
	public Job jobwithFilePartictionCsv() {
		return jobs.get("jobwithFilePartictionCsv").start(masterStep1()).build();
	}

}