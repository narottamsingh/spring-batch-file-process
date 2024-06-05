package com.bce.batch.readerwritterprocessor;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemStream;
import org.springframework.batch.item.ItemStreamException;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.ResourceAwareItemWriterItemStream;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.util.Assert;

import com.bce.batch.config.AppConfigStatic;

public class CustomerWriter<Customer> implements ItemWriter<Customer>, ItemStream {

	private boolean opened = false;
	private ResourceAwareItemWriterItemStream<? super Customer> delegate;
	private Resource resource;
	
	@Override
	public void write(List<? extends Customer> items) throws Exception {
		System.out.print(AppConfigStatic.getDescription());
		if (resource == null) {
			throw new IllegalStateException("Resource must be set before calling write.");
		}

		if (!opened) {
			openNewResource();
		}

		delegate.write(items);
	}

	private void openNewResource() throws IOException {
		File file = setResourceToDelegate();
		if (file.getParent() != null) {
			new File(file.getParent()).mkdirs();
		}
		file.createNewFile();
		Assert.state(file.canWrite(), "Output resource " + file.getAbsolutePath() + " must be writable");
		delegate.open(new ExecutionContext());
		opened = true;
	}

	private File setResourceToDelegate() throws IOException {
		String path = resource.getFile().getAbsolutePath();
		File file = new File(path);
		delegate.setResource(new FileSystemResource(file));
		return file;
	}

	@Override
	public void open(ExecutionContext executionContext) throws ItemStreamException {
		// No-op
	}

	@Override
	public void update(ExecutionContext executionContext) throws ItemStreamException {
		// No-op
	}

	@Override
	public void close() throws ItemStreamException {
		if (opened) {
			delegate.close();
			opened = false;
		}
	}

	
	public void setDelegate(ResourceAwareItemWriterItemStream<? super Customer> delegate) {
		this.delegate = delegate;
	}

	public void setResource(Resource resource) {
		this.resource = resource;
	}
}
