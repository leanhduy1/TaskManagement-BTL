package com.btl.taskmanagement.Models;

import javafx.util.Duration;

public class Pomodoro {
	public enum State {
		FOCUS, BREAK, STOPPED
	}
	
	private final Duration focusTime;
	private final Duration breakTime;
	private final Duration mandatoryTime;
	private State currentState;
	private Duration sessionElapsedTime;
	private Duration totalTime;
	
	public Pomodoro(Duration focusTime, Duration breakTime, Duration mandatoryTime) {
		this.focusTime = focusTime;
		this.breakTime = breakTime;
		this.mandatoryTime = mandatoryTime;
		this.currentState = State.STOPPED;
		this.sessionElapsedTime = Duration.ZERO;
		this.totalTime = Duration.ZERO;
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
