package Start;

import Presentation.ClientFrame;
import Presentation.ProductFrame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Clasa Start reprezintă punctul de intrare în aplicație și conține fereastra principală a aplicației.
 */
public class Start extends JFrame {

    /**
     * Constructorul clasei Start.
     */
    public Start() {
        super();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 900);
        setLocationRelativeTo(null);

        // URL-ul imaginii de fundal
        String imageUrl = "https://media.licdn.com/dms/image/D4D12AQG5YslGFJE8Dg/article-cover_image-shrink_720_1280/0/1675306509536?e=2147483647&v=beta&t=HuOKD-WE574v-i5Xi_ubxihrNT1IkJgGg20ozToK79E";
/**
 *    Încărcarea imaginii de fundal și adăugarea acesteia în cadrul ferestrei principale
 */
        try {


            ImageIcon imageIcon = new ImageIcon(new java.net.URL(imageUrl));
            JLabel imageLabel = new JLabel(imageIcon);
            getContentPane().add(imageLabel, BorderLayout.CENTER);
        } catch (Exception e) {
            e.printStackTrace();
            JLabel errorLabel = new JLabel("Image not available", JLabel.CENTER);
            getContentPane().add(errorLabel, BorderLayout.CENTER);
        }

        /**
         *    Crearea butoanelor pentru navigare
          */

        JButton clientButton = createRoundButton("CLIENT");
        JButton productButton = createRoundButton("PRODUCT");
        JButton orderButton = createRoundButton("ORDER");

        /**
         *  Adăugarea butoanelor în cadrul ferestrei principale
         */
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(1, 3, 10, 10));
        buttonPanel.add(clientButton);
        buttonPanel.add(productButton);
        buttonPanel.add(orderButton);
        buttonPanel.setBackground(new Color(64, 224, 208));

        // Stilizarea și configurarea butoanelor
        clientButton.setBackground(Color.WHITE);
        productButton.setBackground(Color.WHITE);
        orderButton.setBackground(Color.WHITE);
        clientButton.setForeground(Color.RED);
        productButton.setForeground(Color.BLUE);
        orderButton.setForeground(Color.ORANGE);
        clientButton.setFont(new Font("Times New Roman", Font.BOLD, 26));
        orderButton.setFont(new Font("Times New Roman", Font.BOLD, 26));
        productButton.setFont(new Font("Times New Roman", Font.BOLD, 26));


        getContentPane().add(buttonPanel, BorderLayout.SOUTH);


        clientButton.addActionListener(e -> openClientPage());
        productButton.addActionListener(e -> openProductPage());
        orderButton.addActionListener(e -> openOrderPage());
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
                int size = 100;
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
     * Metodă pentru deschiderea paginii de client.
     */
    private void openClientPage() {
        ClientFrame clientView = new ClientFrame();
        clientView.setVisible(true);
    }

    /**
     * Metodă pentru deschiderea paginii de produs.
     */
    private void openProductPage() {
        ProductFrame productView = new ProductFrame();
        productView.setVisible(true);
    }

    /**
     * Metodă pentru deschiderea paginii de comandă.
     */
    private void openOrderPage() {
        Presentation.OrderFrame orderView = new Presentation.OrderFrame();
        orderView.setVisible(true);
    }

    /**
     * Metoda main pentru pornirea aplicației.
     */
    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {
            Start start = new Start();
            start.setVisible(true);
        });
    }
}
