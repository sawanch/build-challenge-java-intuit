package io.github.sawanc.assignment2;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for SalesAnalysisService.
 *
 * @author sawan chakraborty
 */
public class SalesAnalysisServiceTest {

    private SalesAnalysisService service;
    private List<SalesRecord> testRecords;

    @BeforeEach
    public void setUp() {
        service = new SalesAnalysisService();
        testRecords = createTestRecords();
    }

    /**
     * Creates a sample list of sales records for testing.
     *
     * @return a list of SalesRecord objects
     */
    private List<SalesRecord> createTestRecords() {
        List<SalesRecord> records = new ArrayList<>();
        records.add(new SalesRecord("ORD-001", "Laptop", "Electronics", "North America", 1200.0, 1, "2025-01-15"));
        records.add(new SalesRecord("ORD-002", "Mouse", "Electronics", "Europe", 25.0, 2, "2025-01-16"));
        records.add(new SalesRecord("ORD-003", "Laptop", "Electronics", "Asia", 1100.0, 1, "2025-01-17"));
        records.add(new SalesRecord("ORD-004", "Desk", "Furniture", "Africa", 450.0, 1, "2025-01-18"));
        records.add(new SalesRecord("ORD-005", "Chair", "Furniture", "South America", 300.0, 1, "2025-01-19"));
        records.add(new SalesRecord("ORD-006", "Monitor", "Electronics", "Australia", 600.0, 1, "2025-01-20"));
        records.add(new SalesRecord("ORD-007", "Keyboard", "Electronics", "North America", 75.0, 3, "2025-01-21"));
        records.add(new SalesRecord("ORD-008", "Desk", "Furniture", "Europe", 500.0, 1, "2025-01-22"));
        return records;
    }

    /**
     * Tests the getTotalSales method with multiple records.
     * Verifies that the sum of all sales amounts is calculated correctly.
     */
    @Test
    public void testGetTotalSales() {
        double total = service.getTotalSales(testRecords);
        // Expected: 1200 + 25 + 1100 + 450 + 300 + 600 + 75 + 500 = 4250.0
        assertEquals(4250.0, total, 0.01, "Total sales should be 4250.0");
    }

    /**
     * Tests getTotalSales with an empty list.
     * Should return 0.0 for empty input.
     */
    @Test
    public void testGetTotalSalesEmptyList() {
        List<SalesRecord> emptyList = new ArrayList<>();
        double total = service.getTotalSales(emptyList);
        assertEquals(0.0, total, 0.01, "Total sales for empty list should be 0.0");
    }

    /**
     * Tests getTotalSales with zero values.
     * Verifies that zero amounts are handled correctly.
     */
    @Test
    public void testGetTotalSalesWithZeroValues() {
        List<SalesRecord> recordsWithZero = new ArrayList<>();
        recordsWithZero.add(new SalesRecord("ORD-101", "Product1", "Category1", "North America", 0.0, 1, "2025-01-01"));
        recordsWithZero.add(new SalesRecord("ORD-102", "Product2", "Category2", "Europe", 100.0, 1, "2025-01-02"));
        recordsWithZero.add(new SalesRecord("ORD-103", "Product3", "Category3", "Asia", 0.0, 1, "2025-01-03"));

        double total = service.getTotalSales(recordsWithZero);
        assertEquals(100.0, total, 0.01, "Total sales with zero values should be 100.0");
    }

    /**
     * Tests getTotalSales with a single record.
     * Verifies correct calculation for a single-item list.
     */
    @Test
    public void testGetTotalSalesSingleRecord() {
        List<SalesRecord> singleRecord = new ArrayList<>();
        singleRecord.add(new SalesRecord("ORD-999", "Product", "Category", "North America", 250.0, 1, "2025-01-01"));

        double total = service.getTotalSales(singleRecord);
        assertEquals(250.0, total, 0.01, "Total sales for single record should be 250.0");
    }

    /**
     * Tests the getSalesByCategory method.
     * Verifies that sales are correctly grouped by category and summed.
     */
    @Test
    public void testGetSalesByCategory() {
        Map<String, Double> salesByCategory = service.getSalesByCategory(testRecords);

        assertNotNull(salesByCategory, "Sales by category map should not be null");
        assertEquals(2, salesByCategory.size(), "Should have 2 categories");

        // Electronics: 1200 + 25 + 1100 + 600 + 75 = 3000.0
        assertEquals(3000.0, salesByCategory.get("Electronics"), 0.01,
                "Electronics category total should be 3000.0");

        // Furniture: 450 + 300 + 500 = 1250.0
        assertEquals(1250.0, salesByCategory.get("Furniture"), 0.01,
                "Furniture category total should be 1250.0");
    }

    /**
     * Tests getSalesByCategory with an empty list.
     * Should return an empty immutable map.
     */
    @Test
    public void testGetSalesByCategoryEmptyList() {
        List<SalesRecord> emptyList = new ArrayList<>();
        Map<String, Double> salesByCategory = service.getSalesByCategory(emptyList);

        assertNotNull(salesByCategory, "Sales by category map should not be null");
        assertTrue(salesByCategory.isEmpty(), "Sales by category map should be empty");
    }

    /**
     * Tests getSalesByCategory with a single record.
     * Verifies correct grouping for a single-item list.
     */
    @Test
    public void testGetSalesByCategorySingleRecord() {
        List<SalesRecord> singleRecord = new ArrayList<>();
        singleRecord.add(new SalesRecord("ORD-111", "Product", "Category1", "Europe", 100.0, 1, "2025-01-01"));

        Map<String, Double> salesByCategory = service.getSalesByCategory(singleRecord);
        assertEquals(1, salesByCategory.size(), "Should have 1 category");
        assertEquals(100.0, salesByCategory.get("Category1"), 0.01,
                "Category1 total should be 100.0");
    }

    /**
     * Tests the getSalesByRegion method.
     * Verifies that sales are correctly grouped by region and summed.
     */
    @Test
    public void testGetSalesByRegion() {
        Map<String, Double> salesByRegion = service.getSalesByRegion(testRecords);

        assertNotNull(salesByRegion, "Sales by region map should not be null");
        assertEquals(6, salesByRegion.size(), "Should have 6 regions");

        // North America: 1200 + 75 = 1275.0
        assertEquals(1275.0, salesByRegion.get("North America"), 0.01,
                "North America region total should be 1275.0");

        // Europe: 25 + 500 = 525.0
        assertEquals(525.0, salesByRegion.get("Europe"), 0.01,
                "Europe region total should be 525.0");

        // Asia: 1100.0
        assertEquals(1100.0, salesByRegion.get("Asia"), 0.01,
                "Asia region total should be 1100.0");

        // Africa: 450.0
        assertEquals(450.0, salesByRegion.get("Africa"), 0.01,
                "Africa region total should be 450.0");

        // South America: 300.0
        assertEquals(300.0, salesByRegion.get("South America"), 0.01,
                "South America region total should be 300.0");

        // Australia: 600.0
        assertEquals(600.0, salesByRegion.get("Australia"), 0.01,
                "Australia region total should be 600.0");
    }

    /**
     * Tests getSalesByRegion with an empty list.
     * Should return an empty immutable map.
     */
    @Test
    public void testGetSalesByRegionEmptyList() {
        List<SalesRecord> emptyList = new ArrayList<>();
        Map<String, Double> salesByRegion = service.getSalesByRegion(emptyList);

        assertNotNull(salesByRegion, "Sales by region map should not be null");
        assertTrue(salesByRegion.isEmpty(), "Sales by region map should be empty");
    }

    /**
     * Tests getSalesByRegion with zero values.
     * Verifies that zero amounts are handled correctly in grouping.
     */
    @Test
    public void testGetSalesByRegionWithZeroValues() {
        List<SalesRecord> recordsWithZero = new ArrayList<>();
        recordsWithZero.add(new SalesRecord("ORD-201", "Product1", "Category1", "Asia", 0.0, 1, "2025-01-01"));
        recordsWithZero.add(new SalesRecord("ORD-202", "Product2", "Category2", "Asia", 200.0, 1, "2025-01-02"));

        Map<String, Double> salesByRegion = service.getSalesByRegion(recordsWithZero);
        assertEquals(200.0, salesByRegion.get("Asia"), 0.01,
                "Asia total with zero values should be 200.0");
    }

    /**
     * Tests the getAverageSalesPerProduct method.
     * Verifies that average sales per product are calculated correctly.
     */
    @Test
    public void testGetAverageSalesPerProduct() {
        Map<String, Double> avgSalesPerProduct = service.getAverageSalesPerProduct(testRecords);

        assertNotNull(avgSalesPerProduct, "Average sales per product map should not be null");
        assertEquals(6, avgSalesPerProduct.size(), "Should have 6 unique products");

        // Laptop appears twice: (1200 + 1100) / 2 = 1150.0
        assertEquals(1150.0, avgSalesPerProduct.get("Laptop"), 0.01,
                "Average sales for Laptop should be 1150.0");

        // Desk appears twice: (450 + 500) / 2 = 475.0
        assertEquals(475.0, avgSalesPerProduct.get("Desk"), 0.01,
                "Average sales for Desk should be 475.0");

        // Mouse appears once: 25.0
        assertEquals(25.0, avgSalesPerProduct.get("Mouse"), 0.01,
                "Average sales for Mouse should be 25.0");

        // Chair appears once: 300.0
        assertEquals(300.0, avgSalesPerProduct.get("Chair"), 0.01,
                "Average sales for Chair should be 300.0");

        // Monitor appears once: 600.0
        assertEquals(600.0, avgSalesPerProduct.get("Monitor"), 0.01,
                "Average sales for Monitor should be 600.0");

        // Keyboard appears once: 75.0
        assertEquals(75.0, avgSalesPerProduct.get("Keyboard"), 0.01,
                "Average sales for Keyboard should be 75.0");
    }

    /**
     * Tests getAverageSalesPerProduct with an empty list.
     * Should return an empty immutable map.
     */
    @Test
    public void testGetAverageSalesPerProductEmptyList() {
        List<SalesRecord> emptyList = new ArrayList<>();
        Map<String, Double> avgSalesPerProduct = service.getAverageSalesPerProduct(emptyList);

        assertNotNull(avgSalesPerProduct, "Average sales per product map should not be null");
        assertTrue(avgSalesPerProduct.isEmpty(), "Average sales per product map should be empty");
    }

    /**
     * Tests getAverageSalesPerProduct with a single record.
     * Verifies correct average calculation for a single-item list.
     */
    @Test
    public void testGetAverageSalesPerProductSingleRecord() {
        List<SalesRecord> singleRecord = new ArrayList<>();
        singleRecord.add(new SalesRecord("ORD-301", "Product", "Category", "Africa", 150.0, 1, "2025-01-01"));

        Map<String, Double> avgSalesPerProduct = service.getAverageSalesPerProduct(singleRecord);
        assertEquals(1, avgSalesPerProduct.size(), "Should have 1 product");
        assertEquals(150.0, avgSalesPerProduct.get("Product"), 0.01,
                "Average sales for single product should be 150.0");
    }

    /**
     * Tests the filterHighValueTransactions method.
     * Verifies that transactions above the threshold are correctly filtered.
     */
    @Test
    public void testFilterHighValueTransactions() {
        double threshold = 500.0;
        List<SalesRecord> highValue = service.filterHighValueTransactions(testRecords, threshold);

        assertNotNull(highValue, "High-value transactions list should not be null");
        assertEquals(4, highValue.size(), "Should have 4 high-value transactions");

        // Verify all filtered records meet the threshold
        assertTrue(highValue.stream().allMatch(r -> r.getAmount() >= threshold),
                "All filtered records should have amount >= threshold");

        // Verify specific records are included
        assertTrue(highValue.stream().anyMatch(r -> r.getProduct().equals("Laptop") && r.getAmount() == 1200.0),
                "Should include Laptop with 1200.0");
        assertTrue(highValue.stream().anyMatch(r -> r.getProduct().equals("Laptop") && r.getAmount() == 1100.0),
                "Should include Laptop with 1100.0");
        assertTrue(highValue.stream().anyMatch(r -> r.getProduct().equals("Monitor") && r.getAmount() == 600.0),
                "Should include Monitor with 600.0");
        assertTrue(highValue.stream().anyMatch(r -> r.getProduct().equals("Desk") && r.getAmount() == 500.0),
                "Should include Desk with 500.0");
    }

    /**
     * Tests filterHighValueTransactions with a threshold that filters all records.
     * Should return an empty list when threshold is too high.
     */
    @Test
    public void testFilterHighValueTransactionsAllFiltered() {
        double threshold = 2000.0;
        List<SalesRecord> highValue = service.filterHighValueTransactions(testRecords, threshold);

        assertNotNull(highValue, "High-value transactions list should not be null");
        assertTrue(highValue.isEmpty(), "Should return empty list when threshold is too high");
    }

    /**
     * Tests filterHighValueTransactions with a threshold that includes all records.
     * Should return all records when threshold is very low.
     */
    @Test
    public void testFilterHighValueTransactionsNoneFiltered() {
        double threshold = 0.0;
        List<SalesRecord> highValue = service.filterHighValueTransactions(testRecords, threshold);

        assertNotNull(highValue, "High-value transactions list should not be null");
        assertEquals(testRecords.size(), highValue.size(),
                "Should return all records when threshold is 0.0");
    }

    /**
     * Tests filterHighValueTransactions with an empty list.
     * Should return an empty list.
     */
    @Test
    public void testFilterHighValueTransactionsEmptyList() {
        List<SalesRecord> emptyList = new ArrayList<>();
        List<SalesRecord> highValue = service.filterHighValueTransactions(emptyList, 500.0);

        assertNotNull(highValue, "High-value transactions list should not be null");
        assertTrue(highValue.isEmpty(), "Should return empty list for empty input");
    }

    /**
     * Tests filterHighValueTransactions with zero threshold.
     * Should include all records including those with zero amounts.
     */
    @Test
    public void testFilterHighValueTransactionsZeroThreshold() {
        List<SalesRecord> recordsWithZero = new ArrayList<>();
        recordsWithZero.add(new SalesRecord("ORD-401", "Product1", "Category1", "South America", 0.0, 1, "2025-01-01"));
        recordsWithZero.add(new SalesRecord("ORD-402", "Product2", "Category2", "Australia", 100.0, 1, "2025-01-02"));

        List<SalesRecord> highValue = service.filterHighValueTransactions(recordsWithZero, 0.0);
        assertEquals(2, highValue.size(), "Should include all records with zero threshold");
    }

    /**
     * Tests that maps returned by grouping methods are immutable.
     * Verifies that Map.copyOf() was used correctly.
     */
    @Test
    public void testMapsAreImmutable() {
        Map<String, Double> salesByCategory = service.getSalesByCategory(testRecords);

        // Attempting to modify the map should throw UnsupportedOperationException
        assertThrows(UnsupportedOperationException.class, () -> {
            salesByCategory.put("NewCategory", 100.0);
        }, "Map should be immutable and throw UnsupportedOperationException on modification");
    }
}
