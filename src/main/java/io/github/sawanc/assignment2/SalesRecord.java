package io.github.sawanc.assignment2;

/**
 * Represents a sales record with product, category, region, and amount information.
 *
 * @author sawan chakraborty
 */
public class SalesRecord {
    private final String product;
    private final String category;
    private final String region;
    private final double amount;

    /**
     * Constructs a sales record.
     */
    public SalesRecord(String product, String category, String region, double amount) {
        this.product = product;
        this.category = category;
        this.region = region;
        this.amount = amount;
    }

    public String getProduct() {
        return product;
    }

    public String getCategory() {
        return category;
    }

    public String getRegion() {
        return region;
    }

    public double getAmount() {
        return amount;
    }

    @Override
    public String toString() {
        return String.format("SalesRecord{product='%s', category='%s', region='%s', amount=%.2f}",
                product, category, region, amount);
    }
}
