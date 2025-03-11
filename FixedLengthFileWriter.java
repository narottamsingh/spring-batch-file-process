package com.example.demo;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

public class FixedLengthFileWriter {
    private static final int RECORD_LENGTH = 50; // Fixed size for each record

    public static void main(String[] args) throws IOException {
        String[] records = {
            "ABC 123",  // First record
            "XYZ 9876543210", // Second record
            "TEST RECORD" // Third record
        };

        try (FileOutputStream fos = new FileOutputStream("fixed_length_file.dat")) {
            for (String record : records) {
                writeFixedLengthRecord(fos, record);
            }
        }

        System.out.println("Fixed-length file created with multiple 50-byte records.");
    }

    private static void writeFixedLengthRecord(FileOutputStream fos, String record) throws IOException {
        byte[] bytes = record.getBytes(StandardCharsets.US_ASCII);
        ByteBuffer buffer = ByteBuffer.allocate(RECORD_LENGTH);

        int index = 0;
        for (byte b : bytes) {
            if (b == 0x20 && index + 2 <= RECORD_LENGTH) { // Replace space with 0x00 00
                buffer.put((byte) 0x00);
                buffer.put((byte) 0x00);
                index += 2;
            } else if (index < RECORD_LENGTH) {
                buffer.put(b);
                index++;
            }
        }

        // Ensure the record is exactly 50 bytes by padding with 0x00
        while (index < RECORD_LENGTH) {
            buffer.put((byte) 0x00);
            index++;
        }

        // Write the 50-byte record to file
        fos.write(buffer.array());
    }
}


