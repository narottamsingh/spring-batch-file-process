package com.bce.batch.readerwritterprocessor;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemStreamException;
import org.springframework.batch.item.file.ResourceAwareItemWriterItemStream;
import org.springframework.core.io.Resource;

import com.bce.batch.dto.Customer;

public class ResourceAwareItemWriterItemStreamImpl implements ResourceAwareItemWriterItemStream<Customer> {

    private Resource resource;
    private BufferedWriter writer;

    @Override
    public void open(ExecutionContext executionContext) throws ItemStreamException {
        try {
            writer = new BufferedWriter(new FileWriter(resource.getFile()));
        } catch (IOException e) {
            throw new ItemStreamException("Failed to open the resource for writing.", e);
        }
    }

    @Override
    public void update(ExecutionContext executionContext) throws ItemStreamException {
        // Implement if there is any need to update the execution context, like keeping track of line counts
    }

    @Override
    public void close() throws ItemStreamException {
        if (writer != null) {
            try {
                writer.close();
            } catch (IOException e) {
                throw new ItemStreamException("Failed to close the resource.", e);
            }
        }
    }

    @Override
    public void write(List<? extends Customer> items) throws Exception {
        if (writer == null) {
            throw new IllegalStateException("Writer must be opened before writing.");
        }

        for (Customer customer : items) {
            writer.write(customer.toString());  // Assuming Customer has a proper toString method
            writer.newLine();
        }
        writer.flush();
    }

    @Override
    public void setResource(Resource resource) {
        this.resource = resource;
    }
}
