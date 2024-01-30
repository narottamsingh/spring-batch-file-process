package com.bce.batch.config;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.batch.item.file.transform.FixedLengthTokenizer;
import org.springframework.batch.item.file.transform.Range;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import com.bce.batch.dto.Customer;
import com.bce.batch.readerwritterprocessor.CustomerProcessor;
import com.bce.batch.readerwritterprocessor.CustomerWritter;

import lombok.AllArgsConstructor;

@Configuration
@AllArgsConstructor
public class SpringBatchChunkConfig {

    private JobBuilderFactory jobBuilderFactory;

    private StepBuilderFactory stepBuilderFactory;

    
    @Bean
    @StepScope
    public FlatFileItemReader<Customer> fixedLengthReader() {
        FlatFileItemReader<Customer> reader = new FlatFileItemReader<>();
        reader.setResource(new FileSystemResource("src/main/resources/data.txt")); // Change the file name as needed

        FixedLengthTokenizer tokenizer = new FixedLengthTokenizer();
        tokenizer.setColumns(new Range[]{new Range(1, 10), new Range(10, 18)});
        tokenizer.setNames(new String[]{"firstName", "lastName"});

        reader.setLineMapper(new DefaultLineMapper<Customer>() {{
            setLineTokenizer(tokenizer);
            setFieldSetMapper(new BeanWrapperFieldSetMapper<Customer>() {{
                setTargetType(Customer.class);
            }});
        }});

        return reader;
    }
    
	/*
	 * @Bean public FlatFileItemReader<Customer> reader() {
	 * FlatFileItemReader<Customer> itemReader = new FlatFileItemReader<>();
	 * itemReader.setResource(new
	 * FileSystemResource("src/main/resources/customers.csv"));
	 * itemReader.setName("csvReader"); itemReader.setLinesToSkip(1);
	 * itemReader.setLineMapper(lineMapper()); return itemReader; }
	 * 
	 * private LineMapper<Customer> lineMapper() { DefaultLineMapper<Customer>
	 * lineMapper = new DefaultLineMapper<>();
	 * 
	 * DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer();
	 * lineTokenizer.setDelimiter(","); lineTokenizer.setStrict(false);
	 * lineTokenizer.setNames("id", "firstName", "lastName", "email", "gender",
	 * "contactNo", "country", "dob");
	 * 
	 * BeanWrapperFieldSetMapper<Customer> fieldSetMapper = new
	 * BeanWrapperFieldSetMapper<>(); fieldSetMapper.setTargetType(Customer.class);
	 * 
	 * lineMapper.setLineTokenizer(lineTokenizer);
	 * lineMapper.setFieldSetMapper(fieldSetMapper); return lineMapper;
	 * 
	 * }
	 * 
	 */    @Bean
    public CustomerProcessor processor() {
        return new CustomerProcessor();
    }
    
    @Bean
    public CustomerWritter<Customer> writer() {
        return new CustomerWritter<Customer>();
    }

   

    @Bean
    public Step step1() {
        return stepBuilderFactory.get("csv-step").<Customer, Customer>chunk(10)
                .reader(fixedLengthReader())
                .processor(processor())
                .writer(writer())
                .taskExecutor(taskExecutorThreadProcess())
                .build();
    }

    @Bean
    public Job runJobFileProcessWithChunk() {
        return jobBuilderFactory.get("runJobFileProcessWithChunk")
                .flow(step1()).end().build();

    }

 
    
    @Bean
    public ThreadPoolTaskExecutor taskExecutorThreadProcess() {
        ThreadPoolTaskExecutor threadPoolTaskExecutor=new ThreadPoolTaskExecutor();
        threadPoolTaskExecutor.setCorePoolSize(10);
        threadPoolTaskExecutor.setQueueCapacity(20);
        threadPoolTaskExecutor.setThreadNamePrefix("testmultithread");
        return threadPoolTaskExecutor;
    }

}
