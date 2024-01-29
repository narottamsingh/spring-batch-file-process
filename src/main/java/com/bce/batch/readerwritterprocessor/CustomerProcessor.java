package com.bce.batch.readerwritterprocessor;

import org.springframework.batch.item.ItemProcessor;

import com.bce.batch.dto.Customer;

public class CustomerProcessor implements ItemProcessor<Customer,Customer> {

    @Override
    public Customer process(Customer customer) throws Exception {
        	System.out.println("Processor---- "+customer.toString()+" -- "+Thread.currentThread().getName());
            return customer;
    }
}
