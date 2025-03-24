package BussinesLogic;

import Model.Server;
import Model.Task;

import javax.swing.*;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

public class SimulationManager implements Runnable {
    private final String fileName = "non.txt";
    private int timeLimit = 200;
    private int maxProcessingTime = 9;
    private int minProcessingTime = 3;
    private  int  numberOfQueue=5;
    private int minArrivalTime = 10;
    private int maxArrivalTime = 100;
    private int numberOfClients = 20;
    private  SimulationFrame simulationFrame;
    private SelectionPolicy selectionPolicy = SelectionPolicy.SHORTEST_QUEUE;

    private final FileWriter fileWriter;
    private final BufferedWriter buffer;
    private final Scheduler scheduler;
    private final SimulationFrame frame;

    private double avgWaitingTime = 0;
    private int currentTime = 0;
    private double avgSerTime = 0;
    private double avgServDivisor = 0;
    private int maxWaitingHour = 0;
    int waitingTime = 0;

    private final List<Task> Tasks;


    public SimulationManager(int timeLimit, int maxProcessingTime,int minProcessingTime, int minArrivalTime, int maxArrivalTime,int NumberOfQueue ,int numberOfClients,SimulationFrame simulationFrame) {
       this.timeLimit=timeLimit;
       this.maxProcessingTime=maxProcessingTime;
       this.minProcessingTime=minProcessingTime;
       this.minArrivalTime=minArrivalTime;
       this.maxArrivalTime=maxArrivalTime;
        this.numberOfClients=numberOfClients;
       this.simulationFrame=simulationFrame;

       try {
            fileWriter = new FileWriter(fileName);
            buffer = new BufferedWriter(fileWriter);
        } catch (IOException e) {
            throw new RuntimeException("Error creating file writer", e);
        }

        frame = SimulationFrame.getInstance(NumberOfQueue );
        Tasks = generateRandomTasks();
        scheduler = new Scheduler(NumberOfQueue , numberOfClients);
        scheduler.changeStrategy(selectionPolicy);
    }

    private List<Task> generateRandomTasks() {
        List<Task> tasks = new ArrayList<>();
        Random random = new Random();
        for (int i = 0; i < numberOfClients; i++) {
            int processingTime = random.nextInt(maxProcessingTime - minProcessingTime) + minProcessingTime;
            int arrivalTime = random.nextInt(maxArrivalTime - minArrivalTime) + minArrivalTime;
            Task task = new Task(arrivalTime, processingTime,i+1);
            tasks.add(task);
        }

        tasks.sort(Comparator.comparingInt(Task::getArrivalTime));
        return tasks;

    }

    @Override
    public void run() {
        while (currentTime <= timeLimit) {
            processTasks();

            System.out.println(currentTime);
            printQueues();
            printFrame();
            printFiles();
            currentTime++;

            if (currentTime == timeLimit || Tasks.isEmpty()) {
                break;
            }

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException("Thread interrupted", e);
            }

            calculateAvarageTime();
        }
        frame.updateLogLine("Time expired");
        printStatistics();
    }

    private void processTasks() {
        List<Task> removableTasks = new ArrayList<>();
        for (Task task : Tasks) {
            if (currentTime == task.getArrivalTime()) {
                scheduler.dispatchTask(task);
                removableTasks.add(task);
            }
        }
        Tasks.removeAll(removableTasks);
    }

    private void calculateAvarageTime() {
        for (Server server : scheduler.getServers()) {
            for (Task t : server.getTasks()) {
                avgSerTime += t.getServiceTime();
                avgServDivisor++;
            }
            waitingTime += server.getWaitingPeriod();
            avgWaitingTime += server.getWaitingPeriod();
            avgServDivisor++;
        }
        if (avgWaitingTime > maxWaitingHour) {
            maxWaitingHour = (int) avgWaitingTime;

        }
        avgWaitingTime = 0; // Reset waiting time for next iteration
    }

    private void printQueues() {
         int nr=1;
        for (Server server : scheduler.getServers()) {
            System.out.print("Queue " + nr++ + ": ");
            System.out.println(server.getTaskQueue());
        }
    }



    private void printFiles() {
        try {
            buffer.write("Time:" + currentTime + "\n");
            buffer.write("Waiting line: ");
            for (Task t : Tasks) {
                buffer.write(t.toString());
            }
            buffer.write("\n");

            int i = 1;
            for (Server s : scheduler.getServers()) {
                buffer.write("Queue" + i + ":" + s.getTaskQueue() + "\n");i++;
            }
            buffer.write("\n");

        } catch (IOException e) {
            throw new RuntimeException("Error writing to file", e);
        }
    }

    private void printStatistics() {
        avgSerTime = avgSerTime / avgServDivisor;
        avgWaitingTime = avgWaitingTime / avgServDivisor;
        frame.updateLogLine("Average Waiting Time: " + String.format("%.2f", avgWaitingTime));
        frame.updateLogLine("Average Service Time: " + String.format("%.2f", avgSerTime));
    }

    private int getWaitingPeriod() {
        int waitingPeriod = 0;
        for (Server server : scheduler.getServers()) {
            waitingPeriod += server.getWaitingPeriod();
        }
        return waitingPeriod;
    }

    private void printFrame() {
        frame.updateLogLine("Time: " + currentTime);
        frame.updateLogLine("Waiting line :");
        for (Task t : Tasks) {
            frame.updateLog(t.toString());
        }
        frame.updateLogLine(" ");

        int i = 1;
        for (Server s : scheduler.getServers()) {
//            System.out.println(i);
            frame.updateLogLine("Queue " + i + ": " + s.getTaskQueue());i++;
        }
        frame.updateLogLine("");
    }

    public static void main(String[] args) {

        FirstInterface firstInterface = new FirstInterface("Queues Management Application");
        firstInterface.setVisible(true);
        firstInterface.pack();



        firstInterface.addActionListener(e -> {
            Scheduler scheduler = new Scheduler( Integer.parseInt(firstInterface.getInputqueue()), Integer.parseInt(firstInterface.getInputMaxServiceTime()));
            SimulationFrame sim=new SimulationFrame(Integer.parseInt(firstInterface.getInputMaxServiceTime()));
            SimulationManager simulationManager = new SimulationManager(Integer.parseInt(firstInterface.getInputSimulationInterval()),Integer.parseInt(firstInterface.getInputMaxServiceTime()),Integer.parseInt(firstInterface.getInputMinServiceTime()),Integer.parseInt(firstInterface.getInputMinArrTime()),Integer.parseInt(firstInterface.getiNnputMaxArrTime()),Integer.parseInt(firstInterface.getInputqueue()), Integer.parseInt(firstInterface.getInputnrClients()),sim);

            if (e.getSource() == firstInterface.getComboBox()) {
                JComboBox<String> comboBox = (JComboBox<String>) e.getSource();
                String selectedStrategy = (String) comboBox.getSelectedItem();
                if (selectedStrategy != null) {
                    if (selectedStrategy.equals("Shortest queue")) {
                        simulationManager.setSelectionPolicy(SelectionPolicy.SHORTEST_QUEUE);
                    } else if (selectedStrategy.equals("Shortest time")) {
                        simulationManager.setSelectionPolicy(SelectionPolicy.SHORTEST_TIME);
                    }
                }
            } else if (e.getSource() == firstInterface.getM_Simulation()) {
                int maxTasksPerServer = scheduler.getServers().getFirst().getMaxTasksPerServer();
                SimulationFrame simulationFrame = SimulationFrame.getInstance(maxTasksPerServer);
                simulationFrame.setVisible(true);
                Thread simulationThread = new Thread(simulationManager);
                simulationThread.start();
            }
        });

    }

    public void setSelectionPolicy(SelectionPolicy selectionPolicy) {
    }

    public void setScheduler(Scheduler scheduler) {
    }


}
