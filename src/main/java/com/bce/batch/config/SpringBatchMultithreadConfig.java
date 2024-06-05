//package com.bce.batch.config;
//
//import org.springframework.batch.core.Job;
//import org.springframework.batch.core.Step;
//import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
//import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
//import org.springframework.batch.item.file.FlatFileItemReader;
//import org.springframework.batch.item.file.LineMapper;
//import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
//import org.springframework.batch.item.file.mapping.DefaultLineMapper;
//import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.core.io.FileSystemResource;
//import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
//
//import com.bce.batch.dto.Customer;
//import com.bce.batch.readerwritterprocessor.CustomerProcessor;
//import com.bce.batch.readerwritterprocessor.CustomerWritter;
//
//import lombok.AllArgsConstructor;
//
//@Configuration
//@AllArgsConstructor
//public class SpringBatchMultithreadConfig {
//
//    private JobBuilderFactory jobBuilderFactory;
//
//    private StepBuilderFactory stepBuilderFactory;
//
//    @Bean
//    public FlatFileItemReader<Customer> readerMultiThreader() {
//        FlatFileItemReader<Customer> itemReader = new FlatFileItemReader<>();
//        itemReader.setResource(new FileSystemResource("src/main/resources/customers.csv"));
//        itemReader.setName("csvReader");
//        itemReader.setLinesToSkip(1);
//        itemReader.setLineMapper(lineMapperMultiThreader());
//        return itemReader;
//    }
//
//    private LineMapper<Customer> lineMapperMultiThreader() {
//        DefaultLineMapper<Customer> lineMapper = new DefaultLineMapper<>();
//
//        DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer();
//        lineTokenizer.setDelimiter(",");
//        lineTokenizer.setStrict(false);
//        lineTokenizer.setNames("id", "firstName", "lastName", "email", "gender", "contactNo", "country", "dob");
//
//        BeanWrapperFieldSetMapper<Customer> fieldSetMapper = new BeanWrapperFieldSetMapper<>();
//        fieldSetMapper.setTargetType(Customer.class);
//
//        lineMapper.setLineTokenizer(lineTokenizer);
//        lineMapper.setFieldSetMapper(fieldSetMapper);
//        return lineMapper;
//
//    }
//
//    @Bean
//    public CustomerProcessor processorMultiThreader() {
//        return new CustomerProcessor();
//    }
//    
//    @Bean
//    public CustomerWritter<Customer> writerMultiThreader() {
//        return new CustomerWritter<Customer>();
//    }
//
//   
//
//    @Bean
//    public Step step1MultiThreader() {
//        return stepBuilderFactory.get("csv-step").<Customer, Customer>chunk(10)
//                .reader(readerMultiThreader())
//                .processor(processorMultiThreader())
//                .writer(writerMultiThreader())
//                .taskExecutor(taskExecutor())
//                .build();
//    }
//
//    @Bean
//    public Job runJobMultiThread() {
//        return jobBuilderFactory.get("fileprocessingusingmultithreading")
//                .flow(step1MultiThreader()).end().build();
//
//    }
//
//    @Bean
//    public ThreadPoolTaskExecutor taskExecutor() {
//        ThreadPoolTaskExecutor threadPoolTaskExecutor=new ThreadPoolTaskExecutor();
//        threadPoolTaskExecutor.setCorePoolSize(4);
//        threadPoolTaskExecutor.setQueueCapacity(10);
//        threadPoolTaskExecutor.setThreadNamePrefix("testmultithread");
//        return threadPoolTaskExecutor;
//    }
//
//}
