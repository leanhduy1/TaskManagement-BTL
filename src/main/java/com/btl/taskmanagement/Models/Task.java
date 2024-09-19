package com.btl.taskmanagement.Models;

import javafx.beans.property.*;

import java.time.Duration;
import java.time.LocalTime;

public class Task {
	private final StringProperty taskName = new SimpleStringProperty();
	private final ObjectProperty<LocalTime> startTime = new SimpleObjectProperty<>();
	private final ObjectProperty<Duration> focusTime =  new SimpleObjectProperty<>();
	private final ObjectProperty<Duration> breakTime =  new SimpleObjectProperty<>();
	private final StringProperty taskCategory = new SimpleStringProperty();
	private final IntegerProperty importanceLevel = new SimpleIntegerProperty();
	
	
	public StringProperty taskNameProperty() {
		return taskName;
	}
	
	public ObjectProperty<LocalTime> startTimeProperty() {
		return startTime;
	}
	
	public ObjectProperty<Duration> focusTimeProperty() {
		return focusTime;
	}
	
	public ObjectProperty<Duration> breakTimeProperty() {
		return breakTime;
	}
	
	public StringProperty taskCategoryProperty() {
		return taskCategory;
	}
	
	public IntegerProperty importanceLevelProperty() {
		return importanceLevel;
	}
	
	public String getTaskName() {
		return taskName.get();
	}
	
	public void setTaskName(String taskName) {
		this.taskName.set(taskName);
	}
	
	public LocalTime getStartTime() {
		return startTime.get();
	}
	
	public void setStartTime(LocalTime startTime) {
		this.startTime.set(startTime);
	}
	
	public Duration getFocusTime() {
		return focusTime.get();
	}
	
	public void setFocusTime(Duration focusTime) {
		this.focusTime.set(focusTime);
	}
	
	public Duration getBreakTime() {
		return breakTime.get();
	}
	
	public void setBreakTime(Duration breakTime) {
		this.breakTime.set(breakTime);
	}
	
	public String getTaskCategory() {
		return taskCategory.get();
	}
	
	public void setTaskCategory(String taskCategory) {
		this.taskCategory.set(taskCategory);
	}
	
	public int getImportanceLevel() {
		return importanceLevel.get();
	}
	
	public void setImportanceLevel(int importanceLevel) {
		this.importanceLevel.set(importanceLevel);
	}
}
	
