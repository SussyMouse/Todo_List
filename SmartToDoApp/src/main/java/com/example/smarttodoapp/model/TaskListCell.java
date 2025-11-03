package com.example.smarttodoapp.model;

import com.example.smarttodoapp.data.TaskStore;
import com.example.smarttodoapp.model.Task;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

import java.time.format.DateTimeFormatter;
import java.util.Optional;

public class TaskListCell extends ListCell<Task> {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("MM/dd/yyyy");

    private final ObservableList<Task> tasks;

    private final HBox root = new HBox(20);
    private final Button completeButton = new Button("Mark as Completed");
    private final HBox card = new HBox(12);
    private final VBox textBox = new VBox(4);
    private final Label titleLabel = new Label();
    private final Label metaLabel = new Label();
    private final Region spacer = new Region();
    private final HBox actionBox = new HBox(6);
    private final Button notesButton = new Button("\uD83D\uDCCB");
    private final Button infoButton = new Button("\u2139");
    private final Button deleteButton = new Button("\uD83D\uDDD1");

    public TaskListCell(ObservableList<Task> tasks) {
        this.tasks = tasks;

        getStyleClass().add("task-list-cell");

        root.getStyleClass().add("task-cell");
        root.setAlignment(Pos.CENTER_LEFT);

        completeButton.getStyleClass().add("mark-complete-button");
        completeButton.setFocusTraversable(false);

        card.getStyleClass().add("task-card");
        card.setAlignment(Pos.CENTER_LEFT);
        card.setPadding(new Insets(14, 24, 14, 24));

        textBox.getChildren().addAll(titleLabel, metaLabel);
        textBox.getStyleClass().add("task-card-text");

        titleLabel.getStyleClass().add("task-title");
        metaLabel.getStyleClass().add("task-meta");

        actionBox.getStyleClass().add("task-actions");
        actionBox.setAlignment(Pos.CENTER);

        notesButton.getStyleClass().add("task-action-button");
        infoButton.getStyleClass().add("task-action-button");
        deleteButton.getStyleClass().addAll("task-action-button", "task-delete-button");

        notesButton.setFocusTraversable(false);
        infoButton.setFocusTraversable(false);
        deleteButton.setFocusTraversable(false);

        notesButton.setTooltip(new Tooltip("Show description"));
        infoButton.setTooltip(new Tooltip("Task details"));
        deleteButton.setTooltip(new Tooltip("Delete task"));

        actionBox.getChildren().addAll(notesButton, infoButton, deleteButton);

        HBox.setHgrow(spacer, Priority.ALWAYS);
        HBox.setHgrow(card, Priority.ALWAYS);
        card.getChildren().addAll(textBox, spacer, actionBox);

        root.getChildren().addAll(completeButton, card);

        setContentDisplay(ContentDisplay.GRAPHIC_ONLY);

        completeButton.setOnAction(e -> {
            Task item = getItem();
            if (item == null) {
                return;
            }
            item.setCompleted(!item.isCompleted());
            updateCompletionState(item);
            TaskStore.save(tasks);
            // force list to refresh to update style classes in sibling cells if needed
            if (getListView() != null) {
                getListView().refresh();
            }
        });

        deleteButton.setOnAction(e -> {
            Task item = getItem();
            if (item == null) {
                return;
            }
            tasks.remove(item);
            TaskStore.save(tasks);
        });

        infoButton.setOnAction(e -> {
            Task item = getItem();
            if (item == null) {
                return;
            }
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Task Details");
            alert.setHeaderText(item.getName());
            StringBuilder content = new StringBuilder();
            if (item.getDescription() != null && !item.getDescription().isBlank()) {
                content.append(item.getDescription()).append("\n\n");
            }
            content.append("Category: ")
                    .append(Optional.ofNullable(item.getCategory()).filter(s -> !s.isBlank()).orElse("None"));
            content.append("\nPriority: ")
                    .append(Optional.ofNullable(item.getPriority()).map(Object::toString).orElse("None"));
            if (item.getDueDate() != null) {
                content.append("\nDue Date: ").append(DATE_FORMATTER.format(item.getDueDate()));
            }
            alert.setContentText(content.toString());
            alert.showAndWait();
        });

        notesButton.setOnAction(e -> {
            Task item = getItem();
            if (item == null) {
                return;
            }
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Task Notes");
            alert.setHeaderText(item.getName());
            String description = item.getDescription();
            if (description == null || description.isBlank()) {
                description = "No additional notes.";
            }
            alert.setContentText(description);
            alert.showAndWait();
        });
    }

    @Override
    protected void updateItem(Task task, boolean empty) {
        super.updateItem(task, empty);
        if (empty || task == null) {
            setText(null);
            setGraphic(null);
            return;
        }

        titleLabel.setText(task.getName());

        String due = task.getDueDate() != null ? DATE_FORMATTER.format(task.getDueDate()) : "No due date";
        String category = Optional.ofNullable(task.getCategory()).filter(s -> !s.isBlank()).orElse("No category");
        metaLabel.setText(String.format("Due Date: %s, Category: %s", due, category));

        updatePriorityStyle(task);
        updateCompletionState(task);

        setGraphic(root);
    }

    private void updatePriorityStyle(Task task) {
        card.getStyleClass().setAll("task-card");
        if (task.getPriority() != null) {
            card.getStyleClass().add("priority-" + task.getPriority());
        }
        if (task.isCompleted()) {
            card.getStyleClass().add("completed");
        }
    }

    private void updateCompletionState(Task task) {
        if (task.isCompleted()) {
            completeButton.setText("Completed");
            completeButton.getStyleClass().add("completed");
            card.getStyleClass().add("completed");
        } else {
            completeButton.setText("Mark as Completed");
            completeButton.getStyleClass().remove("completed");
            card.getStyleClass().remove("completed");
        }
    }
}
