package taskmanagement;

import taskmanagement.Controllers.StatusUpdateService;
import taskmanagement.Models.Calendar;
import taskmanagement.Models.Day;
import taskmanagement.Models.Task;
import javafx.concurrent.Worker;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class AppManager {
	public static Stage stage;
	public static Scene mainWindow;
	public static Calendar calendar;
	public static Day selectedDay;
	public static Task selectedTask;
	
	private static final StatusUpdateService statusUpdateService = new StatusUpdateService();
	
	public static void switchToDayWindow() throws IOException {
		if (statusUpdateService.getState() == Worker.State.READY) {
			statusUpdateService.start();
		} else if (statusUpdateService.getState() == Worker.State.CANCELLED) {
			statusUpdateService.reset();
			statusUpdateService.start();
		}
		loadAndSetScene("/FXML/day-window.fxml");
	}
	
	public static void switchToPomodoroWindow() throws IOException {
		if (statusUpdateService.isRunning()) {
			statusUpdateService.cancel();
		}
		loadAndSetScene("/FXML/pomodoro-window.fxml");
	}
	
	public static void switchToMainWindow() {
		if (statusUpdateService.isRunning()) {
			statusUpdateService.cancel();
		}
		stage.setScene(mainWindow);
		stage.show();
	}
	
	private static void loadAndSetScene(String fxmlPath) throws IOException {
		FXMLLoader loader = new FXMLLoader(AppManager.class.getResource(fxmlPath));
		Parent root = loader.load();
		Scene scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}
	
}
