package com.btl.taskmanagement.Models;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.time.LocalDate;
import java.util.LinkedList;

public class Day {
	private LocalDate date;
	private LinkedList<Task> taskLinkedList = new LinkedList<>();
	private ObservableList<Task> taskObservableList = FXCollections.observableArrayList();
	
	public Day(LocalDate date) {
		this.date = date;
	}
	
	public void addTask(Task task) {
		updateObservableList();
	}
	
	public void removeTask(Task task) {
		taskLinkedList.remove(task);
		updateObservableList();
	}
	
	private void updateObservableList() {
		taskObservableList.setAll(taskLinkedList);
	}
	
	public ObservableList<Task> getTaskObservableList() {
		return taskObservableList;
	}
	
	public LocalDate getDate() {
		return date;
	}
	
	public void setDate(LocalDate date) {
		this.date = date;
	}
	
}
