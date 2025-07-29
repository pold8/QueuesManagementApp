module proj.queuesmanagementapp {
    requires javafx.controls;
    requires javafx.fxml;


    opens proj.queuesmanagementapp to javafx.fxml;
    exports proj.queuesmanagementapp;
    exports proj.queuesmanagementapp.GUI;
    opens proj.queuesmanagementapp.GUI to javafx.fxml;
}