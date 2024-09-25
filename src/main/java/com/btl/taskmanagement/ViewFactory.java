package com.btl.taskmanagement;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class ViewFactory {
	public static Stage stage;
	public static Scene scene;
	public static Scene mainWindow;
	public static Parent root;
	
	public static void switchScene(String path) throws IOException {
		FXMLLoader loader = new FXMLLoader(ViewFactory.class.getResource(path));
		root = loader.load();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}
	
	public static void switchToDayWindow() throws IOException {
		switchScene("/FXML/day-window.fxml");
	}
	
	public static void switchToPomodoroWindow() throws IOException {
		stage.resizableProperty().setValue(false);
		switchScene("/FXML/pomodoro-window.fxml");
	}
	
	public static void switchToMainWindow() {
		stage.setScene(mainWindow);
		stage.show();
	}
}

