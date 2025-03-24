package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import Connection.ConnectionFactory;
import Model.Product;

/**
 * Data Access Object (DAO) pentru operațiile cu produsele din baza de date.
 */
public class ProductDao {

    private static final Logger LOGGER = Logger.getLogger(ProductDao.class.getName());
    private static final String INSERT_QUERY = "INSERT INTO Product (name, description, price, stock_quantity) VALUES (?, ?, ?, ?)";
    private static final String FIND_BY_ID_QUERY = "SELECT * FROM Product WHERE product_id = ?";
    private static final String FIND_ALL_QUERY = "SELECT * FROM Product";
    private static final String UPDATE_QUERY = "UPDATE Product SET name = ?, description = ?, price = ?, stock_quantity = ? WHERE product_id = ?";
    private static final String DELETE_QUERY = "DELETE FROM Product WHERE product_id = ?";

    /**
     * Găsește un produs după ID-ul său.
     * @param id ID-ul produsului de găsit.
     * @return Produsul găsit sau null dacă nu a fost găsit.
     */
    public static Product findById(int id) {
        Product product = null;
        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_BY_ID_QUERY)) {
            preparedStatement.setInt(1, id);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    String name = resultSet.getString("name");
                    String description = resultSet.getString("description");
                    double price = resultSet.getDouble("price");
                    int quantity = resultSet.getInt("stock_quantity");
                    product = new Product(id, name, description, price, quantity);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return product;
    }

    /**
     * Returnează o listă cu toate produsele din baza de date.
     * @return Lista de produse.
     */
    public static List<Product> findAll() {
        List<Product> products = new ArrayList<>();
        try (Connection connection = ConnectionFactory.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(FIND_ALL_QUERY)) {
            while (resultSet.next()) {
                int id = resultSet.getInt("product_id");
                String name = resultSet.getString("name");
                String description = resultSet.getString("description");
                double price = resultSet.getDouble("price");
                int quantity = resultSet.getInt("stock_quantity");
                Product product = new Product(id, name, description, price, quantity);
                products.add(product);
            }
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, "ProductDao:findAll " + e.getMessage());
        }
        return products;
    }

    /**
     * Inserează un produs în baza de date.
     * @param product Produsul de inserat.
     * @return ID-ul produsului inserat sau -1 în caz de eroare.
     */
    public static int insert(Product product) {
        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(INSERT_QUERY, PreparedStatement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, product.getName());
            preparedStatement.setString(2, product.getDescription());
            preparedStatement.setDouble(3, product.getPrice());
            preparedStatement.setInt(4, product.getStockQuantity());
            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        return generatedKeys.getInt(1);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    /**
     * Șterge un produs din baza de date.
     * @param productId ID-ul produsului de șters.
     * @return true dacă ștergerea a avut succes, false altfel.
     */
    public static boolean delete(int productId) {
        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(DELETE_QUERY)) {
            preparedStatement.setInt(1, productId);
            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, "ProductDao:delete " + e.getMessage());
            return false;
        }
    }

    /**
     * Vizualizează detalii despre un produs.
     * @param id ID-ul produsului de vizualizat.
     * @return Produsul sau null dacă nu a fost găsit.
     */
    public static Product view(int id) {
        Product product = null;
        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_BY_ID_QUERY)) {
            preparedStatement.setInt(1, id);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    String name = resultSet.getString("name");
                    String description = resultSet.getString("description");
                    double price = resultSet.getDouble("price");
                    int quantity = resultSet.getInt("stock_quantity");
                    product = new Product(id, name, description, price, quantity);
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, "ProductDao:view " + e.getMessage());
        }
        return product;
    }

    /**
     * Actualizează informațiile despre un produs în baza de date.
     * @param product Produsul de actualizat.
     */
    public static void update(Product product) {
        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_QUERY)) {
            preparedStatement.setString(1, product.getName());
            preparedStatement.setString(2, product.getDescription());
            preparedStatement.setDouble(3, product.getPrice());
            preparedStatement.setInt(4, product.getStockQuantity());
            preparedStatement.setInt(5, product.getProductId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, "ProductDao:update " + e.getMessage());
        }
    }

}
