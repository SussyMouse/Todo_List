package com.example.smarttodoapp;

import com.example.smarttodoapp.data.TaskStore;
import com.example.smarttodoapp.model.Task;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class MainController {
    @FXML
    private Label welcomeText;

    @FXML
    private javafx.scene.control.TableView<com.example.smarttodoapp.model.Task> taskTable;

    private ObservableList<Task> tasks;

    private Stage stage;
    private Scene scene;
    private Parent root;

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to Smart To Do Application!");
    }

    @FXML
    public void initialize() {
        tasks = TaskStore.load();
    }

    @FXML
    public void switchToTaskFormScene(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("TaskFormDialog.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void switchToTaskInfoScene(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("TaskInfoDialog.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

}
