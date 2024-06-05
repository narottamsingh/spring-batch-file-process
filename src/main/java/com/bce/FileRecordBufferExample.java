package com.bce;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

class FileRecordBuffer {
    private byte[] buffer;
    private int bufferSize;
    private int count;

    public FileRecordBuffer(int bufferSize) {
        this.bufferSize = bufferSize;
        this.buffer = new byte[bufferSize];
        this.count = 0;
    }

    public void addRecord(String record) {
        byte[] recordBytes = record.getBytes(StandardCharsets.UTF_8);
        if (recordBytes.length + count > bufferSize) {
            flush();
        }
        System.arraycopy(recordBytes, 0, buffer, count, recordBytes.length);
        count += recordBytes.length;
    }

    public void flush() {
        // Implement logic to handle buffer when full
        if (count > 0) {
            try (BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream("/home/narottam/output.bin", true))) {
                bos.write(buffer, 0, count);
                count = 0; // Reset buffer count after flushing
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void close() {
        flush();  // Ensure any remaining records are written to the file
    }
}

public class FileRecordBufferExample {
    public static void main(String[] args) {
        FileRecordBuffer fileRecordBuffer = new FileRecordBuffer(1024); // Buffer size of 1024 bytes

        // Add records to the buffer
        fileRecordBuffer.addRecord("Hello test 1\n");
        fileRecordBuffer.addRecord("Hello test 2\n");
        fileRecordBuffer.addRecord("Hello test 3\n");
        fileRecordBuffer.addRecord("Hello test 4\n");
        fileRecordBuffer.addRecord("Hello test 5\n");
        fileRecordBuffer.addRecord("Hello test 6\n");  // This should trigger a flush

        // Close the buffer to write any remaining records
        fileRecordBuffer.close();
    }
}
