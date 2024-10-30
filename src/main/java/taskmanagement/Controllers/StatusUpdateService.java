package taskmanagement.Controllers;

import taskmanagement.Models.Day;
import taskmanagement.AppManager;
import javafx.application.Platform;
import javafx.concurrent.ScheduledService;
import javafx.concurrent.Task;
import javafx.util.Duration;

import java.time.LocalDate;
import java.time.LocalTime;


public class StatusUpdateService extends ScheduledService<Void> {
	
	public StatusUpdateService() {
		setPeriod(Duration.seconds(1));
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
		Day day = AppManager.selectedDay;
		LocalDate currentDate = LocalDate.now();
		LocalTime currentTime = LocalTime.now();
		
		day.getTaskObservableList().forEach(task -> {
			boolean isBeforeToday = day.getDate().isBefore(currentDate);
			boolean isTodayAndReady = task.isReady() && day.getDate().equals(currentDate);
			boolean isTaskExpired = isTodayAndReady && task.getStartTime().plusMinutes(5).isBefore(currentTime);
			
			if (isBeforeToday || isTaskExpired) {
				Platform.runLater(task::setTaskFailed);
			}
		});
	}
	
	
}
