package com.btl.taskmanagement.Models;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.io.Serial;
import java.io.Serializable;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;


public class Day implements Serializable {
	@Serial
	private static final long serialVersionUID = 1L;
	
	private final LocalDate date;
	private transient ObservableList<Task> taskObservableList;
	private List<Task> serializableList;
	
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
	
	@Serial
	private void writeObject(ObjectOutputStream oos) throws IOException {
		serializableList = new ArrayList<>(taskObservableList);
		oos.defaultWriteObject();
	}
	
	@Serial
	private void readObject(ObjectInputStream ois) throws IOException, ClassNotFoundException {
		ois.defaultReadObject();
		taskObservableList = FXCollections.observableArrayList(serializableList);
	}
	
}