//package com.bce.batch.config;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import org.springframework.batch.item.file.FlatFileItemReader;
//import org.springframework.batch.item.file.transform.Range;
//
//public class CustomFixedWidthItemReader<T> extends FlatFileItemReader<T> {
//
//    private Range[] fieldRanges;
//
//    public void setFieldRanges(Range[] fieldRanges) {
//        this.fieldRanges = fieldRanges;
//    }
//
//    @Override
//    protected T doRead() throws Exception {
//        String line = this.readLine();
//        if (line == null) {
//            return null;
//        }
//
//        T item = doParse(line);
//        return item;
//    }
//
//    private T doParse(String line) {
//        // Your custom logic to parse fixed-width fields
//        List<String> fieldValues = new ArrayList<>();
//
//        for (Range fieldRange : fieldRanges) {
//            int start = fieldRange.getMin() - 1; // Adjust to 0-based index
//            int end = fieldRange.getMax(); // End position is inclusive
//
//            if (end > line.length()) {
//                end = line.length();
//            }
//
//            String fieldValue = line.substring(start, end).trim();
//            fieldValues.add(fieldValue);
//        }
//
//        // Create and populate your domain object (replace MyObject with your actual class)
//        FieldDto myObject = new MyObject();
//        myObject.setFirstName(fieldValues.get(0));
//        myObject.setLastName(fieldValues.get(1));
//        myObject.setAge(Integer.parseInt(fieldValues.get(2)));
//        myObject.setMarks(Integer.parseInt(fieldValues.get(3)));
//
//        return (T) myObject;
//    }
//}
//
