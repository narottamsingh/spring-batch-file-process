package com.bce.batch.readerwritterprocessor;

import java.util.List;

import org.springframework.batch.item.ItemWriter;

public class CustomerWritter<Customer> implements ItemWriter<Customer> {

	@Override
	public void write(List<? extends Customer> items) throws Exception {
		for (Customer customer : items) {
//			Thread.sleep(1000);
			System.out.println("Writting..."+customer);
		}
		
	}

   
}
