package com.bce;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class MultiThreadedFileWriterTest {
    public static void main(String[] args) {
        final int NUM_THREADS = 5;
        final int NUM_RECORDS = 10;
        String filePath = "output.txt";

        try {
            MultiThreadedFileWriter fileWriter = new MultiThreadedFileWriter(filePath);
            ExecutorService executor = Executors.newFixedThreadPool(NUM_THREADS);

            for (int i = 0; i < NUM_RECORDS; i++) {
                final String content = "Record " + (i + 1);
                executor.submit(() -> fileWriter.writeToFile(content));
            }

            executor.shutdown();
            executor.awaitTermination(1, TimeUnit.MINUTES);

            fileWriter.close();
            System.out.println("All records have been written to the file.");

            // Read and verify the file content
            readAndVerifyFile(filePath, NUM_RECORDS);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void readAndVerifyFile(String filePath, int expectedRecords) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            int count = 0;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
                count++;
            }
            if (count == expectedRecords) {
                System.out.println("Test passed: All records are present.");
            } else {
                System.out.println("Test failed: Expected " + expectedRecords + " records, but found " + count + " records.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

