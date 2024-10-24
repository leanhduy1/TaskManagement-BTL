package com.btl.taskmanagement.Controllers;

import com.btl.taskmanagement.Models.Task;
import com.btl.taskmanagement.ViewController;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.StringBinding;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.paint.Color;
import javafx.geometry.Insets;
import javafx.scene.effect.ColorAdjust;

public class TaskCellDayWindow extends ListCell<Task> {
	@Override
	protected void updateItem(Task item, boolean empty) {
		super.updateItem(item, empty);
		if (empty || item == null) {
			setText(null);
			setGraphic(null);
		} else {
			HBox cell = new HBox();
			cell.setSpacing(10);
			
			GridPane taskInfo = new GridPane();
			taskInfo.setVgap(5);
			taskInfo.setHgap(10);
			
			Text taskName = new Text(item.getTaskName());
			taskName.setFont(Font.font("System", FontWeight.BOLD, 14));
			taskName.setFill(Color.BLACK);
			taskInfo.add(taskName, 0, 0); // Tên nhiệm vụ ở hàng 0, cột 0
			
			Text startTime = new Text("Start: " + item.getStartTime());
			startTime.setFont(Font.font("System", FontWeight.NORMAL, 12));
			startTime.setFill(Color.BLACK);
			taskInfo.add(startTime, 0, 1); // Thời gian bắt đầu ở hàng 1, cột 0
			
			Text mandatoryTime = new Text("Min: " + item.getMandatoryTime().toMinutes() + " min");
			mandatoryTime.setFont(Font.font("System", FontWeight.NORMAL, 12));
			mandatoryTime.setFill(Color.BLACK);
			taskInfo.add(mandatoryTime, 1, 1); // Thời gian tối thiểu ở hàng 1, cột 1
			
			Text focusTime = new Text("Focus: " + item.getFocusTime().toMinutes() + " min");
			focusTime.setFont(Font.font("System", FontWeight.NORMAL, 12));
			focusTime.setFill(Color.BLACK);
			taskInfo.add(focusTime, 0, 2); // Thời gian làm ở hàng 2, cột 0
			
			Text breakTime = new Text("Break: " + item.getBreakTime().toMinutes() + " min");
			breakTime.setFont(Font.font("System", FontWeight.NORMAL, 12));
			breakTime.setFill(Color.BLACK);
			taskInfo.add(breakTime, 1, 2); // Thời gian nghỉ ở hàng 2, cột 1
			
			BackgroundFill backgroundFill = switch (item.getImportanceLevel()) {
				case LOW -> new BackgroundFill(Color.web("#A8E6CF"), new CornerRadii(5), Insets.EMPTY);
				case MEDIUM -> new BackgroundFill(Color.web("#FFD54F"), new CornerRadii(5), Insets.EMPTY);
				case HIGH -> new BackgroundFill(Color.web("#FFAB91"), new CornerRadii(5), Insets.EMPTY);
			};
			
			cell.setBackground(new Background(backgroundFill));
			
			if (isSelected()) {
				ColorAdjust colorAdjust = new ColorAdjust();
				colorAdjust.setBrightness(-0.2);
				cell.setEffect(colorAdjust);
				ViewController.selectedTask = item;
			} else {
				cell.setEffect(null);
			}
			
			cell.getChildren().addAll(taskInfo);
			
			
			Region spacer = new Region();
			HBox.setHgrow(spacer, Priority.ALWAYS);
			cell.getChildren().add(spacer);
			
			
			Label statusLabel = new Label();
			statusLabel.setFont(Font.font("System", FontWeight.BOLD, 12));
			statusLabel.setBackground(new Background(new BackgroundFill(Color.web("#F0F0F0"), new CornerRadii(5), Insets.EMPTY)));
			statusLabel.setPadding(new Insets(5));
			statusLabel.setTextFill(Color.BLACK);
			
			StringBinding statusBinding = Bindings.createStringBinding(() -> {
				Task.State state = item.currentStateProperty().get();
				return switch (state) {
					case DONE -> "DONE";
					case FAIL -> "FAIL";
					case READY -> "READY";
					case FOCUS -> "FOCUS";
					case BREAK -> "BREAK";
					case STOPPED -> "STOPPED";
				};
			}, item.currentStateProperty());
			
			statusLabel.textProperty().bind(statusBinding);
			
			cell.getChildren().add(statusLabel);
			
			cell.setPrefHeight(80);
			cell.setAlignment(Pos.CENTER_LEFT);
			cell.setPadding(new Insets(10, 10, 10, 10));
			
			setGraphic(cell);
			setText(null);
		}
	}
	
}
