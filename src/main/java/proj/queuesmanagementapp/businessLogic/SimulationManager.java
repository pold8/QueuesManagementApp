package proj.queuesmanagementapp.businessLogic;

import proj.queuesmanagementapp.GUI.SimulationController;
import proj.queuesmanagementapp.model.Server;
import proj.queuesmanagementapp.model.Task;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

public class SimulationManager implements Runnable {

    private SimulationController controller;  // Reference to the controller to access the TextArea
    public int timeLimit;
    public int maxProcessingTime;
    public int minProcessingTime;
    public int numberOfServers;
    public int numberOfClients;
    public int minArrivalTime;
    public int maxArrivalTime;

    private Scheduler scheduler;
    private List<Task> generatedTask;

    public SimulationManager(int timeLimit, int maxProcessingTime, int minProcessingTime, int numberOfServers, int numberOfClients, int minArrivalTime, int maxArrivalTime, SelectionPolicy selectionPolicy, SimulationController controller) {
        this.timeLimit = timeLimit;
        this.maxProcessingTime = maxProcessingTime;
        this.minProcessingTime = minProcessingTime;
        this.numberOfServers = numberOfServers;
        this.numberOfClients = numberOfClients;
        this.minArrivalTime = minArrivalTime;
        this.maxArrivalTime = maxArrivalTime;
        this.controller = controller;
        scheduler = new Scheduler(numberOfServers);
        scheduler.changeStrategy(selectionPolicy);
        generateNRandomTasks();
    }

    private void generateNRandomTasks() {
        generatedTask = new ArrayList<>();
        Random random = new Random();
        for (int i = 1; i <= numberOfClients; i++) {
            int processingTime = random.nextInt(minProcessingTime, maxProcessingTime);
            int arrivalTime = random.nextInt(minArrivalTime, maxArrivalTime);
            generatedTask.add(new Task(i, arrivalTime, processingTime));
            generatedTask.sort(Comparator.comparing(Task::getArrivalTime));
        }
    }

    private float getAverageServiceTime() {
        int sum = 0;
        for (Task t : generatedTask) {
            sum += t.getServiceTime();
        }
        return (float) sum / numberOfClients;
    }

    @Override
    public void run() {
        int currentTime = 0;
        int peakHour = 0;
        int maxTasksInQueues = 0;
        int totalWaitingTime = 0;
        float avgServiceTime = getAverageServiceTime();

        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new FileWriter("output.txt"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        while (currentTime <= timeLimit) {
            StringBuilder output = new StringBuilder();
            output.append("Time ").append(currentTime).append("\n");

            output.append("Waiting clients:");
            if (!generatedTask.isEmpty()) {
                output.append(" ");
                for (int i = 0; i < generatedTask.size(); i++) {
                    Task task = generatedTask.get(i);
                    if (task.getArrivalTime() != currentTime) {
                        output.append("(").append(task.getID()).append(",")
                                .append(task.getArrivalTime()).append(",")
                                .append(task.getServiceTime()).append(")");
                        if (i < generatedTask.size() - 1) {
                            output.append("; ");
                        }
                    }
                }
            }
            output.append("\n");

            List<Task> toRemove = new ArrayList<>();
            for (Task task : generatedTask) {
                if (task.getArrivalTime() == currentTime) {
                    scheduler.dispatchTask(task);
                    toRemove.add(task);
                    for (Server s : scheduler.getServers()) {
                        if (s.getTasks().contains(task)) {
                            for(Task t : s.getTasks()) {
                                totalWaitingTime += t.getServiceTime();
                            }
                        }
                    }
                }
            }
            generatedTask.removeAll(toRemove);

            int totalTasksInQueues = 0;
            int serverIndex = 1;

            for (Server server : scheduler.getServers()) {
                StringBuilder sb = new StringBuilder("Queue " + serverIndex + ": ");
                if (!server.getTasks().isEmpty()) {
                    List<Task> taskList = new ArrayList<>(server.getTasks());
                    Task firstTask = taskList.get(0);

                    if (firstTask.getServiceTime() == 0) {
                        server.getTasks().remove(firstTask);
                    }

                    for (Task task : server.getTasks()) {
                        sb.append("(")
                                .append(task.getID()).append(",")
                                .append(task.getArrivalTime()).append(",")
                                .append(task.getServiceTime()).append("); ");
                        totalTasksInQueues++;
                    }

                    if (firstTask.getServiceTime() > 0) {
                        firstTask.setServiceTime(firstTask.getServiceTime() - 1);
                    }

                    if (sb.toString().endsWith("; ")) {
                        sb.setLength(sb.length() - 2);
                    }
                } else {
                    sb.append("closed");
                }

                output.append(sb.toString().trim()).append("\n");
                serverIndex++;
            }

            if (totalTasksInQueues > maxTasksInQueues) {
                maxTasksInQueues = totalTasksInQueues;
                peakHour = currentTime;
            }

            int v = 0;
            if (generatedTask.isEmpty()) {
                for (Server s : scheduler.getServers()) {
                    if (s.getTasks().isEmpty()) {
                        v++;
                    }
                }
                if (v == numberOfServers) {
                    currentTime = 61;
                }
            }

            controller.appendToTextArea(output.toString());
            if (writer != null) {
                try {
                    writer.write(output.toString());
                    writer.newLine();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            currentTime++;
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        try {
            scheduler.stopThreads();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        float avgWaitingTime = (float) totalWaitingTime / numberOfClients;

        StringBuilder finalOutput = new StringBuilder();
        finalOutput.append("Simulation ended.\n");
        finalOutput.append(String.format("Average waiting time: %.2f\n", avgWaitingTime));
        finalOutput.append(String.format("Average service time: %.2f\n", avgServiceTime));
        finalOutput.append("Peak hour: ").append(peakHour).append("\n");

        controller.appendToTextArea(finalOutput.toString());
        if (writer != null) {
            try {
                writer.write(finalOutput.toString());
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        //task1
        //SimulationManager gen = new SimulationManager(60,4,2,2,4,2,30,SelectionPolicy.SHORTEST_TIME);
        // task2
        // SimulationManager gen = new SimulationManager(60,7,1,5,50,2,40,SelectionPolicy.SHORTEST_TIME);
        // task3
        // SimulationManager gen = new SimulationManager(200,9,3,20,1000,10,100, SelectionPolicy.SHORTEST_TIME);
        //Thread t = new Thread(gen);
        //t.start();
    }
}