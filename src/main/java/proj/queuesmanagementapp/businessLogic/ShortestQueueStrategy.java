package proj.queuesmanagementapp.businessLogic;

import proj.queuesmanagementapp.model.Server;
import proj.queuesmanagementapp.model.Task;

import java.util.List;

public class ShortestQueueStrategy implements Strategy{

    @Override
    public void addTask(List<Server> servers, Task task){
        Server server = null;
        int minQueue = Integer.MAX_VALUE;

        for (Server s : servers) {
            if(s.getTasks().size() < minQueue) {
                server = s;
                minQueue = s.getTasks().size();
            }
        }

        server.addTask(task);
    }
}
