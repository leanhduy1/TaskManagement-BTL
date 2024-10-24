package com.btl.taskmanagement.Models;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalTime;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.util.Duration;

public class Task implements Serializable {
	@Serial
	private static final long serialVersionUID = 1L;
	
	
	public enum State {
		READY, FOCUS, BREAK, STOPPED, FAIL, DONE
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
	
	private transient ObjectProperty<State> currentStateProperty;
	
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
		this.sessionElapsedTime = Duration.ZERO;
		this.totalTime = Duration.ZERO;
		this.currentState = State.READY;
		this.currentStateProperty = new SimpleObjectProperty<>(currentState);
	}
	
	public ObjectProperty<State> currentStateProperty() {
		if (currentStateProperty == null) {
			currentStateProperty = new SimpleObjectProperty<>(currentState);
		}
		return currentStateProperty;
	}
	
	public void setCurrentState(State state) {
		this.currentState = state;
		this.currentStateProperty().set(state);
	}
	
	public State getCurrentState() {
		return currentState;
	}
	
	public void startFocus() {
		setCurrentState(State.FOCUS);
		sessionElapsedTime = Duration.ZERO;
	}
	
	public void startBreak() {
		setCurrentState(State.BREAK);
		sessionElapsedTime = Duration.ZERO;
	}
	
	public void stop() {
		setCurrentState(State.STOPPED);
		sessionElapsedTime = Duration.ZERO;
		totalTime = Duration.ZERO;
	}
	
	public void updateTime(Duration duration) {
		if (getCurrentState() == State.FOCUS || getCurrentState() == State.BREAK) {
			sessionElapsedTime = sessionElapsedTime.add(duration);
		}
		if (getCurrentState() == State.FOCUS) {
			totalTime = totalTime.add(duration);
		}
	}
	
	// Getter và Setter cho các thuộc tính khác
	public Priority getImportanceLevel() {
		return importanceLevel;
	}
	
	public LocalTime getStartTime() {
		return startTime;
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
	
	public Duration getMandatoryTime() {
		return mandatoryTime;
	}
	
	public Duration getFocusTime() {
		return focusTime;
	}
	
	public Duration getSessionRemainingTime() {
		Duration time = getCurrentState() == State.BREAK ? breakTime : focusTime;
		return time.subtract(sessionElapsedTime);
	}
	
	public boolean isFinishedSession() {
		return getSessionRemainingTime().lessThanOrEqualTo(Duration.ZERO);
	}
	
	public boolean isDone() {
		return getCurrentState() == State.DONE;
	}
	
	public boolean isReady() {
		return getCurrentState() == State.READY;
	}
	
	public boolean isRunning() {
		return getCurrentState() == State.FOCUS || getCurrentState() == State.BREAK;
	}
	
	public boolean isFocus() {
		return getCurrentState() == State.FOCUS;
	}
	
	public boolean isBreak() {
		return getCurrentState() == State.BREAK;
	}
	
	public boolean isFail() {
		return getCurrentState() == State.FAIL;
	}
	
	public void setTaskDone() {
		setCurrentState(State.DONE);
	}
	
	public void setTaskFailed() {
		setCurrentState(State.FAIL);
	}
	
	
	
}
