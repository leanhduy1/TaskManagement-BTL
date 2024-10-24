package com.btl.taskmanagement.Controllers;

import com.btl.taskmanagement.Models.Day;
import com.btl.taskmanagement.Models.Task;

import com.btl.taskmanagement.ViewController;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Optional;
import java.util.ResourceBundle;

public class DayViewController implements Initializable {
	protected Day day;
	@FXML
	private ListView<Task> listView;
	@FXML
	private Button addButton, deleteButton, startButton, exitButton;
	@FXML
	private ProgressBar completionBar;
	@FXML
	private Label completionPercentage, dateLabel;
	@FXML
	private VBox importanceLegendBox;

	
	@Override
	public void initialize(URL url, ResourceBundle resourceBundle) {
		day = ViewController.selectedDay;
		listView.setCellFactory(_ -> new TaskCellDayWindow());
		listView.setItems(day.getTaskObservableList());
		
		dateLabel.setText(day.getDate().toString());
		dateLabel.setFont(Font.font("System", FontWeight.BOLD, 12));
		
		
		updateCompletion();
		addImportanceLegend();
	}

	@FXML
	public void handleAddTask() {
		Dialog<Task> dialog = new Dialog<>();
		dialog.setTitle("Thêm Công Việc Mới");
		dialog.setHeaderText("Nhập chi tiết cho công việc mới");

		ButtonType addButtonType = new ButtonType("Thêm", ButtonBar.ButtonData.OK_DONE);
		dialog.getDialogPane().getButtonTypes().addAll(addButtonType, ButtonType.CANCEL);

		TextField taskNameField = new TextField();
		taskNameField.setPromptText("Tên công việc");

		TextField startTimeField = new TextField();
		startTimeField.setPromptText("Thời gian bắt đầu (H:mm)");

		TextField focusTimeField = new TextField();
		focusTimeField.setPromptText("Thời gian tập trung (phút)");

		TextField breakTimeField = new TextField();
		breakTimeField.setPromptText("Thời gian nghỉ (phút)");

		TextField taskCategoryField = new TextField();
		taskCategoryField.setPromptText("Danh mục");

		ChoiceBox<Task.Priority> importanceLevelChoiceBox = new ChoiceBox<>();
		importanceLevelChoiceBox.setItems(FXCollections.observableArrayList(Task.Priority.values()));
		importanceLevelChoiceBox.getSelectionModel().selectFirst();

		TextField mandatoryTimeField = new TextField();
		mandatoryTimeField.setPromptText("Thời gian bắt buộc (phút)");

		VBox content = new VBox(10);
		content.getChildren().addAll(
				new Label("Tên công việc:"), taskNameField,
				new Label("Thời gian bắt đầu:"), startTimeField,
				new Label("Thời gian tập trung:"), focusTimeField,
				new Label("Thời gian nghỉ:"), breakTimeField,
				new Label("Danh mục:"), taskCategoryField,
				new Label("Mức độ quan trọng:"), importanceLevelChoiceBox,
				new Label("Thời gian bắt buộc:"), mandatoryTimeField
		);

		dialog.getDialogPane().setContent(content);

		final Button addButton = (Button) dialog.getDialogPane().lookupButton(addButtonType);
		addButton.addEventFilter(ActionEvent.ACTION, event -> {
			try {
				String taskName = taskNameField.getText();
				LocalTime startTime = LocalTime.parse(startTimeField.getText(), DateTimeFormatter.ofPattern("H:mm"));
				Duration focusTime = Duration.minutes(Integer.parseInt(focusTimeField.getText()));
				Duration breakTime = Duration.minutes(Integer.parseInt(breakTimeField.getText()));
				String taskCategory = taskCategoryField.getText();
				Task.Priority importanceLevel = importanceLevelChoiceBox.getValue();
				Duration mandatoryTime = Duration.minutes(Integer.parseInt(mandatoryTimeField.getText()));

				Task newTask = new Task(taskName, startTime, focusTime, breakTime, taskCategory, importanceLevel, mandatoryTime);

				if (!day.addTask(newTask)) {
					Alert alert = new Alert(Alert.AlertType.WARNING);
					alert.setHeaderText("Trùng lịch công việc");
					alert.setContentText("Thời gian của công việc này trùng với một công việc khác.");
					alert.showAndWait();
					event.consume(); // Ngăn dialog đóng
				}
			} catch (DateTimeParseException e) {
				Alert alert = new Alert(Alert.AlertType.ERROR);
				alert.setHeaderText("Lỗi định dạng");
				alert.setContentText("Thời gian bắt đầu phải theo định dạng HH:mm.");
				alert.showAndWait();
				event.consume(); // Ngăn dialog đóng
			} catch (NumberFormatException e) {
				Alert alert = new Alert(Alert.AlertType.ERROR);
				alert.setHeaderText("Lỗi định dạng");
				alert.setContentText("Thời gian tập trung, nghỉ và bắt buộc phải là số nguyên.");
				alert.showAndWait();
				event.consume(); // Ngăn dialog đóng
			} catch (Exception e) {
				Alert alert = new Alert(Alert.AlertType.ERROR);
				alert.setHeaderText("Dữ liệu không hợp lệ");
				alert.setContentText("Vui lòng đảm bảo tất cả các trường đều được nhập đúng định dạng.");
				alert.showAndWait();
				event.consume(); // Ngăn dialog đóng
			}
		});

		dialog.showAndWait();
	}

	@FXML
	public void handleDeleteTask() {
		if(listView.getSelectionModel().getSelectedItem() != null) {
			Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
			alert.setTitle("Confirmation");
			alert.setHeaderText("Delete task");
			alert.setContentText("Bạn có chắc muốn xóa task này không ?");
			Optional<ButtonType> result = alert.showAndWait();
			if (result.isPresent() && result.get() == ButtonType.OK) {
				day.removeTask(listView.getSelectionModel().getSelectedItem());
			}
			listView.getSelectionModel().clearSelection();
		}
	}

	private void updateCompletion() {

		double completedTasks = day.getTaskObservableList().stream().filter(Task::isDone).count();
		double totalTasks = day.getTaskObservableList().size();
		double completion = (totalTasks > 0) ? (completedTasks / totalTasks) : 0;
		completionBar.setProgress(completion);
		completionPercentage = new Label(String.format("%.0f%%", completion * 100));
	}


	private void addImportanceLegend() {
		HBox lowPriority = createPriorityBox("LOW", Color.web("#A8E6CF"));
		HBox mediumPriority = createPriorityBox("MEDIUM", Color.web("#FFD54F"));
		HBox highPriority = createPriorityBox("HIGH", Color.web("#FFAB91"));

		lowPriority.setPadding(new Insets(5));
		mediumPriority.setPadding(new Insets(5));
		highPriority.setPadding(new Insets(5));

		importanceLegendBox.getChildren().addAll(lowPriority, mediumPriority, highPriority);
	}

	private HBox createPriorityBox(String labelText, Color color) {
		Label label = new Label(labelText);
		Rectangle colorBox = new Rectangle(15, 15, color);
		colorBox.setStroke(Color.BLACK);

		HBox box = new HBox(colorBox, label);
		box.setSpacing(5);
		return box;
	}

	public void handleStartTask() throws IOException {
		if(listView.getSelectionModel().getSelectedItem() == null){
			return;
		}
		if(ViewController.selectedTask.isReady()){
			Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
			alert.setContentText("Xác nhận bắt đầu làm việc");
			Optional<ButtonType> result = alert.showAndWait();
			if (result.isPresent() && result.get() == ButtonType.OK) {
				ViewController.switchToPomodoroWindow();
			}
		}
		else if(ViewController.selectedTask.isDone()){
			Alert alert = new Alert(Alert.AlertType.INFORMATION);
			alert.setHeaderText("TASK DONE");
			alert.setContentText("Bạn đã hoàn thành công việc này rồi");
			alert.show();
		}
		else if(ViewController.selectedTask.isFail()){
			Alert alert = new Alert(Alert.AlertType.INFORMATION);
			alert.setHeaderText("TASK FAILED");
			alert.setContentText("Đã quá hạn để làm việc này");
			alert.show();
		}
	}
	
	@FXML
	public void handleExitButton(){
		ViewController.switchToMainWindow();
	}
}
