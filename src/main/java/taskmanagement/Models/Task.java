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
	
	public enum State { READY, FOCUS, BREAK, STOPPED, FAIL, DONE }
	public enum Priority { LOW, MEDIUM, HIGH }
	
	private final String taskName;
	private final LocalTime startTime;
	private final Duration focusTime;
	private final Duration breakTime;
	private final Priority importanceLevel;
	private final Duration mandatoryTime;
	
	private State currentState;
	private transient ObjectProperty<State> currentStateProperty;
	private Duration sessionElapsedTime = Duration.ZERO;
	private Duration totalTime = Duration.ZERO;
	
	public Task(String taskName, LocalTime startTime, Duration focusTime, Duration breakTime, Priority importanceLevel, Duration mandatoryTime) {
		this.taskName = taskName;
		this.startTime = startTime;
		this.focusTime = focusTime;
		this.breakTime = breakTime;
		this.importanceLevel = importanceLevel;
		this.mandatoryTime = mandatoryTime;
		this.currentState = State.READY;
		this.currentStateProperty = new SimpleObjectProperty<>(currentState);
	}
	
	public ObjectProperty<State> currentStateProperty() {
		if (currentStateProperty == null) currentStateProperty = new SimpleObjectProperty<>(currentState);
		return currentStateProperty;
	}
	
	public void setCurrentState(State state) {
		this.currentState = state;
		currentStateProperty().set(state);
	}
	
	public void startFocus() { resetSession(State.FOCUS); }
	public void startBreak() { resetSession(State.BREAK); }
	public void stop() { setCurrentState(State.STOPPED); resetTimes(); }
	
	private void resetSession(State state) {
		setCurrentState(state);
		sessionElapsedTime = Duration.ZERO;
	}
	
	private void resetTimes() {
		sessionElapsedTime = Duration.ZERO;
		totalTime = Duration.ZERO;
	}
	
	public void updateTime(Duration duration) {
		if (isRunning()) {
			sessionElapsedTime = sessionElapsedTime.add(duration);
			if (isFocus()) totalTime = totalTime.add(duration);
		}
	}
	
	public Duration getSessionRemainingTime() {
		Duration time = isBreak() ? breakTime : focusTime;
		return time.subtract(sessionElapsedTime);
	}
	
	public boolean isFinishedSession() { return getSessionRemainingTime().lessThanOrEqualTo(Duration.ZERO); }
	public boolean isDone() { return currentState == State.DONE; }
	public boolean isReady() { return currentState == State.READY; }
	public boolean isRunning() { return currentState == State.FOCUS || currentState == State.BREAK; }
	public boolean isFocus() { return currentState == State.FOCUS; }
	public boolean isBreak() { return currentState == State.BREAK; }
	public boolean isFail() { return currentState == State.FAIL; }
	
	public void setTaskDone() { setCurrentState(State.DONE); }
	public void setTaskFailed() { setCurrentState(State.FAIL); }
	
	public Priority getImportanceLevel() { return importanceLevel; }
	public LocalTime getStartTime() { return startTime; }
	public Duration getBreakTime() { return breakTime; }
	public String getTaskName() { return taskName; }
	public Duration getTotalTime() { return totalTime; }
	public Duration getMandatoryTime() { return mandatoryTime; }
	public Duration getFocusTime() { return focusTime; }
}
