package Presentation;

import dao.OrderDao;
import Model.Client;
import Model.Order;
import Model.Product;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class OrderFrame extends JFrame {
    private JComboBox<Product> productComboBox;
    private JComboBox<Client> clientComboBox;
    private JTextField quantityField;
    private JLabel messageLabel;

    private OrderDao orderDao;
    /**
     * Constructor pentru inițializarea ferestrei OrderFrame.
     */
    public OrderFrame() {
        orderDao = new OrderDao();

        setTitle("Create Product Order");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(900, 900);
        setLocationRelativeTo(null);
        // Adăugarea unei imagini la fereastră
        String imageUrl = "https://static.vecteezy.com/system/resources/thumbnails/022/597/173/small_2x/3d-order-online-shop-png.png";
        try {
            ImageIcon imageIcon = new ImageIcon(new java.net.URL(imageUrl));
            JLabel imageLabel = new JLabel(imageIcon);
            getContentPane().add(imageLabel, BorderLayout.NORTH);
            getContentPane().setBackground(Color.WHITE);
        } catch (Exception e) {
            e.printStackTrace();
            JLabel errorLabel = new JLabel("Image not available", JLabel.CENTER);
            getContentPane().add(errorLabel, BorderLayout.NORTH);
        }
        // Inițializarea panoului principal
        JPanel mainPanel = new JPanel(new GridLayout(4, 2, 5, 5));
        productComboBox = new JComboBox<>();
        clientComboBox = new JComboBox<>();
        quantityField = new JTextField();
        JButton orderButton = new JButton("Place Order");
        orderButton.setBackground(Color.WHITE);
        orderButton.setForeground(Color.RED);
        orderButton.setFont(new Font("Times New Roman", Font.BOLD, 26));
        messageLabel = new JLabel();

        JLabel selectProductLabel = new JLabel("Select Product:");
        selectProductLabel.setForeground(Color.BLUE);
        selectProductLabel.setFont(new Font("Times New Roman", Font.BOLD, 26));
        mainPanel.add(selectProductLabel);
        mainPanel.add(productComboBox);

        JLabel selectClientLabel = new JLabel("Select Client:");
        selectClientLabel.setForeground(Color.BLUE);
        selectClientLabel.setFont(new Font("Times New Roman", Font.BOLD, 26));
        mainPanel.add(selectClientLabel);
        mainPanel.add(clientComboBox);

        JLabel quantityLabel = new JLabel("Quantity:");
        quantityLabel.setForeground(Color.BLUE);
        quantityLabel.setFont(new Font("Times New Roman", Font.BOLD, 26));
        mainPanel.add(quantityLabel);
        mainPanel.add(quantityField);

        mainPanel.add(orderButton);
        mainPanel.add(messageLabel);

        add(mainPanel);
        setLocationRelativeTo(null);

        populateComboBoxes();

        orderButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                placeOrder();
            }
        });
    }

    private void populateComboBoxes() {
        List<Client> clients = orderDao.findAllClients();
        for (Client client : clients) {
            clientComboBox.addItem(client);
        }

        List<Product> products = orderDao.findAllProducts();
        for (Product product : products) {
            productComboBox.addItem(product);
        }
    }

    private void placeOrder() {
        Client selectedClient = (Client) clientComboBox.getSelectedItem();
        Product selectedProduct = (Product) productComboBox.getSelectedItem();
        String quantityText = quantityField.getText();

        if (selectedClient == null || selectedProduct == null || quantityText.isEmpty()) {
            messageLabel.setText("Please fill in all fields.");
            return;
        }

        int quantity;
        try {
            quantity = Integer.parseInt(quantityText);
        } catch (NumberFormatException e) {
            messageLabel.setText("Invalid quantity.");
            return;
        }

        if (quantity > selectedProduct.getStockQuantity()) {
            messageLabel.setText("Insufficient stock quantity.");
            return;
        }

        // Actualizarea stocului produsului
        int newStockQuantity = selectedProduct.getStockQuantity() - quantity;
        selectedProduct.setStock_quantity(newStockQuantity);

        Order order = new Order(selectedClient.getClientId(), selectedProduct.getProductId(), quantity);

        try {
            // Actualizarea stocului în baza de date
            orderDao.updateProductStock(selectedProduct);

            // Inserarea comenzii în baza de date
            orderDao.insertOrder(order);
            messageLabel.setText("Order placed successfully.");
        } catch (Exception e) {
            e.printStackTrace();
            messageLabel.setText("Error placing order.");
        }
    }






    }



