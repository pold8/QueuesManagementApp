package proj.queuesmanagementapp.businessLogic;

import proj.queuesmanagementapp.model.Server;
import proj.queuesmanagementapp.model.Task;

import java.util.List;

public class TimeStrategy implements Strategy {

    @Override
    public void addTask(List<Server> servers, Task task) {
        Server server = null;
        int minTime = Integer.MAX_VALUE;

        for (Server s : servers) {
            if(s.getWaitingPeriod().intValue() < minTime){
                server = s;
                minTime = s.getWaitingPeriod().intValue();
            }
        }

        server.addTask(task);
    }

}
