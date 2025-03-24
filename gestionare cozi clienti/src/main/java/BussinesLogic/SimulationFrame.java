package BussinesLogic;

import javax.swing.*;
import java.awt.*;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class SimulationFrame extends JFrame {
    private static SimulationFrame instance;//asigura că există o singură instanță a ferestrei de simulare

    private JTextArea log= new JTextArea("log",30,1);
    private JPanel movingFrame= new JPanel(new GridLayout(1,1));
    private JPanel mainPanel= new JPanel();
    private int elapsedTime = 0;
    private JScrollPane logPanel= new JScrollPane(log);

    public SimulationFrame(int maxTasksPerServer){
        this.setTitle("Second interface");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(640,780);
        log.setEditable(false);
        log.setText("");
        log.setBackground(Color.CYAN);


        mainPanel.setLayout(new BorderLayout());
        mainPanel.add(logPanel, BorderLayout.CENTER);
        this.setContentPane(mainPanel);

        Timer timer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                elapsedTime+=1;
            }
        });
        timer.start();
    }

    public static synchronized SimulationFrame getInstance(int numberOfServers) {
        if (instance == null) {
            instance = new SimulationFrame(numberOfServers);
        }
        return instance;
    }

    public void updateLogLine (String string){
        log.append(string + "\n");
        // print id

    }

    public void updateLog  (String string) {
        log.append(string + ", ");
        // print id

    }

}
