package com.bce.batch.readerwritterprocessor;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.ResourceAwareItemWriterItemStream;
import org.springframework.batch.item.file.ResourceSuffixCreator;
import org.springframework.batch.item.file.SimpleResourceSuffixCreator;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.util.Assert;

public class CustomerWritter<Customer> implements ItemWriter<Customer> {

    private boolean opened = false;

    private ResourceAwareItemWriterItemStream<? super Customer> delegate;
    private Resource resource;
    private int resourceIndex = 1;

    private int itemCountLimitPerResource = Integer.MAX_VALUE;
    private int currentResourceItemCount = 0;

    private ResourceSuffixCreator suffixCreator = new SimpleResourceSuffixCreator();

    @Override
    public void write(List<? extends Customer> items) throws Exception {
        if (resource == null) {
            throw new IllegalStateException("Resource must be set before calling write.");
        }

        if (!opened) {
            openNewResource();
        }

        delegate.write(items);
        currentResourceItemCount += items.size();
        if (currentResourceItemCount >= itemCountLimitPerResource) {
            delegate.close();
            resourceIndex++;
            currentResourceItemCount = 0;
            openNewResource();
        }
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
        String path = resource.getFile().getAbsolutePath() + suffixCreator.getSuffix(resourceIndex);
        File file = new File(path);
        delegate.setResource(new FileSystemResource(file));
        return file;
    }

    /**
     * Allows customization of the suffix of the created resources based on the
     * index.
     */
    public void setResourceSuffixCreator(ResourceSuffixCreator suffixCreator) {
        this.suffixCreator = suffixCreator;
    }

    /**
     * After this limit is exceeded the next chunk will be written into newly
     * created resource.
     */
    public void setItemCountLimitPerResource(int itemCountLimitPerResource) {
        this.itemCountLimitPerResource = itemCountLimitPerResource;
    }

    /**
     * Delegate used for actual writing of the output.
     */
    public void setDelegate(ResourceAwareItemWriterItemStream<? super Customer> delegate) {
        this.delegate = delegate;
    }

    public void setResource(Resource resource) {
        this.resource = resource;
    }
}
