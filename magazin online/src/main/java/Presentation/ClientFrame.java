package Presentation;

import Connection.ConnectionFactory;
import Model.Client;
import Model.Product;
import Start.ReflectionExample;
import com.mysql.cj.xdevapi.Statement;
import dao.ClientDao;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Clasa ClientFrame reprezintă o fereastră de interfață grafică pentru gestionarea operațiunilor legate de clienți.
 */
public class ClientFrame extends JFrame {

    private JButton addClientButton;
    private JButton editClientButton;
    private JButton deleteClientButton;
    private JButton viewClientsButton;
    private JTable clientTable;
    /**
     * Constructor pentru inițializarea ferestrei ClientFrame.
     */
    public ClientFrame() {
        super();
        setTitle("Client Operations");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(900, 900);
        setLocationRelativeTo(null);
        // Adăugarea unei imagini la fereastră
        String imageUrl = "https://media.licdn.com/dms/image/C4D12AQGjaSmqFZkq3g/article-cover_image-shrink_720_1280/0/1624371557124?e=2147483647&v=beta&t=i_71ctaIK1xXQZQItKjaQe81w6q1P_35UN3E3C2c9LU";

        try {
            ImageIcon imageIcon = new ImageIcon(new java.net.URL(imageUrl));
            JLabel imageLabel = new JLabel(imageIcon);
            getContentPane().add(imageLabel, BorderLayout.CENTER);
        } catch (Exception e) {
            e.printStackTrace();
            JLabel errorLabel = new JLabel("Image not available", JLabel.CENTER);
            getContentPane().add(errorLabel, BorderLayout.CENTER);
        }
        // Crearea butoanelor și adăugarea lor la panoul de butoane
        addClientButton = createRoundButton("ADD NEW CLIENT");
        addClientButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addClientAction();
            }
        });

        editClientButton = createRoundButton("EDIT CLIENT");
        editClientButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                editClientAction();
            }
        });

        deleteClientButton = createRoundButton("DELETE CLIENT");
        deleteClientButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteClientAction();
            }
        });
        viewClientsButton = createRoundButton("VIEW CLIENTS");
        viewClientsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                viewClientsAction();
            }
        });

/**
 * *   Crearea panoului de butoane și adăugarea lor la partea de jos a ferestrei
 */
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(1, 4, 15, 15));
        buttonPanel.add(addClientButton);
        buttonPanel.setBackground(new Color(64, 224, 208));
        buttonPanel.add(editClientButton);
        buttonPanel.add(deleteClientButton);
        buttonPanel.add(viewClientsButton);
/**
 *   Setarea aspectului butoanelor
 */
        addClientButton.setBackground(Color.white);
        editClientButton.setBackground(Color.white);
        deleteClientButton.setBackground(Color.white);
        viewClientsButton.setBackground(Color.white);

        addClientButton.setForeground(Color.RED);
        editClientButton.setForeground(Color.BLUE);
        deleteClientButton.setForeground(Color.ORANGE);
        viewClientsButton.setForeground(Color.GREEN);

        addClientButton.setFont(new Font("Times New Roman", Font.BOLD, 18));
        editClientButton.setFont(new Font("Times New Roman", Font.BOLD, 18));
        deleteClientButton.setFont(new Font("Times New Roman", Font.BOLD, 18));
        viewClientsButton.setFont(new Font("Times New Roman", Font.BOLD, 18));

        getContentPane().add(buttonPanel, BorderLayout.SOUTH);
    }
    /**
     * Metodă pentru crearea unui buton rotund cu text dat.
     * @param text Textul butonului.
     * @return Butonul creat.
     */
    private JButton createRoundButton(String text) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                if (getModel().isArmed()) {
                    g.setColor(Color.LIGHT_GRAY);
                } else {
                    g.setColor(getBackground());
                }
                g.fillOval(2, 2, getWidth(), getHeight());
                super.paintComponent(g);
            }

            @Override
            public Dimension getPreferredSize() {
                int size = 200;
                return new Dimension(size, size);
            }

            @Override
            protected void paintBorder(Graphics g) {
                g.setColor(getForeground());
                g.drawOval(0, 0, getWidth() - 1, getHeight() - 1);
            }

            @Override
            public boolean contains(int x, int y) {
                int radius = getWidth() / 2;
                return (x - radius) * (x - radius) + (y - radius) * (y - radius) <= radius * radius;
            }
        };
        button.setContentAreaFilled(false);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        return button;
    }
    // Metode pentru acțiunile butoanelor

    /**
     * Acțiunea de adăugare a unui client.
     */
    private void addClientAction() {
        AddClientDialog addClientDialog = new AddClientDialog();
        addClientDialog.setVisible(true);
    }

    /**
     * Acțiunea de editare a unui client.
     */

    private void editClientAction() {
        EditClientDialog editClientDialog = new EditClientDialog();
        editClientDialog.setVisible(true);
    }



/**
 * Acțiunea de ștergere a unui client.
 */
    private void deleteClientAction() {
        DeleteClientDialog deleteClientDialog = new DeleteClientDialog();
        deleteClientDialog.setVisible(true);
    }


    /**
     * Acțiunea de vizualizare a clienților.
     */

    private void viewClientsAction() {
        ViewClientDialog viewClientDialog = new ViewClientDialog();
        viewClientDialog.setVisible(true);
    }




    private class AddClientDialog extends JDialog {
        private JTextField clientNameField;
        private JTextField clientEmailField;
        private JTextField clientPhoneField;
        private JButton addButton;

        public AddClientDialog() {
            setTitle("Add Clients");
            setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
            setSize(900, 900);
            setLocationRelativeTo(null);
            setLayout(new BorderLayout());

            String imageUrl = "https://play-lh.googleusercontent.com/nEDyVELWukB6aT6_poLWuXWLbD4jU1UlByrKhhp4Fjie8GdrQ24kDLGcxhvSeNO5mbkU";

            try {
                ImageIcon imageIcon = new ImageIcon(new java.net.URL(imageUrl));
                JLabel imageLabel = new JLabel(imageIcon);
                getContentPane().add(imageLabel, BorderLayout.NORTH);
            } catch (Exception e) {
                e.printStackTrace();
                JLabel errorLabel = new JLabel("Image not available", JLabel.CENTER);
                getContentPane().add(errorLabel, BorderLayout.NORTH);
            }

            JPanel formPanel = new JPanel();
            formPanel.setLayout(new GridLayout(4, 2, 7, 7));

            JLabel clientNameLabel = new JLabel("Name:");
            clientNameField = new JTextField();
            JLabel clientEmailLabel = new JLabel("Email:");
            clientEmailField = new JTextField();
            JLabel clientPhoneLabel = new JLabel("Phone:");
            clientPhoneField = new JTextField();
            addButton = new JButton("Add");

            clientNameLabel.setFont(new Font("Times New Roman", Font.BOLD, 26));
            clientEmailLabel.setFont(new Font("Times New Roman", Font.BOLD, 26));
            clientPhoneLabel.setFont(new Font("Times New Roman", Font.BOLD, 26));

            formPanel.add(clientNameLabel);
            formPanel.add(clientNameField);
            formPanel.add(clientEmailLabel);
            formPanel.add(clientEmailField);
            formPanel.add(clientPhoneLabel);
            formPanel.add(clientPhoneField);
            formPanel.add(new JLabel());
            formPanel.add(addButton);

            getContentPane().add(formPanel, BorderLayout.CENTER);


            addButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    addClientToDatabase();
                }
            });
        }

        private void addClientToDatabase() {
            String name = clientNameField.getText();
            String email = clientEmailField.getText();
            String phone = clientPhoneField.getText();

            Client client = new Client(name, email, phone);
            int insertedId = ClientDao.insert(client);

            if (insertedId != -1) {
                JOptionPane.showMessageDialog(this, "Client added successfully with ID: " + insertedId);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Error adding client");
            }
        }
    }


    private class EditClientDialog extends JDialog {
        private JTextField editIdField;
        private JTextField editNameField;
        private JTextField editEmailField;
        private JTextField editPhoneField;

        public EditClientDialog() {
            setTitle("Edit Client");
            setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
            setSize(900, 900);
            setLocationRelativeTo(null);
            setLayout(new GridLayout(5, 2, 7, 7));

            JLabel editIdLabel = new JLabel("ID:");
            editIdField = new JTextField();
            JLabel editNameLabel = new JLabel("Edit Name:");
            editNameField = new JTextField();
            JLabel editEmailLabel = new JLabel("Edit Email:");
            editEmailField = new JTextField();
            JLabel editPhoneLabel = new JLabel("Edit Phone:");
            editPhoneField = new JTextField();
            JButton editButton = new JButton("Edit");

            add(editIdLabel);
            add(editIdField);
            add(editNameLabel);
            add(editNameField);
            add(editEmailLabel);
            add(editEmailField);
            add(editPhoneLabel);
            add(editPhoneField);
            add(new JLabel());
            add(editButton);

            editButton.addActionListener(e -> {
                try {
                    int id = Integer.parseInt(editIdField.getText());
                    String name = editNameField.getText();
                    String email = editEmailField.getText();
                    String phone = editPhoneField.getText();

                    Client client = new Client(id, name, email, phone);
                    boolean isUpdated = ClientDao.update(client);
                    if (isUpdated) {
                        JOptionPane.showMessageDialog(this, "Client updated successfully.");
                        dispose();
                    } else {
                        JOptionPane.showMessageDialog(this, "Error updating client. Client ID may not exist.");
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "Invalid input format", "Error", JOptionPane.ERROR_MESSAGE);
                }
            });
        }
    }





    private class DeleteClientDialog extends JDialog {
        private JTextField clientIdField;
        private JButton deleteButton;

        public DeleteClientDialog() {
            setTitle("Delete Client");
            setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
            setSize(900, 900);
            setLocationRelativeTo(null);
            setLayout(new BorderLayout());

            String imageUrl = "https://cdn-icons-png.freepik.com/512/153/153630.png";

            try {
                ImageIcon imageIcon = new ImageIcon(new java.net.URL(imageUrl));
                JLabel imageLabel = new JLabel(imageIcon);
                getContentPane().add(imageLabel, BorderLayout.NORTH);
            } catch (Exception e) {
                e.printStackTrace();
                JLabel errorLabel = new JLabel("Image not available", JLabel.CENTER);
                getContentPane().add(errorLabel, BorderLayout.NORTH);
            }

            JPanel formPanel = new JPanel();
            formPanel.setLayout(new GridLayout(2, 2, 7, 7));

            JLabel clientIdLabel = new JLabel("Client ID:");
            clientIdField = new JTextField();
            deleteButton = new JButton("Delete");

            clientIdLabel.setFont(new Font("Times New Roman", Font.BOLD, 26));
            formPanel.add(clientIdLabel);
            formPanel.add(clientIdField);
            formPanel.add(new JLabel());
            formPanel.add(deleteButton);

            getContentPane().add(formPanel, BorderLayout.CENTER);


            deleteButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    deleteClientFromDatabase();
                }
            });
        }

        private void deleteClientFromDatabase() {
            try {
                int clientId = Integer.parseInt(clientIdField.getText());
                boolean isDeleted = ClientDao.delete(clientId);

                if (isDeleted) {
                    JOptionPane.showMessageDialog(this, "Client deleted successfully.");
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(this, "Error deleting client. Client ID may not exist.");
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Please enter a valid Client ID.");
            }
        }

    }





    public class ViewClientDialog extends JDialog {
        private JTable table;

        public ViewClientDialog() {
            setTitle("View Clients");
            setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
            setSize(900, 900);
            setLocationRelativeTo(null);
            setLayout(new BorderLayout());



            DefaultTableModel model = new DefaultTableModel();


            for(String s: ReflectionExample.retrieveProperties(new Client(1, "Popescu ioan ","iaona@yahoo.com","0976435789"))) {
                model.addColumn(s);
            }


            try (Connection connection = ConnectionFactory.getConnection();
                 java.sql.Statement statement = connection.createStatement();
                 ResultSet resultSet = statement.executeQuery("SELECT * FROM Client")) {


                while (resultSet.next()) {
                    int id = resultSet.getInt("client_id");
                    String name = resultSet.getString("name");
                    String email = resultSet.getString("email");
                    String phone = resultSet.getString("phone");
                    model.addRow(new Object[]{id, name, email, phone});
                }
            } catch (SQLException e) {
                Logger.getLogger(ViewClientDialog.class.getName()).log(Level.SEVERE, null, e);
                JOptionPane.showMessageDialog(this, "Error fetching clients from database: " + e.getMessage());
            }


            table = new JTable(model);


            JScrollPane scrollPane = new JScrollPane(table);


            getContentPane().add(scrollPane, BorderLayout.CENTER);
        }
    }





}