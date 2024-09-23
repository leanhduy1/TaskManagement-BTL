package com.btl.taskmanagement.Models;


import java.io.Serial;
import java.io.Serializable;
import java.time.Duration;
import java.time.LocalTime;

public class Task implements Serializable {
	@Serial
	private static final long serialVersionUID = 1L;
	private final String taskName;
	private final LocalTime startTime;
	private final Duration focusTime;
	private final Duration breakTime;
	private final String taskCategory;
	private final int importanceLevel;
	private boolean completed = false;
	
	public Task(String taskName, LocalTime startTime, Duration focusTime, Duration breakTime, String taskCategory, int importanceLevel) {
		this.taskName = taskName;
		this.startTime = startTime;
		this.focusTime = focusTime;
		this.breakTime = breakTime;
		this.taskCategory = taskCategory;
		this.importanceLevel = importanceLevel;
	}
	
	
	
}
	
