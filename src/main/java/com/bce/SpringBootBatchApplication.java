package com.bce;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class SpringBootBatchApplication {

	public static void main(String[] args) {
		//SpringApplication.run(SpringBootBatchApplication.class, args);
		
		ConfigurableApplicationContext ctx = SpringApplication.run(SpringBootApplication.class, args);
		int exitCode = SpringApplication.exit(ctx, ()->{
			return 0;
		});
		System.exit(exitCode);
	}

}
