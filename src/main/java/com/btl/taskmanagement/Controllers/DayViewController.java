package com.btl.taskmanagement.Controllers;

import com.btl.taskmanagement.Models.Day;
import com.btl.taskmanagement.Models.Task;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.util.Duration;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ResourceBundle;

public class DayViewController implements Initializable {
	protected Day day;
	@FXML
	private ListView<Task> listView;
	@FXML
	private Button addButton, deleteButton, startButton;
	
	@Override
	public void initialize(URL url, ResourceBundle resourceBundle) {
		// tạo tạm day để test
		day = new Day(LocalDate.now());
		day.addTask(new Task("Cong viec A", LocalTime.of(8, 30), Duration.minutes(25), Duration.minutes(5), "X", Task.Priority.LOW, Duration.minutes(5)));
		day.addTask(new Task("Cong viec B", LocalTime.of(9, 0), Duration.minutes(25), Duration.minutes(5), "Y", Task.Priority.HIGH, Duration.minutes(5)));
		day.addTask(new Task("Cong viec C", LocalTime.of(9, 30), Duration.minutes(25), Duration.minutes(5), "Z", Task.Priority.MEDIUM, Duration.minutes(5)));
		day.addTask(new Task("Cong viec D", LocalTime.of(13, 30), Duration.minutes(25), Duration.minutes(5), "X", Task.Priority.LOW, Duration.minutes(5)));
		day.addTask(new Task("Cong viec E", LocalTime.of(14, 0), Duration.minutes(25), Duration.minutes(5), "Y", Task.Priority.HIGH, Duration.minutes(5)));
		day.addTask(new Task("Cong viec F", LocalTime.of(17, 30), Duration.minutes(25), Duration.minutes(5), "Z", Task.Priority.MEDIUM, Duration.minutes(5)));
		listView.setCellFactory(_ -> new CustomTaskCell());
		listView.setItems(day.getTaskObservableList());
		
	}
	
	@FXML
	public void handleAddTask() {
	
	}
	
	@FXML
	public void handleDeleteTask() {
		day.removeTask(listView.getSelectionModel().getSelectedItem());
		listView.getSelectionModel().clearSelection();
	}
	
	
	
	
	
}
