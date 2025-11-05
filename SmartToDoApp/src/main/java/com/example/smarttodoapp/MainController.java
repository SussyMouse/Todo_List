package com.example.smarttodoapp;

import com.example.smarttodoapp.data.TaskStore;
import com.example.smarttodoapp.model.Task;
import com.example.smarttodoapp.model.TaskListCell;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class MainController {
    private static final String ALL_CATEGORIES = "All Categories";
    private static final String ALL_PRIORITIES = "All Priorities";
    private static final String[] DEFAULT_CATEGORIES = {
            "Study", "Work", "Shopping", "Fitness", "Personal", "Healthy", "Home"
    };

    @FXML
    private ListView<Task> taskListView;
    @FXML
    private ComboBox<String> categoryFilterComboBox;
    @FXML
    private ComboBox<String> priorityFilterComboBox;
    @FXML
    private DatePicker dueDateFilterDatePicker;
    @FXML
    private TextField searchTextField;

    private ObservableList<Task> tasks;
    private ObservableList<Task> filteredTasks;

    @FXML
    public void initialize() {
        tasks = TaskStore.load();

        if (tasks == null) {
            tasks = FXCollections.observableArrayList();
        }
        filteredTasks = FXCollections.observableArrayList();
        taskListView.setItems(filteredTasks);
        taskListView.setCellFactory(list -> new TaskListCell(tasks, this::handleTasksChanged));

        configureFilterControls();
        applyFilters();

        tasks.addListener((ListChangeListener<Task>) change -> {
            refreshCategoryOptions();
            applyFilters();
        });
    }

    @FXML
    public void switchToTaskFormScene(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("TaskFormDialog.fxml"));
        Parent dialogRoot = loader.load();
        TaskFormController controller = loader.getController();
        controller.setTasks(tasks);
        controller.setOnTasksChanged(this::handleTasksChanged);

        Stage owner = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Stage dialog = new Stage();
        dialog.initOwner(owner);
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setTitle("Add Task");
        dialog.setScene(new Scene(dialogRoot));
        dialog.showAndWait();

        taskListView.refresh();
    }

//    public void switchToTaskInfoScene(ActionEvent event) throws IOException {
//        root = FXMLLoader.load(getClass().getResource("TaskInfoDialog.fxml"));
//        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
//        scene = new Scene(root);
//        stage.setScene(scene);
//        stage.show();
//    }

    private void configureFilterControls() {
        if (priorityFilterComboBox != null) {
            priorityFilterComboBox.setItems(FXCollections.observableArrayList(
                    ALL_PRIORITIES, "1", "2", "3", "4"
            ));
            priorityFilterComboBox.setValue(ALL_PRIORITIES);
            priorityFilterComboBox.valueProperty().addListener((obs, oldValue, newValue) -> applyFilters());
        }

        if (categoryFilterComboBox != null) {
            refreshCategoryOptions();
            categoryFilterComboBox.valueProperty().addListener((obs, oldValue, newValue) -> applyFilters());
        }

        if (dueDateFilterDatePicker != null) {
            dueDateFilterDatePicker.valueProperty().addListener((obs, oldValue, newValue) -> applyFilters());
        }

        if (searchTextField != null) {
            searchTextField.textProperty().addListener((obs, oldValue, newValue) -> applyFilters());
        }
    }

    private void refreshCategoryOptions() {
        if (categoryFilterComboBox == null) {
            return;
        }

        String previousSelection = categoryFilterComboBox.getValue();
        List<String> options = new ArrayList<>();
        options.add(ALL_CATEGORIES);

        for (String defaultCategory : DEFAULT_CATEGORIES) {
            addOptionIfMissing(options, defaultCategory);
        }

        if (tasks != null) {
            for (Task task : tasks) {
                if (task == null) {
                    continue;
                }
                String category = task.getCategory();
                if (category != null && !category.isBlank()) {
                    addOptionIfMissing(options, category);
                }
            }
        }

        categoryFilterComboBox.setItems(FXCollections.observableArrayList(options));

        String matchingSelection = null;
        if (previousSelection != null) {
            for (String option : options) {
                if (option.equalsIgnoreCase(previousSelection)) {
                    matchingSelection = option;
                    break;
                }
            }
        }

        if (matchingSelection != null) {
            categoryFilterComboBox.setValue(matchingSelection);
        } else {
            categoryFilterComboBox.setValue(ALL_CATEGORIES);
        }
    }

    private void applyFilters() {
        String keyword = searchTextField != null ? searchTextField.getText() : null;
        String trimmedKeyword = keyword == null ? "" : keyword.trim().toLowerCase();
        boolean hasKeyword = !trimmedKeyword.isBlank();

        String selectedCategory = categoryFilterComboBox != null ? categoryFilterComboBox.getValue() : null;
        String selectedPriority = priorityFilterComboBox != null ? priorityFilterComboBox.getValue() : null;
        LocalDate selectedDate = dueDateFilterDatePicker != null ? dueDateFilterDatePicker.getValue() : null;

        boolean checkCategory = selectedCategory != null && !selectedCategory.equals(ALL_CATEGORIES);
        boolean checkPriority = selectedPriority != null && !selectedPriority.equals(ALL_PRIORITIES);
        boolean checkDate = selectedDate != null;

        int requiredPriority = 0;
        if (checkPriority) {
            try {
                requiredPriority = Integer.parseInt(selectedPriority);
            } catch (NumberFormatException ex) {
                checkPriority = false;
            }
        }

        filteredTasks.clear();
        if (tasks == null) {
            return;
        }

        for (Task task : tasks) {
            if (task == null) {
                continue;
            }

            if (checkCategory) {
                String taskCategory = task.getCategory();
                if (taskCategory == null || !taskCategory.equalsIgnoreCase(selectedCategory)) {
                    continue;
                }
            }

            if (checkPriority) {
                Integer taskPriority = task.getPriority();
                if (taskPriority == null || taskPriority != requiredPriority) {
                    continue;
                }
            }

            if (checkDate) {
                if (task.getDueDate() == null || !task.getDueDate().isEqual(selectedDate)) {
                    continue;
                }
            }

            if (hasKeyword) {
                if (!containsIgnoreCase(task.getName(), trimmedKeyword)
                        && !containsIgnoreCase(task.getDescription(), trimmedKeyword)
                        && !containsIgnoreCase(task.getCategory(), trimmedKeyword)) {
                    continue;
                }
            }

            filteredTasks.add(task);
        }
    }

    private void addOptionIfMissing(List<String> list, String value) {
        if (value == null) {
            return;
        }
        for (String existing : list) {
            if (existing.equalsIgnoreCase(value)) {
                return;
            }
        }
        list.add(value);
    }

    private boolean containsIgnoreCase(String value, String keyword) {
        return value != null && value.toLowerCase().contains(keyword);
    }

    private void handleTasksChanged() {
        refreshCategoryOptions();
        applyFilters();
        if (taskListView != null) {
            taskListView.refresh();
        }
    }
}
