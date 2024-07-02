import com.opencsv.CSVWriter;
import org.springframework.stereotype.Service;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

@Service
public class CsvExportService {

    public void writeUserOrdersToCsv(String filePath, List<UserOrderDTO> userOrders) {
        try (CSVWriter writer = new CSVWriter(new FileWriter(filePath))) {
            // Write header
            String[] header = { "UserId", "UserName", "UserEmail", "OrderId", "Product", "Quantity" };
            writer.writeNext(header);

            // Write data
            for (UserOrderDTO userOrder : userOrders) {
                String[] data = {
                    userOrder.getUserId().toString(),
                    userOrder.getUserName(),
                    userOrder.getUserEmail(),
                    userOrder.getOrderId().toString(),
                    userOrder.getProduct(),
                    userOrder.getQuantity().toString()
                };
                writer.writeNext(data);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
