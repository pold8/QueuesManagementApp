package proj.queuesmanagementapp.businessLogic;

import proj.queuesmanagementapp.model.Server;
import proj.queuesmanagementapp.model.Task;

import java.util.ArrayList;
import java.util.List;

public class Scheduler {
    private List<Server> servers;
    private int maxNoServers;
    private Strategy strategy;

    public Scheduler(int maxNoServers) {
        this.maxNoServers = maxNoServers;

        servers = new ArrayList<>();
        List<Thread> threads = new ArrayList<>();

        for (int i = 0; i < maxNoServers; i++) {
            Server server = new Server(1000);
            servers.add(server);

            Thread thread = new Thread(server);
            thread.start();
            threads.add(thread);
        }
    }

    public void changeStrategy(SelectionPolicy policy) {

        if(policy == SelectionPolicy.SHORTEST_QUEUE){
            strategy = new ShortestQueueStrategy();
        }
        if(policy == SelectionPolicy.SHORTEST_TIME){
            strategy = new TimeStrategy();
        }
    }

    public void dispatchTask(Task task){
        strategy.addTask(servers, task);
    }

    public List<Server> getServers() {
        return servers;
    }

    public void stopThreads() throws InterruptedException {
        for (Server server : servers) {
            server.setRunning(false);
        }
    }

}