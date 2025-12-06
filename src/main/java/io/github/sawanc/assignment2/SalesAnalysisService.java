package io.github.sawanc.assignment2;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Service class for analyzing sales data.
 *
 * @author sawan chakraborty
 */
public class SalesAnalysisService {

    /**
     * Calculates the total sales amount across all records.
     *
     * @param records the list of sales records
     * @return the total sales amount
     */
    public double getTotalSales(List<SalesRecord> records) {
        return records.stream()
                .mapToDouble(SalesRecord::getAmount)
                .sum();
    }

    /**
     * Groups sales by category and calculates the total for each category.
     *
     * @param records the list of sales records
     * @return map of category to total sales
     */
    public Map<String, Double> getSalesByCategory(List<SalesRecord> records) {
        Map<String, Double> salesByCategory = records.stream()
                .collect(Collectors.groupingBy(
                        SalesRecord::getCategory,
                        Collectors.summingDouble(SalesRecord::getAmount)
                ));
        return Map.copyOf(salesByCategory);
    }

    /**
     * Groups sales by region and calculates the total for each region.
     *
     * @param records the list of sales records
     * @return map of region to total sales
     */
    public Map<String, Double> getSalesByRegion(List<SalesRecord> records) {
        Map<String, Double> salesByRegion = records.stream()
                .collect(Collectors.groupingBy(
                        SalesRecord::getRegion,
                        Collectors.summingDouble(SalesRecord::getAmount)
                ));
        return Map.copyOf(salesByRegion);
    }

    /**
     * Calculates the average sales amount per product.
     *
     * @param records the list of sales records
     * @return map of product to average sales
     */
    public Map<String, Double> getAverageSalesPerProduct(List<SalesRecord> records) {
        Map<String, Double> avgSalesByProduct = records.stream()
                .collect(Collectors.groupingBy(
                        SalesRecord::getProduct,
                        Collectors.averagingDouble(SalesRecord::getAmount)
                ));
        return Map.copyOf(avgSalesByProduct);
    }

    /**
     * Filters sales records by minimum amount threshold.
     *
     * @param records   the list of sales records
     * @param threshold the minimum amount threshold
     * @return list of sales records meeting the threshold
     */
    public List<SalesRecord> filterHighValueTransactions(List<SalesRecord> records, double threshold) {
        return records.stream()
                .filter(record -> record.getAmount() >= threshold)
                .collect(Collectors.toList());
    }

    /**
     * Prints a comprehensive summary of sales data analysis.
     *
     * @param records the list of sales records to analyze
     */
    public void printSummary(List<SalesRecord> records) {
        System.out.println("=".repeat(60));
        System.out.println("SALES ANALYSIS SUMMARY");
        System.out.println("=".repeat(60));

        // 1. Total Sales
        double totalSales = getTotalSales(records);
        System.out.printf("\n1. TOTAL SALES: $%.2f\n", totalSales);

        // 2. Sales by Category
        System.out.println("\n2. SALES BY CATEGORY:");
        Map<String, Double> salesByCategory = getSalesByCategory(records);
        salesByCategory.forEach((category, amount) ->
                System.out.printf("   %-15s: $%.2f\n", category, amount));

        // 3. Sales by Region
        System.out.println("\n3. SALES BY REGION:");
        Map<String, Double> salesByRegion = getSalesByRegion(records);
        salesByRegion.forEach((region, amount) ->
                System.out.printf("   %-15s: $%.2f\n", region, amount));

        // 4. Average Sales per Product
        System.out.println("\n4. AVERAGE SALES PER PRODUCT:");
        Map<String, Double> avgSalesPerProduct = getAverageSalesPerProduct(records);
        avgSalesPerProduct.forEach((product, avgAmount) ->
                System.out.printf("   %-15s: $%.2f\n", product, avgAmount));

        // 5. High-Value Transactions (threshold = 500.0)
        double threshold = 500.0;
        System.out.printf("\n5. HIGH-VALUE TRANSACTIONS (>= $%.2f):\n", threshold);
        List<SalesRecord> highValueTransactions = filterHighValueTransactions(records, threshold);
        if (highValueTransactions.isEmpty()) {
            System.out.println("   No high-value transactions found.");
        } else {
            highValueTransactions.forEach(record ->
                    System.out.printf("   %s\n", record));
        }

        System.out.println("\n" + "=".repeat(60));
    }
}
