package com.bce.batch.config;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.FixedLengthTokenizer;
import org.springframework.batch.item.file.transform.Range;
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
public class FileBatchPartition2 {

	@Autowired
	private JobBuilderFactory jobs;

	@Autowired
	private StepBuilderFactory steps;

	@Autowired
	private ResourcePatternResolver resourcePatternResolver;
	
	@Value("${test}")
	private String test;

	private final String filePathDir = "/home/narottam/project/springboot/springbatch/spring-boot-batch/src/main/resources/";

	public static AtomicInteger c = new AtomicInteger(0);

	@Bean
	public Step slaveStep2() {
		return steps.get("slaveStep2").<Customer, Customer>chunk(10).reader(fixedLengthReader2(null))
				.processor(processor2()).writer(writer2()).taskExecutor(taskExecutorThread2()).build();
	}

	@Bean
	public Step masterStep2() {
		return steps.get("masterStep2").partitioner("slavepatition2", partitioner2()).step(slaveStep2())
				.taskExecutor(taskExecutorThread2()).build();
	}

	@Bean
	@StepScope
	public FlatFileItemReader<Customer> fixedLengthReader2(
			@Value("#{stepExecutionContext['filename']}") String filePath) {
		FlatFileItemReader<Customer> reader = new FlatFileItemReader<>();
		reader.setResource(new FileSystemResource(filePathDir + filePath)); // Change the file name as needed

		FixedLengthTokenizer tokenizer = new FixedLengthTokenizer();
		tokenizer.setColumns(new Range[] { new Range(1, 10) });
		tokenizer.setNames(new String[] { "firstName" });

		reader.setLineMapper(new DefaultLineMapper<Customer>() {
			{
				setLineTokenizer(createFixedLengthTokenizer());

				setFieldSetMapper(new BeanWrapperFieldSetMapper<Customer>() {
					{
						setTargetType(Customer.class);
					}
				});
			}
		});

		return reader;
	}

	private FixedLengthTokenizer createFixedLengthTokenizer() {

		FixedLengthTokenizer tokenizer = new FixedLengthTokenizer();
		tokenizer.setNames(new String[] { "firstName" }); // Add field names
		tokenizer.setColumns(new Range[] { new Range(1, 15) }); // Adjust ranges as needed

		// Customize behavior to trim whitespace from each field
		tokenizer.setStrict(false); // Allow for shorter lines than the specified ranges
		tokenizer.setNames(new String[] { "firstName" });
		tokenizer.setColumns(new Range[] { new Range(1, 15) }); // Adjust ranges as needed

		return tokenizer;
	}

	@Bean
	@StepScope
	public CustomerProcessor processor2() {
		return new CustomerProcessor();
	}

	@Bean
	@StepScope
	public CustomerWritter<Customer> writer2() {
		return new CustomerWritter<Customer>();
	}

	@Bean
	public ThreadPoolTaskExecutor taskExecutorThread2() {
		ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
		threadPoolTaskExecutor.setCorePoolSize(10);
		threadPoolTaskExecutor.setQueueCapacity(20);
		threadPoolTaskExecutor.setThreadNamePrefix("testmultithread");
		return threadPoolTaskExecutor;
	}

	@Bean
	public CustomMultiResourcePartitioner partitioner2() {
		CustomMultiResourcePartitioner partitioner = new CustomMultiResourcePartitioner();
		Resource[] resources;
		try {
			resources = resourcePatternResolver.getResources("file:" + filePathDir + "*.txt");
		} catch (IOException e) {
			throw new RuntimeException("I/O problems when resolving" + " the input file pattern.", e);
		}
		partitioner.setResources(resources);
		partitioner.partition(10);
		return partitioner;
	}

	@Bean
	public Step fileCopyStep() {
		return steps.get("fileCopyStep").tasklet(new FileCopyTasklet()) // Associate the tasklet with the step
				.build();
	}

	@Bean
	public Step fileCleanStep() {
		return steps.get("fileCleanStep").tasklet(new CleanUpTasklet()) // Associate the tasklet with the step
				.build();
	}

	@Bean
	public Job jobwithFilePartictionCsv2() {
		System.out.println("Call second job");
		 jobs.get("jobwithFilePartictionCsv2").start(fileCopyStep()).next(masterStep2()).next(fileCleanStep())
				.build();
		 return null;
	}

}