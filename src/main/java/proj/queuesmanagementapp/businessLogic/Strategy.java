package proj.queuesmanagementapp.businessLogic;

import proj.queuesmanagementapp.model.Server;
import proj.queuesmanagementapp.model.Task;

import java.util.List;

public interface Strategy {
    public void addTask(List<Server> servers, Task task);
}
