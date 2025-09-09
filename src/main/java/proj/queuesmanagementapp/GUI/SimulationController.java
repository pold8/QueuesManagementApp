package proj.queuesmanagementapp.GUI;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import proj.queuesmanagementapp.businessLogic.SelectionPolicy;
import proj.queuesmanagementapp.businessLogic.SimulationManager;

public class SimulationController {
    @FXML
    public TextField clients_textField;
    @FXML
    public TextField queues_textField;
    @FXML
    public TextField time_textField;
    @FXML
    public TextField minArrival_textField;
    @FXML
    public TextField maxArrival_textField;
    @FXML
    public TextField minService_textField;
    @FXML
    public TextField maxService_textField;

    @FXML
    public Label strategy_label;

    @FXML
    public Button changeStrategy_button;
    @FXML
    public Button start_button;

    @FXML
    public TextArea output_textArea;

    SelectionPolicy selectionPolicy = SelectionPolicy.SHORTEST_TIME;

    public void changeStrategyAction(ActionEvent event) {
        if(selectionPolicy == SelectionPolicy.SHORTEST_TIME) {
            selectionPolicy = SelectionPolicy.SHORTEST_QUEUE;
            strategy_label.setText("Shortest Queue");
        }
        else {
            selectionPolicy = SelectionPolicy.SHORTEST_TIME;
            strategy_label.setText("Shortest Time");
        }
    }

    public void startAction(ActionEvent event) {
        output_textArea.setText("");
        int nrServers = Integer.parseInt(clients_textField.getText());
        int nrClients = Integer.parseInt(queues_textField.getText());
        int simulationTime = Integer.parseInt(time_textField.getText());
        int minArrivalTime = Integer.parseInt(minArrival_textField.getText());
        int maxArrivalTime = Integer.parseInt(maxArrival_textField.getText());
        int minServiceTime = Integer.parseInt(minService_textField.getText());
        int maxServiceTime = Integer.parseInt(maxService_textField.getText());

        SimulationManager simulationManager =  new SimulationManager(simulationTime, maxServiceTime, minServiceTime, nrClients, nrServers, minArrivalTime, maxArrivalTime, selectionPolicy, this);
        Thread t = new Thread(simulationManager);
        t.start();
    }

    public void initialize(){
        if(selectionPolicy == SelectionPolicy.SHORTEST_TIME) {
            strategy_label.setText("Shortest Time");
        }
        else {
            strategy_label.setText("Shortest Queue");
        }
    }

    // Method to append text to the output TextArea
    public void appendToTextArea(String text) {
        output_textArea.appendText(text + "\n");
    }
}