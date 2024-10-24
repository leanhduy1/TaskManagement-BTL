package com.btl.taskmanagement;

import com.btl.taskmanagement.Models.Calendar;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

public class App extends Application {
	@Override
	public void init() {
		ViewController.calendar = new Calendar();
	}
	public void start(Stage stage) throws IOException {
		FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("/Fxml/main-window.fxml"));
		Scene mainWindow = new Scene(fxmlLoader.load());
		ViewController.stage = stage;
		ViewController.mainWindow = mainWindow;
		ViewController.switchToMainWindow();
	}
	public void stop() throws IOException {
		ViewController.calendar.saveWeeksToFile();
	}
	public static void main(String[] args) {
		launch();
	}
}
