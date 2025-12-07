package io.github.sawanc.assignment2;

/**
 * Represents a sales record with order details.
 *
 * @author sawan chakraborty
 */
public class SalesRecord {
    private final String orderId;
    private final String product;
    private final String category;
    private final String region;
    private final double amount;
    private final int quantity;
    private final String orderDate;

    /**
     * Constructs a sales record.
     */
    public SalesRecord(String orderId, String product, String category, String region, 
                       double amount, int quantity, String orderDate) {
        this.orderId = orderId;
        this.product = product;
        this.category = category;
        this.region = region;
        this.amount = amount;
        this.quantity = quantity;
        this.orderDate = orderDate;
    }

    public String getOrderId() {
        return orderId;
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

    public int getQuantity() {
        return quantity;
    }

    public String getOrderDate() {
        return orderDate;
    }

    @Override
    public String toString() {
        return String.format("SalesRecord{orderId='%s', product='%s', category='%s', region='%s', amount=%.2f, quantity=%d, date='%s'}",
                orderId, product, category, region, amount, quantity, orderDate);
    }
}
