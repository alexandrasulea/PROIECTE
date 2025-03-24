package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import Connection.ConnectionFactory;
import Model.Client;
import bll.ClientBLL;
import bll.Validators.Validator;

/**
 * Data Access Object (DAO) pentru operațiunile legate de client în baza de date.
 */
public class ClientDao {

    private static final Logger LOGGER = Logger.getLogger(ClientDao.class.getName());
    private static final String INSERT_QUERY = "INSERT INTO Client (name, email, phone) VALUES (?, ?, ?)";
    private static final String FIND_BY_ID_QUERY = "SELECT * FROM Client WHERE client_id = ?";
    private static final String FIND_ALL_QUERY = "SELECT * FROM Client";
    private static final String UPDATE_QUERY = "UPDATE Client SET name = ?, email = ?, phone = ? WHERE client_id = ?";
    private static final String DELETE_QUERY = "DELETE FROM Client WHERE client_id = ?";

    /**
     * Găsește un client din baza de date după ID-ul său.
     * @param id ID-ul clientului de căutat.
     * @return Clientul găsit sau null dacă nu există.
     */
    public static Client findById(int id) {
        Client client = null;
        String query = "SELECT * FROM Client WHERE client_id = ?";
        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, id);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    String name = resultSet.getString("name");
                    String email = resultSet.getString("email");
                    String phone = resultSet.getString("phone");
                    client = new Client(id, name, email, phone);
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Eroare la găsirea clientului după ID: " + e.getMessage());
        }
        return client;
    }

    /**
     * Returnează o listă cu toți clienții din baza de date.
     * @return Lista de clienți.
     */
    public static List<Client> findAll() {
        List<Client> clients = new ArrayList<>();
        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(FIND_ALL_QUERY)) {

            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                int clientId = resultSet.getInt("client_id");
                String name = resultSet.getString("name");
                String email = resultSet.getString("email");
                String phone = resultSet.getString("phone");
                Client client = new Client(clientId, name, email, phone);
                clients.add(client);
            }
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, "Eroare la obținerea tuturor clienților: " + e.getMessage());
        }
        return clients;
    }

    /**
     * Inserează un client în baza de date.
     * @param client Clientul de inserat.
     * @return ID-ul clientului inserat sau -1 în caz de eroare.
     */
    public static int insert(Client client) {
        String query = "INSERT INTO Client (name, email, phone) VALUES (?, ?, ?)";
        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, client.getName());
            preparedStatement.setString(2, client.getEmail());
            preparedStatement.setString(3, client.getPhone());
            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        return generatedKeys.getInt(1);
                    }
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Eroare la inserarea clientului: " + e.getMessage());
        }
        return -1;
    }

    /**
     * Actualizează informațiile unui client în baza de date.
     * @param client Clientul de actualizat.
     * @return true dacă actualizarea s-a efectuat cu succes, false altfel.
     */
    public static boolean update(Client client) {
        String query = "UPDATE Client SET name = ?, email = ?, phone = ? WHERE client_id = ?";
        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, client.getName());
            preparedStatement.setString(2, client.getEmail());
            preparedStatement.setString(3, client.getPhone());
            preparedStatement.setInt(4, client.getId());
            int affectedRows = preparedStatement.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Eroare la actualizarea clientului: " + e.getMessage());
        }
        return false;
    }

    /**
     * Șterge un client din baza de date după ID-ul său.
     * @param id ID-ul clientului de șters.
     * @return true dacă ștergerea s-a efectuat cu succes, false altfel.
     */
    public static boolean delete(int id) {
        String query = "DELETE FROM Client WHERE client_id = ?";
        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, id);
            int affectedRows = preparedStatement.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Eroare la ștergerea clientului: " + e.getMessage());
        }
        return false;
    }
}
