package com.btl.taskmanagement.Models;


import java.io.Serial;
import java.io.Serializable;
import javafx.util.Duration;
import java.time.LocalTime;

public class Task implements Serializable {
	@Serial
	private static final long serialVersionUID = 1L;

	public enum State {
		FOCUS, BREAK, STOPPED
	}
	
	public enum Priority {
		LOW, MEDIUM, HIGH
	}
	
	private final String taskName;
	private final LocalTime startTime;
	private final Duration focusTime;
	private final Duration breakTime;
	private final String taskCategory;
	private final Priority importanceLevel;
	private final Duration mandatoryTime;
	private State currentState;
	private Duration sessionElapsedTime;
	private Duration totalTime;
	
	public Task(String taskName, LocalTime startTime, Duration focusTime, Duration breakTime, String taskCategory, Priority importanceLevel, Duration mandatoryTime) {
		this.taskName = taskName;
		this.startTime = startTime;
		this.focusTime = focusTime;
		this.breakTime = breakTime;
		this.taskCategory = taskCategory;
		this.importanceLevel = importanceLevel;
		this.mandatoryTime = mandatoryTime;
		this.currentState = Task.State.STOPPED;
		this.sessionElapsedTime = Duration.ZERO;
		this.totalTime = Duration.ZERO;
	}
	
	public Priority getImportanceLevel() {
		return importanceLevel;
	}
	
	public LocalTime getStartTime() {
		return startTime;
	}
	
	public void startFocus() {
		currentState = State.FOCUS;
		sessionElapsedTime = Duration.ZERO;
	}
	
	public void startBreak() {
		currentState = State.BREAK;
		sessionElapsedTime = Duration.ZERO;
	}
	
	public void stop() {
		currentState = State.STOPPED;
		sessionElapsedTime = Duration.ZERO;
		totalTime = Duration.ZERO;
	}
	
	public void updateTime(Duration duration) {
		if (currentState != State.STOPPED) {
			sessionElapsedTime = sessionElapsedTime.add(duration);
			if (currentState == State.FOCUS) {
				totalTime = totalTime.add(duration);
			}
		}
	}

	public Duration getBreakTime() {
		return breakTime;
	}

	public String getTaskCategory() {
		return taskCategory;
	}

	public String getTaskName() {
		return taskName;
	}
	
	public Duration getTotalTime() {
		return totalTime;
	}
	
	public Duration getRemainingTime() {
		Duration time = currentState == State.BREAK ? breakTime : focusTime;
		return time.subtract(sessionElapsedTime);
	}
	
	public boolean isFinishedSession() {
		return getRemainingTime().lessThanOrEqualTo(Duration.ZERO);
	}
	
	public boolean isMandatoryTimeMet() {
		return totalTime.greaterThanOrEqualTo(mandatoryTime);
	}
	
	public Duration getFocusTime() {
		return focusTime;
	}
	
	public boolean isRunning() {
		return currentState != State.STOPPED;
	}
	
	public boolean isBreak() {
		return currentState == State.BREAK;
	}
	
	public Duration getMandatoryTime(){
		return mandatoryTime;
	}
	
}
	
