package com.btl.taskmanagement.Controllers;

import com.btl.taskmanagement.ViewFactory;
import javafx.application.Platform;
import javafx.concurrent.ScheduledService;
import javafx.concurrent.Task;
import javafx.util.Duration;

import java.time.LocalTime;


public class StatusUpdateService extends ScheduledService<Void> {
	
	public StatusUpdateService() {
		setPeriod(Duration.minutes(1));
	}
	
	@Override
	protected Task<Void> createTask() {
		return new Task<>() {
			@Override
			protected Void call() {
				updateStatus();
				return null;
			}
		};
	}
	
	
	private void updateStatus() {
		System.out.println("UPDATING STATUS");
		ViewFactory.selectedDay.getTaskObservableList().forEach(task -> {
			if (task.isReady() && task.getStartTime().isBefore(LocalTime.now())) {
				Platform.runLater(task::setTaskFailed);
			}
		});
	}
	
}
