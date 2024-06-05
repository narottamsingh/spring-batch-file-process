package com.bce;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class MultiThreadedFileWriter {
    private final BufferedWriter writer;

    public MultiThreadedFileWriter(String filePath) throws IOException {
        this.writer = new BufferedWriter(new FileWriter(filePath, true));
    }

    public void writeToFile(String content) {
        synchronized (this) {
            try {
               // writer.write(content.getBytes());
                writer.newLine();
                writer.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void close() throws IOException {
        writer.close();
    }
}
