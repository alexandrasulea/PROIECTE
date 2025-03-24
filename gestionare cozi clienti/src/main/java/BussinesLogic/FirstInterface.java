package BussinesLogic;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import javax.swing.border.LineBorder;

public class FirstInterface extends JFrame {

    private JPanel contentPane;
    private JTextField title;
    private JTextField NumberOfClients;
    private JTextField NumberOfQueue;
    private JTextField MinArrTime;
    private JTextField MaxArrTime;
    private JTextField MinServiceTime;
    private JTextField SimulationInterval;
    private JButton m_Simulation;
    private JTextField MaxServiceTime;
    private JComboBox<String> comboBox;

    public FirstInterface(String name) {
        super(name);
        this.prepareGui();
    }

    public void prepareGui() {
        this.setSize(600, 600);
        this.setVisible(true);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.contentPane = new JPanel(new BorderLayout());

        // Prepare Title Panel
        this.prepareTitlePanel();
        this.contentPane.add(title, BorderLayout.NORTH);

        // Prepare Numbers Panel
        this.prepareNumbersPanel();
        JPanel numbersAndLabelsPanel = new JPanel(new GridLayout(8, 2));

        // Add Labels and TextFields to Numbers Panel
        addLabelAndTextField(numbersAndLabelsPanel, "Number Of Clients:", NumberOfClients);
        addLabelAndTextField(numbersAndLabelsPanel, "Number Of Queue:", NumberOfQueue);
        addLabelAndTextField(numbersAndLabelsPanel, "Simulation Interval:", SimulationInterval);
        addLabelAndTextField(numbersAndLabelsPanel, "Minimum Arrival Time:", MinArrTime);
        addLabelAndTextField(numbersAndLabelsPanel, "Maximum Arrival Time:", MaxArrTime);
        addLabelAndTextField(numbersAndLabelsPanel, "Minimum Service Time:", MinServiceTime);
        addLabelAndTextField(numbersAndLabelsPanel, "Maximum Service Time:", MaxServiceTime);

        // Add JComboBox
        String[] options = {"Shortest queue", "Shortest time"};
        comboBox = new JComboBox<>(options);
        JPanel comboBoxPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        comboBoxPanel.add(new JLabel("Choose an option:", JLabel.CENTER));
        comboBoxPanel.add(comboBox);
        numbersAndLabelsPanel.add(comboBoxPanel);

        // Add Buttons
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonsPanel.add(m_Simulation);

        // Add Numbers Panel and Buttons Panel to Content Pane
        this.contentPane.add(numbersAndLabelsPanel, BorderLayout.CENTER);
        this.contentPane.add(buttonsPanel, BorderLayout.SOUTH);

        // Set Content Pane
        this.setContentPane(this.contentPane);
    }

    private void prepareTitlePanel() {
        this.title = new JTextField("QUEUES MANAGEMENT APPLICATION USING\n" +
                "THREADS AND SYNCHRONIZATION MECHANISMS");
        this.title.setEditable(false);
    }

    private void prepareNumbersPanel() {
        this.NumberOfClients = new JTextField();
        this.NumberOfQueue = new JTextField();
        this.SimulationInterval = new JTextField();
        this.MinArrTime = new JTextField();
        this.MaxArrTime = new JTextField();
        this.MinServiceTime = new JTextField();
        this.MaxServiceTime = new JTextField();
        this.m_Simulation = new JButton("Simulation");
    }

    private void addLabelAndTextField(JPanel panel, String labelText, JTextField textField) {
        JLabel label = new JLabel(labelText, JLabel.CENTER);
        label.setBackground(Color.LIGHT_GRAY);
        label.setOpaque(true);
        label.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 20));
        panel.add(label);
        panel.add(textField);
    }

    public JTextField getNumberOfClientsTextField() {
        return NumberOfClients;
    }

    public JTextField getNumberOfQueueTextField() {
        return NumberOfQueue;
    }

    public JTextField getSimulationIntervalTextField() {
        return SimulationInterval;
    }

    public JTextField getMinArrTime() {
        return MinArrTime;
    }

    public JTextField getMaxArrTime() {
        return MaxArrTime;
    }

    public JTextField getMinAndMaxServiceTime() {
        return MinServiceTime;
    }

    public JTextField getMaxServiceTime() {
        return MaxServiceTime;
    }

    public void addActionListener(ActionListener listener) {
        m_Simulation.addActionListener(listener);
        // comboBox.addActionListener(listener);
    }

    public JComboBox<String> getComboBox() {
        return comboBox;
    }

    public JButton getM_Simulation() {
        return m_Simulation;
    }

    public String getInputqueue(){ return  NumberOfQueue.getText();}
    public String getInputnrClients(){ return NumberOfClients.getText();}
    public String getInputMinArrTime(){ return MinArrTime.getText();}
    public String getiNnputMaxArrTime(){ return MaxArrTime.getText();}
    public String getInputMinServiceTime() { return MinServiceTime.getText();}
    public String getInputMaxServiceTime(){ return MaxServiceTime.getText();}
    public String getInputSimulationInterval(){ return SimulationInterval.getText();}


}

