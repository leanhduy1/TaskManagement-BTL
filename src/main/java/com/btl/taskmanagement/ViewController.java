// ViewFactory.java
package com.btl.taskmanagement;

import com.btl.taskmanagement.Controllers.StatusUpdateService;
import com.btl.taskmanagement.Models.Calendar;
import com.btl.taskmanagement.Models.Day;
import com.btl.taskmanagement.Models.Task;
import javafx.concurrent.Worker;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;

public class ViewFactory {
	public static Stage stage;
	public static Scene mainWindow;
	public static Calendar calendar;
	public static Day selectedDay;
	public static Task selectedTask;
	private static Scene dayWindow;
	private static final StatusUpdateService statusUpdateService = new StatusUpdateService();
	
	public static void switchToDayWindow() throws IOException {
		if (dayWindow == null) {
			FXMLLoader loader = new FXMLLoader(ViewFactory.class.getResource("/FXML/day-window.fxml"));
			Parent root = loader.load();
			dayWindow = new Scene(root);
		}
		
		if (statusUpdateService.getState() == Worker.State.READY) {
			statusUpdateService.start();
		} else if (statusUpdateService.getState() == Worker.State.CANCELLED) {
			statusUpdateService.reset();
			statusUpdateService.start();
		}
		
		stage.setScene(dayWindow);
		stage.show();
	}
	
	public static void switchToPomodoroWindow() throws IOException {
		if (statusUpdateService.getState() == Worker.State.READY) {
			statusUpdateService.cancel();
		}
		FXMLLoader loader = new FXMLLoader(ViewFactory.class.getResource("/FXML/pomodoro-window.fxml"));
		Parent root = loader.load();
		Scene pomodoroWindow = new Scene(root);
		stage.setScene(pomodoroWindow);
		stage.show();
	}
	
	public static void switchToMainWindow() {
		if (statusUpdateService.getState() == Worker.State.READY) {
			statusUpdateService.cancel();
		}
		stage.setScene(mainWindow);
		stage.show();
	}
}
