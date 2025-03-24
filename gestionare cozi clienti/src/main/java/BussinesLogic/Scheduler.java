package BussinesLogic;

import Model.Server;
import Model.Task;

import java.util.ArrayList;
import java.util.List;

public class Scheduler {
    private List<Server> servers;
    private int maxNrOfServers;
    private int maxTasksPerServer;
    private Strategy strategy;

    public Scheduler(int maxNrOfServers, int maxTasksPerServer) {
        this.maxNrOfServers = maxNrOfServers;
        this.maxTasksPerServer = maxTasksPerServer;
        servers = new ArrayList<>();
        for (int i = 0; i < maxNrOfServers; i++) {
            Server server = new Server(maxTasksPerServer);
            servers.add(server);
            Thread thread = new Thread(server);
            thread.start();
        }
    }

    public void changeStrategy(SelectionPolicy policy) {
        // apply strategy pattern to instantiate the strategy with the concrete
        // strategy corresponding to policy

        if (policy == SelectionPolicy.SHORTEST_QUEUE) {
            strategy = new ConcreteStrategyQueue();
        }
        else{
            strategy = new ConcreteStrategyTime();
        }
    }



    public void dispatchTask(Task task) {
        // call the strategy addTask method
        strategy.addTask(task, servers);
    }



    public List<Server> getServers() {
        return servers;
    }
}
