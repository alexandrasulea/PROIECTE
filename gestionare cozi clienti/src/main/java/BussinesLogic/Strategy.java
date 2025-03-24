package BussinesLogic;

import Model.Server;
import Model.Task;

import java.util.List;

// Strategy interface
public interface Strategy {
    void addTask(Task task, List<Server> servers);
}

// Concrete implementation for shortest queue policy
class ConcreteStrategyQueue implements Strategy {
    @Override
    public void addTask(Task task, List<Server> servers) {
        // Find the server with the shortest queue
        Server shortestQueueServer = servers.get(0);
        for (Server server : servers) {
            if (server.getWaitingPeriod() < shortestQueueServer.getWaitingPeriod()) {
                shortestQueueServer = server;
            }
        }
        shortestQueueServer.addTask(task);
    }
}

// Concrete implementation for shortest time policy
class ConcreteStrategyTime implements Strategy {
    @Override
    public void addTask(Task task, List<Server> servers) {
        // Find the server with the shortest total processing time
        Server shortestTimeServer = servers.get(0);
        for (Server server : servers) {
            if (server.getWaitingPeriod() < shortestTimeServer.getWaitingPeriod()) {
                shortestTimeServer = server;
            }
        }
        shortestTimeServer.addTask(task);
    }
}
