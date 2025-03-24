package Model;

public class Product {
    private int product_id;
    private String name;
    private String description;
    private double price;
    private int stock_quantity;



    public Product() {
    }

    public Product(String name, String description, double price, int stock_quantity) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.stock_quantity = stock_quantity;
    }
    /**
     * Constructor pentru inițializarea unui produs.
     * @param name Numele produsului.
     * @param description Descrierea produsului.
     * @param price Prețul produsului.
     * @param stock_quantity Cantitatea de produs în stoc.
     */
    /**
     * Constructor pentru inițializarea unui produs cu ID.
     * @param product_id ID-ul produsului.
     * @param name Numele produsului.
     * @param description Descrierea produsului.
     * @param price Prețul produsului.
     * @param stock_quantity Cantitatea de produs în stoc.
     */

    public Product(int product_id, String name, String description, double price, int stock_quantity) {
        this.product_id = product_id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.stock_quantity = stock_quantity;
    }


    public int getProductId() {
        return product_id;
    }

    public void setProduct_id(int product_id) {
        this.product_id = product_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getStockQuantity() {
        return stock_quantity;
    }

    public void setStock_quantity(int quantity) {
        this.stock_quantity = quantity;
    }

    @Override
    public String toString() {
        return name + " - " + description;
    }


}
