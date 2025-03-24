package Model;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class Server implements Runnable {
    private BlockingQueue<Task> tasks;
    private AtomicInteger waitingPeriod;
    private int maxTasksPerServer;
    private int TotalProcessingTime;

    public Server(int maxTasksPerServer) {
        this.maxTasksPerServer = maxTasksPerServer;
        tasks = new LinkedBlockingQueue<>(maxTasksPerServer);
        waitingPeriod = new AtomicInteger(0);
    }

    public void addTask(Task newTask) {
        tasks.add(newTask);
        waitingPeriod.getAndAdd(newTask.getServiceTime());
    }

    public void run() {
        while (true) {
            if(!tasks.isEmpty()) {
                Task task = tasks.peek();//obtine elementul din varful cozii
                while(task.getServiceTime() > 0) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        break;
                    }
                    waitingPeriod.getAndDecrement();
                    task.setServiceTime(task.getServiceTime() - 1);
                }
                tasks.poll();//eliminarea din coada
            }
        }
    }

    public int getTaskQueueSize() {
        return tasks.size();
    }

    public Task[] getTasks() {
        return tasks.toArray(new Task[0]);
    }

    public int getTotalWaitingTime() {
        return waitingPeriod.get();
    }

    public String getTaskQueue() {
        StringBuilder queueContent = new StringBuilder();
        for (Task task : tasks) {
            queueContent.append(task.toString()).append("\n");
        }
        return queueContent.toString();
    }

    public int getWaitingPeriod() {
        return waitingPeriod.get();
    }

    public Object getTotalProcessingTime() {
        return TotalProcessingTime;
    }
    public int getMaxTasksPerServer(){
        return maxTasksPerServer;
    }
}
