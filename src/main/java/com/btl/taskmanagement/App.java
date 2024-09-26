package com.btl.taskmanagement;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

public class App extends Application {
	@Override
	public void start(Stage stage) throws IOException {
		FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("/Fxml/main-window.fxml"));
		Scene mainWindow = new Scene(fxmlLoader.load());
		ViewFactory.stage = stage;
		ViewFactory.mainWindow = mainWindow;
		ViewFactory.switchToMainWindow();
		
	}
	
	public static void main(String[] args) {
		launch();
	}
}