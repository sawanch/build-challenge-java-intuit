package io.github.sawanc.assignment2;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for CSVReader.
 *
 * @author sawan chakraborty
 */
public class CSVReaderTest {

    /**
     * Tests successful reading of a valid CSV file.
     */
    @Test
    public void testReadValidCSV(@TempDir Path tempDir) throws IOException {
        // Create a temporary CSV file with valid data
        Path csvFile = tempDir.resolve("test_sales.csv");
        String csvContent = """
                order_id,product,category,region,amount,quantity,order_date
                ORD-001,Laptop,Electronics,North America,1200.50,1,2025-01-05
                ORD-002,Mouse,Electronics,Europe,25.99,2,2025-01-06
                ORD-003,Desk,Furniture,Asia,450.00,1,2025-01-07
                """;
        Files.writeString(csvFile, csvContent);

        // Read the CSV file
        List<SalesRecord> records = CSVReader.readSalesData(csvFile.toString());

        // Verify the results
        assertNotNull(records, "Records list should not be null");
        assertEquals(3, records.size(), "Should read 3 records");

        // Verify first record
        SalesRecord record1 = records.get(0);
        assertEquals("ORD-001", record1.getOrderId());
        assertEquals("Laptop", record1.getProduct());
        assertEquals("Electronics", record1.getCategory());
        assertEquals("North America", record1.getRegion());
        assertEquals(1200.50, record1.getAmount(), 0.01);
        assertEquals(1, record1.getQuantity());
        assertEquals("2025-01-05", record1.getOrderDate());

        // Verify second record
        SalesRecord record2 = records.get(1);
        assertEquals("ORD-002", record2.getOrderId());
        assertEquals("Mouse", record2.getProduct());
        assertEquals(25.99, record2.getAmount(), 0.01);
        assertEquals(2, record2.getQuantity());
    }

    /**
     * Tests reading the actual sales.csv file from the data directory.
     */
    @Test
    public void testReadActualSalesFile() throws IOException {
        String filePath = "data/sales.csv";
        
        List<SalesRecord> records = CSVReader.readSalesData(filePath);
        
        assertNotNull(records, "Records list should not be null");
        assertEquals(100, records.size(), "Should read 100 records from sales.csv");
        
        // Verify first record structure
        SalesRecord firstRecord = records.get(0);
        assertNotNull(firstRecord.getOrderId());
        assertNotNull(firstRecord.getProduct());
        assertNotNull(firstRecord.getCategory());
        assertNotNull(firstRecord.getRegion());
        assertTrue(firstRecord.getAmount() > 0);
        assertTrue(firstRecord.getQuantity() > 0);
        assertNotNull(firstRecord.getOrderDate());
    }

    /**
     * Tests that FileNotFoundException is thrown for non-existent file.
     */
    @Test
    public void testFileNotFound() {
        String nonExistentFile = "non_existent_file.csv";
        
        IOException exception = assertThrows(IOException.class, () -> {
            CSVReader.readSalesData(nonExistentFile);
        }, "Should throw IOException for non-existent file");
        
        assertTrue(exception.getMessage().contains("CSV file not found"),
                "Exception message should indicate file not found");
    }

    /**
     * Tests that empty CSV file throws appropriate exception.
     */
    @Test
    public void testEmptyCSVFile(@TempDir Path tempDir) throws IOException {
        // Create an empty CSV file
        Path csvFile = tempDir.resolve("empty.csv");
        Files.writeString(csvFile, "");

        // Should throw IllegalArgumentException for empty file
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            CSVReader.readSalesData(csvFile.toString());
        }, "Should throw IllegalArgumentException for empty CSV");
        
        assertTrue(exception.getMessage().contains("empty"),
                "Exception message should indicate empty file");
    }

    /**
     * Tests CSV with only header (no data rows).
     */
    @Test
    public void testCSVWithOnlyHeader(@TempDir Path tempDir) throws IOException {
        // Create CSV with only header
        Path csvFile = tempDir.resolve("header_only.csv");
        String csvContent = "order_id,product,category,region,amount,quantity,order_date\n";
        Files.writeString(csvFile, csvContent);

        List<SalesRecord> records = CSVReader.readSalesData(csvFile.toString());
        
        assertNotNull(records, "Records list should not be null");
        assertEquals(0, records.size(), "Should return empty list for header-only CSV");
    }

    /**
     * Tests malformed CSV with wrong number of columns.
     */
    @Test
    public void testMalformedCSVWrongColumnCount(@TempDir Path tempDir) throws IOException {
        // Create CSV with wrong number of columns
        Path csvFile = tempDir.resolve("malformed.csv");
        String csvContent = """
                order_id,product,category,region,amount,quantity,order_date
                ORD-001,Laptop,Electronics,North America
                """;
        Files.writeString(csvFile, csvContent);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            CSVReader.readSalesData(csvFile.toString());
        }, "Should throw IllegalArgumentException for wrong column count");
        
        assertTrue(exception.getMessage().contains("Expected 7 columns"),
                "Exception should mention expected column count");
    }

    /**
     * Tests CSV with empty fields.
     */
    @Test
    public void testCSVWithEmptyFields(@TempDir Path tempDir) throws IOException {
        // Create CSV with empty field
        Path csvFile = tempDir.resolve("empty_field.csv");
        String csvContent = """
                order_id,product,category,region,amount,quantity,order_date
                ORD-001,,Electronics,North America,1200.50,1,2025-01-05
                """;
        Files.writeString(csvFile, csvContent);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            CSVReader.readSalesData(csvFile.toString());
        }, "Should throw IllegalArgumentException for empty fields");
        
        assertTrue(exception.getMessage().contains("empty fields"),
                "Exception should mention empty fields");
    }

    /**
     * Tests CSV with invalid amount value (non-numeric).
     */
    @Test
    public void testCSVWithInvalidAmount(@TempDir Path tempDir) throws IOException {
        // Create CSV with invalid amount
        Path csvFile = tempDir.resolve("invalid_amount.csv");
        String csvContent = """
                order_id,product,category,region,amount,quantity,order_date
                ORD-001,Laptop,Electronics,North America,invalid,1,2025-01-05
                """;
        Files.writeString(csvFile, csvContent);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            CSVReader.readSalesData(csvFile.toString());
        }, "Should throw IllegalArgumentException for invalid amount");
        
        assertTrue(exception.getMessage().contains("amount"),
                "Exception should mention invalid amount");
    }

    /**
     * Tests CSV with invalid quantity value (non-numeric).
     */
    @Test
    public void testCSVWithInvalidQuantity(@TempDir Path tempDir) throws IOException {
        // Create CSV with invalid quantity
        Path csvFile = tempDir.resolve("invalid_quantity.csv");
        String csvContent = """
                order_id,product,category,region,amount,quantity,order_date
                ORD-001,Laptop,Electronics,North America,1200.50,invalid,2025-01-05
                """;
        Files.writeString(csvFile, csvContent);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            CSVReader.readSalesData(csvFile.toString());
        }, "Should throw IllegalArgumentException for invalid quantity");
        
        assertTrue(exception.getMessage().contains("quantity"),
                "Exception should mention invalid quantity");
    }

    /**
     * Tests CSV with whitespace in fields (should be trimmed).
     */
    @Test
    public void testCSVWithWhitespace(@TempDir Path tempDir) throws IOException {
        // Create CSV with whitespace
        Path csvFile = tempDir.resolve("whitespace.csv");
        String csvContent = """
                order_id,product,category,region,amount,quantity,order_date
                 ORD-001 , Laptop , Electronics , North America , 1200.50 , 1 , 2025-01-05 
                """;
        Files.writeString(csvFile, csvContent);

        List<SalesRecord> records = CSVReader.readSalesData(csvFile.toString());
        
        assertNotNull(records);
        assertEquals(1, records.size());
        
        SalesRecord record = records.get(0);
        assertEquals("ORD-001", record.getOrderId(), "Order ID should be trimmed");
        assertEquals("Laptop", record.getProduct(), "Product should be trimmed");
        assertEquals("Electronics", record.getCategory(), "Category should be trimmed");
    }

    /**
     * Tests CSV with multiple valid records.
     */
    @Test
    public void testCSVWithMultipleRecords(@TempDir Path tempDir) throws IOException {
        Path csvFile = tempDir.resolve("multiple.csv");
        String csvContent = """
                order_id,product,category,region,amount,quantity,order_date
                ORD-001,Laptop,Electronics,North America,1200.50,1,2025-01-05
                ORD-002,Mouse,Electronics,Europe,25.99,2,2025-01-06
                ORD-003,Desk,Furniture,Asia,450.00,1,2025-01-07
                ORD-004,Chair,Furniture,Africa,300.00,1,2025-01-08
                ORD-005,Notebook,Stationery,South America,5.99,10,2025-01-09
                """;
        Files.writeString(csvFile, csvContent);

        List<SalesRecord> records = CSVReader.readSalesData(csvFile.toString());
        
        assertEquals(5, records.size(), "Should read all 5 records");
        
        // Verify different categories
        assertEquals("Electronics", records.get(0).getCategory());
        assertEquals("Electronics", records.get(1).getCategory());
        assertEquals("Furniture", records.get(2).getCategory());
        assertEquals("Furniture", records.get(3).getCategory());
        assertEquals("Stationery", records.get(4).getCategory());
    }
}

