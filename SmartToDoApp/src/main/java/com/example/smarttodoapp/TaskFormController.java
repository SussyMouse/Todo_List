package com.example.smarttodoapp;

import com.example.smarttodoapp.data.TaskStore;
import com.example.smarttodoapp.model.Task;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import java.io.IOException;
import java.time.LocalDate;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class TaskFormController {

    @FXML
    private TextField taskNameTextField;
    @FXML
    private TextArea taskDescriptionTextField;
    @FXML
    private ComboBox<String> taskCategoryComboBox;
    @FXML
    private ComboBox<Integer> taskPriorityComboBox;
    @FXML
    private DatePicker taskDueDateDatePicker;
    @FXML
    private Button taskSaveButton;
    @FXML
    private Button taskCancelButton;

    private ObservableList<Task> tasks;

    String taskCategory;
    Integer taskPriority;

    private Stage stage;
    private Scene scene;
    private Parent root;

    public void setTasks(ObservableList<Task> tasks) {
        this.tasks = tasks;
    }

    @FXML
    public void initialize() {
        tasks = TaskStore.load();
        taskPriorityComboBox.getItems().setAll(1,2,3,4);
        taskCategoryComboBox.getItems().setAll("Study", "Work", "Shopping", "Fitness", "Personal", "Healthy", "Home");
    }

    @FXML
    public void categoryComboSelection() {
        taskCategory =  taskCategoryComboBox.getValue();
    }

    @FXML
    public void priorityComboSelection() {
        taskPriority = taskPriorityComboBox.getValue();
    }

    @FXML
    public void save(ActionEvent event) {
        String taskName = taskNameTextField.getText();
        String taskDescription = taskDescriptionTextField.getText();
        String taskCategory = taskCategoryComboBox.getValue();
        Integer taskPriority = taskPriorityComboBox.getValue();
        LocalDate taskDueDate = taskDueDateDatePicker.getValue();

        if (taskName == null || taskName.isBlank()) {
            new Alert(Alert.AlertType.WARNING, "Please enter a task name.").showAndWait();
            return;
        }

        Task t = new Task(taskName, taskDescription, taskCategory, taskPriority, taskDueDate, false);

        tasks.add(t);

        TaskStore.save(tasks);

    }

    @FXML
    private void cancel(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

    public void switchToMainWindowScene(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("MainWindow.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void saveAndSwitchToMainWindowScene(ActionEvent event) throws IOException {
        save(event);
        switchToMainWindowScene(event);
    }

}
