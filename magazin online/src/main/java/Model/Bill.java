package Model;

/**
 * Reprezintă o factură pentru o comandă.
 */
public class Bill {
    private int orderId;
    private int clientId;
    private int productId;
    private int quantity;

    /**
     * Constructor pentru inițializarea unei facturi.
     * @param orderId ID-ul comenzii.
     * @param clientId ID-ul clientului.
     * @param productId ID-ul produsului.
     * @param quantity Cantitatea de produs.
     */
    public Bill(int orderId, int clientId, int productId, int quantity) {
        this.orderId = orderId;
        this.clientId = clientId;
        this.productId = productId;
        this.quantity = quantity;
    }

    public int getOrderId() { return orderId; }
    public void setOrderId(int orderId) { this.orderId = orderId; }

    public int getClientId() { return clientId; }
    public void setClientId(int clientId) { this.clientId = clientId; }

    public int getProductId() { return productId; }
    public void setProductId(int productId) { this.productId = productId; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
}
