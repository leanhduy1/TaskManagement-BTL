package com.btl.taskmanagement.Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class PomodoroController {
	@FXML
	Label time_lbl;
	@FXML
	Button end_button;
	@FXML
	Button start_button;
	
	public void start(ActionEvent e){
		time_lbl.setText("20:00");
	}
	
	public void end(ActionEvent e){
		time_lbl.setText("00:00");
	}
}
