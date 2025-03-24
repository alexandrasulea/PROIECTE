package dao;

import Model.Bill;
import Model.Order;
import Model.Client;
import Model.Product;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import static Connection.ConnectionFactory.getConnection;

/**
 * OrderDao este clasa responsabilă pentru gestionarea operațiunilor CRUD pentru entitatea Order.
 */
public class OrderDao {
    private static final Logger LOGGER = Logger.getLogger(OrderDao.class.getName());
    private static final String INSERT_QUERY = "INSERT INTO orders (client_id, product_id, quantity) VALUES (?, ?, ?)";
    private static final String FIND_BY_ID_QUERY = "SELECT * FROM orders WHERE order_id = ?";
    private static final String FIND_ALL_QUERY = "SELECT * FROM orders";
    private static final String UPDATE_QUERY = "UPDATE orders SET client_id = ?, product_id = ?, quantity = ? WHERE order_id = ?";
    private static final String DELETE_QUERY = "DELETE FROM orders WHERE order_id = ?";
    private static final String FIND_ALL_CLIENTS_QUERY = "SELECT * FROM Client";
    private static final String FIND_ALL_PRODUCTS_QUERY = "SELECT * FROM Product";
    private static final String UPDATE_PRODUCT_STOCK_QUERY = "UPDATE Product SET stock_quantity = ? WHERE product_id = ?";
    private static final String INSERT_BILL_QUERY = "INSERT INTO Log(order_id, client_id, product_id, quantity) VALUES (?, ?, ?, ?)";

    /**
     * Actualizează stocul unui produs.
     * @param product Produsul a cărui cantitate de stoc trebuie actualizată.
     */
    public void updateProductStock(Product product) {
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(UPDATE_PRODUCT_STOCK_QUERY)) {
            statement.setInt(1, product.getStockQuantity());
            statement.setInt(2, product.getProductId());
            statement.executeUpdate();
        } catch (SQLException e) {
            LOGGER.severe("Error updating product stock: " + e.getMessage());
        }
    }

    /**
     * Inserează o nouă comandă în baza de date.
     * @param order Comanda care trebuie inserată.
     */
    public void insertOrder(Order order) {
        if (order.getQuantity() <= 0) {
            LOGGER.warning("Order quantity must be positive.");
            return;
        }
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(INSERT_QUERY, Statement.RETURN_GENERATED_KEYS)) {
            statement.setInt(1, order.getClientId());
            statement.setInt(2, order.getProductId());
            statement.setInt(3, order.getQuantity());
            statement.executeUpdate();

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int orderId = generatedKeys.getInt(1);
                    Bill bill = new Bill(orderId, order.getClientId(), order.getProductId(), order.getQuantity());
                    insertBillIntoLog(bill);
                } else {
                    throw new SQLException("Creating order failed, no ID obtained.");
                }
            }
        } catch (SQLException e) {
            LOGGER.severe("Error inserting order: " + e.getMessage());
        }
    }

    /**
     * Găsește o comandă după ID-ul său.
     * @param orderId ID-ul comenzii care trebuie găsită.
     * @return Comanda găsită sau null dacă nu a fost găsită.
     */
    public Order findOrderById(int orderId) {
        Order order = null;
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(FIND_BY_ID_QUERY)) {
            statement.setInt(1, orderId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    order = new Order(
                            resultSet.getInt("order_id"),
                            resultSet.getInt("client_id"),
                            resultSet.getInt("product_id"),
                            resultSet.getInt("quantity"),
                            resultSet.getTimestamp("order_date")
                    );
                }
            }
        } catch (SQLException e) {
            LOGGER.severe("Error finding order by ID: " + e.getMessage());
        }
        return order;
    }

    /**
     * Găsește toate comenzile din baza de date.
     * @return Lista comenzilor găsite.
     */
    public List<Order> findAllOrders() {
        List<Order> orders = new ArrayList<>();
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(FIND_ALL_QUERY);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                Order order = new Order(
                        resultSet.getInt("order_id"),
                        resultSet.getInt("client_id"),
                        resultSet.getInt("product_id"),
                        resultSet.getInt("quantity"),
                        resultSet.getTimestamp("order_date")
                );
                orders.add(order);
            }
        } catch (SQLException e) {
            LOGGER.severe("Error finding all orders: " + e.getMessage());
        }
        return orders;
    }

    /**
     * Actualizează o comandă existentă în baza de date.
     * @param order Comanda care trebuie actualizată.
     */
    public void updateOrder(Order order) {
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(UPDATE_QUERY)) {
            statement.setInt(1, order.getClientId());
            statement.setInt(2, order.getProductId());
            statement.setInt(3, order.getQuantity());
            statement.setInt(4, order.getOrderId());
            statement.executeUpdate();
        } catch (SQLException e) {
            LOGGER.severe("Error updating order: " + e.getMessage());
        }
    }

    /**
     * Șterge o comandă din baza de date.
     * @param orderId ID-ul comenzii care trebuie ștearsă.
     */
    public void deleteOrder(int orderId) {
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(DELETE_QUERY)) {
            statement.setInt(1, orderId);
            statement.executeUpdate();
        } catch (SQLException e) {
            LOGGER.severe("Error deleting order: " + e.getMessage());
        }
    }

    /**
     * Găsește toți clienții din baza de date.
     * @return Lista clienților găsiți.
     */
    public List<Client> findAllClients() {
        List<Client> clients = new ArrayList<>();
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(FIND_ALL_CLIENTS_QUERY);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                Client client = new Client(
                        resultSet.getInt("client_id"),
                        resultSet.getString("name"),
                        resultSet.getString("email"),
                        resultSet.getString("phone")
                );
                clients.add(client);
            }
        } catch (SQLException e) {
            LOGGER.severe("Error finding all clients: " + e.getMessage());
        }
        return clients;
    }

    /**
     * Găsește toate produsele din baza de date.
     * @return Lista produselor găsite.
     */
    public List<Product> findAllProducts() {
        List<Product> products = new ArrayList<>();
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(FIND_ALL_PRODUCTS_QUERY);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                Product product = new Product(
                        resultSet.getInt("product_id"),
                        resultSet.getString("name"),
                        resultSet.getString("description"),
                        resultSet.getDouble("price"),
                        resultSet.getInt("stock_quantity")
                );
                products.add(product);
            }
        } catch (SQLException e) {
            LOGGER.severe("Error finding all products: " + e.getMessage());
        }
        return products;
    }

    /**
     * Creează o factură (Bill) dintr-o comandă.
     * @param order Comanda din care se creează factura.
     * @return Factura creată.
     */
    private Bill createBillFromOrder(Order order) {
        return new Bill(
                order.getOrderId(),
                order.getClientId(),
                order.getProductId(),
                order.getQuantity()
        );
    }

    /**
     * Inserează o factură în jurnal (log).
     * @param bill Factura care trebuie inserată în jurnal.
     */
    private void insertBillIntoLog(Bill bill) {
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(INSERT_BILL_QUERY)) {
            statement.setInt(1, bill.getOrderId());
            statement.setInt(2, bill.getClientId());
            statement.setInt(3, bill.getProductId());
            statement.setInt(4, bill.getQuantity());
            statement.executeUpdate();
        } catch (SQLException e) {
            LOGGER.severe("Error inserting bill into Log: " + e.getMessage());
        }
    }
}
