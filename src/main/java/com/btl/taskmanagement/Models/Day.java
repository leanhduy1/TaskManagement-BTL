package com.btl.taskmanagement.Models;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Comparator;

public class Day {
	private final LocalDate date;
	private final ObservableList<Task> taskObservableList;
	
	public Day(LocalDate date) {
		this.date = date;
		this.taskObservableList = FXCollections.observableArrayList();
	}
	
	public LocalDate getDate() {
		return date;
	}
	
	public boolean addTask(Task task) {
		if (!isTimeConflict(task)) {
			taskObservableList.add(task);
			sortTasksByTime();
			return true;
		}
		return false;
	}
	
	public void removeTask(Task task) {
		taskObservableList.remove(task);
	}
	
	public ObservableList<Task> getTaskObservableList() {
		return taskObservableList;
	}
	
	private boolean isTimeConflict(Task newTask) {
		LocalTime newStart = newTask.getStartTime();
		LocalTime newEnd = newStart.plusSeconds((long) newTask.getMandatoryTime().toSeconds());
		
		return taskObservableList.stream().anyMatch(existingTask -> {
			LocalTime existingStart = existingTask.getStartTime();
			LocalTime existingEnd = existingStart.plusSeconds((long) existingTask.getMandatoryTime().toSeconds());
			return newStart.isBefore(existingEnd) && newEnd.isAfter(existingStart);
		});
	}
	
	private void sortTasksByTime() {
		taskObservableList.sort(Comparator.comparing(Task::getStartTime));
	}
}
