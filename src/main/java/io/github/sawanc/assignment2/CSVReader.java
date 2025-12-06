package io.github.sawanc.assignment2;

import com.opencsv.exceptions.CsvException;

import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Utility class for reading sales data from CSV files.
 *
 * @author sawan chakraborty
 */
public class CSVReader {

    /**
     * Reads sales records from a CSV file.
     * Expected CSV format: header row followed by data rows with columns: product, category, region, amount.
     *
     * @param filePath the path to the CSV file
     * @return a list of SalesRecord objects parsed from the file
     * @throws IOException if the file cannot be read or does not exist
     * @throws IllegalArgumentException if the CSV data is malformed
     */
    public static List<SalesRecord> readSalesData(String filePath) throws IOException {
        // Validate file exists
        Path path = Paths.get(filePath);
        if (!Files.exists(path)) {
            throw new IOException("CSV file not found: " + filePath);
        }

        List<SalesRecord> salesRecords = new ArrayList<>();

        try (com.opencsv.CSVReader csvReader = new com.opencsv.CSVReader(new FileReader(filePath))) {
            // Read all lines from the CSV file
            List<String[]> allRows = csvReader.readAll();

            // Validate that file has content
            if (allRows.isEmpty()) {
                throw new IllegalArgumentException("CSV file is empty: " + filePath);
            }

            // Skip the header row (first row)
            boolean isFirstRow = true;
            for (String[] row : allRows) {
                if (isFirstRow) {
                    isFirstRow = false;
                    continue; // Skip header
                }

                // Parse each row into a SalesRecord
                try {
                    SalesRecord record = parseRow(row);
                    salesRecords.add(record);
                } catch (Exception e) {
                    throw new IllegalArgumentException(
                            "Malformed CSV row: " + String.join(",", row) + " - " + e.getMessage(), e);
                }
            }

        } catch (CsvException e) {
            throw new IllegalArgumentException("Error parsing CSV file: " + e.getMessage(), e);
        }

        return salesRecords;
    }

    /**
     * Parses a CSV row into a SalesRecord object.
     *
     * @param row array of strings representing a CSV row
     * @return a SalesRecord object
     * @throws IllegalArgumentException if the row format is invalid
     */
    private static SalesRecord parseRow(String[] row) {
        if (row.length != 4) {
            throw new IllegalArgumentException(
                    "Expected 4 columns (product, category, region, amount), but found " + row.length);
        }

        // Trim whitespace from all fields
        String product = row[0].trim();
        String category = row[1].trim();
        String region = row[2].trim();
        String amountStr = row[3].trim();

        // Validate non-empty fields
        if (product.isEmpty() || category.isEmpty() || region.isEmpty() || amountStr.isEmpty()) {
            throw new IllegalArgumentException("CSV row contains empty fields");
        }

        // Parse amount to double
        double amount;
        try {
            amount = Double.parseDouble(amountStr);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid amount value: " + amountStr, e);
        }

        return new SalesRecord(product, category, region, amount);
    }
}
