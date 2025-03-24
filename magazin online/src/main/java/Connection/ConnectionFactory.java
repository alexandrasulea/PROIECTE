package Connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Fabrica de conexiuni cu baza de date.
 */
public class ConnectionFactory {

    private static final Logger LOGGER = Logger.getLogger(ConnectionFactory.class.getName());
    private static final String DRIVER = "com.mysql.cj.jdbc.Driver";
    private static final String DBURL = "jdbc:mysql://localhost:3306/tp3tema";
    private static final String USER = "root";
    private static final String PASS = "13Septembrie";

    private static ConnectionFactory singleInstance = new ConnectionFactory();

    private ConnectionFactory() {
        try {
            Class.forName(DRIVER);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private Connection createConnection() {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(DBURL, USER, PASS);
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, "A apărut o eroare la conectarea la baza de date", e);
            e.printStackTrace();
        }
        return connection;
    }

    /**
     * Obține o conexiune la baza de date.
     * @return Conexiunea la baza de date.
     */
    public static Connection getConnection() {
        return singleInstance.createConnection();
    }

    /**
     * Închide o conexiune la baza de date.
     * @param connection Conexiunea de închis.
     */
    public static void close(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                LOGGER.log(Level.WARNING, "A apărut o eroare la închiderea conexiunii");
            }
        }
    }

    /**
     * Închide un statement.
     * @param statement Statement-ul de închis.
     */
    public static void close(Statement statement) {
        if (statement != null) {
            try {
                statement.close();
            } catch (SQLException e) {
                LOGGER.log(Level.WARNING, "A apărut o eroare la închiderea statement-ului");
            }
        }
    }

    /**
     * Închide un ResultSet.
     * @param resultSet ResultSet-ul de închis.
     */
    public static void close(ResultSet resultSet) {
        if (resultSet != null) {
            try {
                resultSet.close();
            } catch (SQLException e) {
                LOGGER.log(Level.WARNING, "A apărut o eroare la închiderea ResultSet-ului");
            }
        }
    }

    /**
     * Verifică dacă există o conexiune la baza de date.
     * @return true dacă există o conexiune, false altfel.
     */
    public static boolean isDatabaseConnected() {
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            connection = getConnection();
            if (connection != null) {
                statement = connection.createStatement();
                resultSet = statement.executeQuery("SELECT 1");
                if (resultSet.next()) {
                    return true;
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, "A apărut o eroare la verificarea conexiunii cu baza de date", e);
        } finally {
            close(resultSet);
            close(statement);
            close(connection);
        }
        return false;
    }

    /**
     * Verifică dacă baza de date este conectată și afișează un mesaj corespunzător.
     * @param args Argumentele liniei de comandă (nu sunt utilizate).
     */
    public static void main(String[] args) {
        boolean isConnected = isDatabaseConnected();
        if (isConnected) {
            System.out.println("Baza de date este conectată.");
        } else {
            System.out.println("Baza de date nu este conectată.");
        }
    }
}
