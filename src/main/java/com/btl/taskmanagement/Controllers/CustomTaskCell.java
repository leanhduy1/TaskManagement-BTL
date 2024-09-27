package com.btl.taskmanagement.Controllers;

import com.btl.taskmanagement.Models.Task;
import com.btl.taskmanagement.ViewFactory;
import javafx.geometry.Pos;
import javafx.scene.control.ListCell;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.paint.Color;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.geometry.Insets;
import javafx.scene.effect.ColorAdjust;

public class CustomTaskCell extends ListCell<Task> {
	@Override
	protected void updateItem(Task item, boolean empty) {
		super.updateItem(item, empty);
		if (empty || item == null) {
			setText(null);
			setGraphic(null);
		} else {
			HBox cell = new HBox();
			cell.setSpacing(10);
			
			Text taskName = new Text(item.getTaskName());
			taskName.setFont(Font.font("Arial", FontWeight.BOLD, 14));
			
			CornerRadii cornerRadii = new CornerRadii(0);
			
			// Set brighter colors for each importance level
			BackgroundFill backgroundFill = null;
			switch (item.getImportanceLevel()) {
				case LOW:
					backgroundFill = new BackgroundFill(Color.web("#90EE90"), cornerRadii, Insets.EMPTY);
					taskName.setFill(Color.web("#006400"));
					break;
				case MEDIUM:
					backgroundFill = new BackgroundFill(Color.web("#FFD700"), cornerRadii, Insets.EMPTY);
					taskName.setFill(Color.web("#D93131"));
					break;
				case HIGH:
					backgroundFill = new BackgroundFill(Color.web("#FF6F61"), cornerRadii, Insets.EMPTY);
					taskName.setFill(Color.WHITE);
					break;
			}
			
			cell.setBackground(new Background(backgroundFill));
			
			// Kiểm tra nếu cell được chọn và làm sáng màu nền
			if (isSelected()) {
				ColorAdjust colorAdjust = new ColorAdjust();
				colorAdjust.setBrightness(-0.3); // Làm sáng hơn 30%
				cell.setEffect(colorAdjust);
				ViewFactory.selectedTask = item; // lưu task để chuyển sang pomodoro window
			} else {
				cell.setEffect(null);
			}
			
			cell.getChildren().addAll(taskName);
			
			// Spacer để mở rộng cell theo chiều ngang
			Region spacer = new Region();
			HBox.setHgrow(spacer, Priority.ALWAYS);
			cell.getChildren().add(spacer);
			
			cell.setPrefHeight(60);
			cell.setAlignment(Pos.CENTER_LEFT);
			cell.setPadding(new Insets(0, 10, 0, 10));
			
			setGraphic(cell);
			setText(null);
		}
	}
}
