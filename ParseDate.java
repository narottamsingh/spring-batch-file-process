import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.DayOfWeek;

public class DateParsingExample {
    public static void main(String[] args) {
        String dateString = "Thu, 21 Sep 2024 19:21:32 +0000";

        // Define the input formatter
        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("EEE, dd MMM yyyy HH:mm:ss Z");

        // Parse the date string
        ZonedDateTime zonedDateTime = ZonedDateTime.parse(dateString, inputFormatter);

        // Extract and format the required parts
        String date = zonedDateTime.toLocalDate().toString(); // 2024-09-21
        DayOfWeek day = zonedDateTime.getDayOfWeek();         // SATURDAY
        String dateTime = zonedDateTime.toString();           // 2024-09-21T19:21:32Z

        // Print results
        System.out.println("Date: " + date);
        System.out.println("Day: " + day);
        System.out.println("DateTime: " + dateTime);
    }
}
