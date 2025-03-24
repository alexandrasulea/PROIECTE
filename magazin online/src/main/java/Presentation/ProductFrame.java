package Presentation;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import Connection.ConnectionFactory;
import Model.Client;
import Start.ReflectionExample;
import dao.ProductDao;
import Model.Product;
/**
 * Clasa ProductFrame reprezintă o fereastră de interfață grafică pentru operațiile legate de produse.
 */
public class ProductFrame extends JFrame {
    /**
     * Constructorul clasei ProductFrame.
     */
    public ProductFrame() {
        super();
        setTitle("Product Operations");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(900, 900);
        setLocationRelativeTo(null);

        String imageUrl = "https://s3.amazonaws.com/www-inside-design/uploads/2018/12/The-product-of-you-810x810.png";

        try {
            ImageIcon imageIcon = new ImageIcon(new java.net.URL(imageUrl));
            JLabel imageLabel = new JLabel(imageIcon);
            getContentPane().add(imageLabel, BorderLayout.CENTER);
        } catch (Exception e) {
            e.printStackTrace();
            JLabel errorLabel = new JLabel("Image not available", JLabel.CENTER);
            getContentPane().add(errorLabel, BorderLayout.CENTER);
        }

        JButton addProduct = createRoundButton("ADD NEW PRODUCT");
        JButton editProduct = createRoundButton("EDIT PRODUCT");
        JButton deleteProduct = createRoundButton("DELETE PRODUCT");
        JButton viewProduct = createRoundButton("VIEW PRODUCTS");

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(1, 4, 15, 15));
        buttonPanel.add(addProduct);
        buttonPanel.setBackground(new Color(64, 224, 208));
        buttonPanel.add(editProduct);
        buttonPanel.add(deleteProduct);
        buttonPanel.add(viewProduct);
        addProduct.setBackground(Color.WHITE);
        editProduct.setBackground(Color.WHITE);
        deleteProduct.setBackground(Color.WHITE);
        viewProduct.setBackground(Color.WHITE);
        addProduct.setForeground(Color.RED);
        editProduct.setForeground(Color.BLUE);
        deleteProduct.setForeground(Color.ORANGE);
        viewProduct.setForeground(Color.GREEN);
        addProduct.setFont(new Font("Times New Roman", Font.BOLD, 16));
        editProduct.setFont(new Font("Times New Roman", Font.BOLD, 18));
        deleteProduct.setFont(new Font("Times New Roman", Font.BOLD, 18));
        viewProduct.setFont(new Font("Times New Roman", Font.BOLD, 18));

        getContentPane().add(buttonPanel, BorderLayout.SOUTH);

        addProduct.addActionListener(e -> addProductAction());
        editProduct.addActionListener(e -> editProductAction());
        deleteProduct.addActionListener(e -> deleteProductAction());
        viewProduct.addActionListener(e -> viewProductAction());
    }
    /**
     * Acțiunea de vizualizare a produselor.
     */
    private void viewProductAction() {
        ViewProductDialog viewProductDialog = new ViewProductDialog();
        viewProductDialog.setVisible(true);
    }

    /**
     * Metodă pentru crearea unui buton rotund.
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

    /**
     * Acțiunea de adăugare a unui produs.
     */
    private void addProductAction() {
        AddProductDialog addProductDialog = new AddProductDialog();
        addProductDialog.setVisible(true);
    }

    /**
     * Acțiunea de editare a unui produs.
     */
    private void editProductAction() {
        EditProductDialog editProductDialog = new EditProductDialog();
        editProductDialog.setVisible(true);
    }
    /**
     * Acțiunea de ștergere a unui produs.
     */
    private void deleteProductAction() {
        DeleteProductDialog deleteProductDialog = new DeleteProductDialog();
        deleteProductDialog.setVisible(true);
    }

    /**
     * Clasa internă pentru fereastra de adăugare a unui produs.
     */
    private class AddProductDialog extends JDialog {
        private JTextField productNameField;
        private JTextField productDescriptionField;
        private JTextField productPriceField;
        private JTextField productQuantityField;

        public AddProductDialog() {
            setTitle("Add Product");
            setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
            setSize(900, 900);
            setLocationRelativeTo(null);
            setLayout(new GridLayout(5, 2, 7, 7));

            JLabel productNameLabel = new JLabel("Name:");
            productNameField = new JTextField();
            JLabel productDescriptionLabel = new JLabel("Description:");
            productDescriptionField = new JTextField();
            JLabel productPriceLabel = new JLabel("Price:");
            productPriceField = new JTextField();
            JLabel productQuantityLabel = new JLabel("Quantity:");
            productQuantityField = new JTextField();
            JButton addButton = new JButton("Add");

            add(productNameLabel);
            add(productNameField);
            add(productDescriptionLabel);
            add(productDescriptionField);
            add(productPriceLabel);
            add(productPriceField);
            add(productQuantityLabel);
            add(productQuantityField);
            add(new JLabel());
            add(addButton);

            addButton.addActionListener(e -> {
                try {
                    String name = productNameField.getText();
                    String description = productDescriptionField.getText();
                    double price = Double.parseDouble(productPriceField.getText());
                    int quantity = Integer.parseInt(productQuantityField.getText());

                    Product product = new Product(name, description, price, quantity);
                    ProductDao.insert(product);
                    dispose();
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "Invalid input format", "Error", JOptionPane.ERROR_MESSAGE);
                }
            });
        }
    }


    /**
     * Clasa internă pentru fereastra de editare a unui produs.
     */
    private class EditProductDialog extends JDialog {
        private JTextField editIdField;
        private JTextField editNameField;
        private JTextField editDescriptionField;
        private JTextField editPriceField;
        private JTextField editQuantityField;

        public EditProductDialog() {
            setTitle("Edit Product");
            setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
            setSize(900, 900);
            setLocationRelativeTo(null);
            setLayout(new GridLayout(6, 2, 7, 7));

            JLabel editIdLabel = new JLabel("ID:");
            editIdField = new JTextField();
            JLabel editNameLabel = new JLabel("Edit Name:");
            editNameField = new JTextField();
            JLabel editDescriptionLabel = new JLabel("Edit Description:");
            editDescriptionField = new JTextField();
            JLabel editPriceLabel = new JLabel("Edit Price:");
            editPriceField = new JTextField();
            JLabel editQuantityLabel = new JLabel("Edit Quantity:");
            editQuantityField = new JTextField();
            JButton editButton = new JButton("Edit");
            add(editIdLabel);
            add(editIdField);
            add(editNameLabel);
            add(editNameField);
            add(editDescriptionLabel);
            add(editDescriptionField);
            add(editPriceLabel);
            add(editPriceField);
            add(editQuantityLabel);
            add(editQuantityField);
            add(new JLabel());
            add(editButton);

            editButton.addActionListener(e -> {
                try {
                    int id = Integer.parseInt(editIdField.getText());
                    String name = editNameField.getText();
                    String description = editDescriptionField.getText();
                    double price = Double.parseDouble(editPriceField.getText());
                    int quantity = Integer.parseInt(editQuantityField.getText());

                    Product product = new Product(id, name, description, price, quantity);
                    ProductDao.update(product);
                    dispose();
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "Invalid input format", "Error", JOptionPane.ERROR_MESSAGE);
                }
            });
        }
    }

    private class DeleteProductDialog extends JDialog {
        private JTextField deleteIdField;

        public DeleteProductDialog() {
            setTitle("Delete Product");
            setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
            setSize(300, 150);
            setLocationRelativeTo(null);
            setLayout(new GridLayout(2, 2, 7, 7));

            JLabel deleteIdLabel = new JLabel("ID:");
            deleteIdField = new JTextField();
            JButton deleteButton = new JButton("Delete");

            add(deleteIdLabel);
            add(deleteIdField);
            add(new JLabel());
            add(deleteButton);

            deleteButton.addActionListener(e -> {
                try {
                    int id = Integer.parseInt(deleteIdField.getText());
                    boolean deleted = ProductDao.delete(id);
                    if (deleted) {
                        JOptionPane.showMessageDialog(this, "Product deleted successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(this, "Product not found or deletion failed", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                    dispose();
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "Invalid input format", "Error", JOptionPane.ERROR_MESSAGE);
                }
            });
        }
    }

    public class ViewProductDialog extends JDialog {
        private JTable table;

        public ViewProductDialog() {
            setTitle("View Products");
            setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
            setSize(900, 900);
            setLocationRelativeTo(null);
            setLayout(new BorderLayout());




            DefaultTableModel model = new DefaultTableModel();



            for(String s: ReflectionExample.retrieveProperties(new Product("product4", "produs",100,50))) {
                model.addColumn(s);
            }

            try (Connection connection = ConnectionFactory.getConnection();
                 java.sql.Statement statement = connection.createStatement();
                 ResultSet resultSet = statement.executeQuery("SELECT * FROM Product")) {


                while (resultSet.next()) {
                    int id = resultSet.getInt("product_id");
                    String name = resultSet.getString("name");
                    String description = resultSet.getString("description");
                    double price = resultSet.getDouble("price");
                    int stockQuantity = resultSet.getInt("stock_quantity");
                    model.addRow(new Object[]{id, name, description, price, stockQuantity});
                }
            } catch (SQLException e) {
                Logger.getLogger(ViewProductDialog.class.getName()).log(Level.SEVERE, null, e);
                JOptionPane.showMessageDialog(this, "Error fetching products from database: " + e.getMessage());
            }


            table = new JTable(model);
            JScrollPane scrollPane = new JScrollPane(table);
            getContentPane().add(scrollPane, BorderLayout.CENTER);
        }
    }



}
