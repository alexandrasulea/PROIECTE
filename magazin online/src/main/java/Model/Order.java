package Model;

import java.sql.Timestamp;

public class Order {
    private int orderId;
    private int clientId;
    private int productId;
    private int quantity;
    private Timestamp orderDate;

    public Order(int orderId, int clientId, int productId, int quantity, Timestamp orderDate) {
        this.orderId = orderId;
        this.clientId = clientId;
        this.productId = productId;
        this.quantity = quantity;
        this.orderDate = orderDate;
    }


    public Order(int clientId, int productId, int quantity) {
        this.clientId = clientId;
        this.productId = productId;
        this.quantity = quantity;
    }

    public int getOrderId() {
        return orderId;
    }

    public int getClientId() {
        return clientId;
    }

    public int getProductId() {
        return productId;
    }

    public int getQuantity() {
        return quantity;
    }

    public Timestamp getOrderDate() {
        return orderDate;
    }
    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public void setClientId(int clientId) {
        this.clientId = clientId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setOrderDate(Timestamp orderDate) {
        this.orderDate = orderDate;
    }
    /**
     * Constructor pentru inițializarea unei comenzi.
     * @param orderId ID-ul comenzii.
     * @param clientId ID-ul clientului.
     * @param productId ID-ul produsului.
     * @param quantity Cantitatea de produs.
     * @param orderDate Data comenzii.
     */
}
