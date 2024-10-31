package taskmanagement.Controllers;

/* Sử dụng list view để hiện thị các task có trong ngày
Xử lý sự kiện với 3 nút ADD, DELETE, START cho task được select trong list view
Tạo dialog để xử lý việc thêm task
*/

import taskmanagement.Models.Day;
import taskmanagement.Models.Task;

import taskmanagement.AppManager;
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
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Optional;
import java.util.ResourceBundle;

public class DayViewController implements Initializable {
	private Day day;
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
		day = AppManager.selectedDay;
		
		// Đặt custom cell và set item cho list view
		listView.setCellFactory(_ -> new TaskCellDayWindow());
		listView.setItems(day.getTaskObservableList());
		
		dateLabel.setText(day.getDate().toString());
		dateLabel.setFont(Font.font("System", FontWeight.BOLD, 12));
		
		updateCompletion();
		addImportanceLegend();
	}

	// Tạo hộp thoại để thêm công việc
	@FXML
	public void handleAddTask() {
		Dialog<Task> dialog = new Dialog<>();
		dialog.setTitle("Thêm công việc mới");
		dialog.setHeaderText("Nhập chi tiết cho công việc mới");

		ButtonType addButtonType = new ButtonType("Thêm", ButtonBar.ButtonData.OK_DONE);
		dialog.getDialogPane().getButtonTypes().addAll(addButtonType, ButtonType.CANCEL);
		
		// Tạo các trường nhập thông tin của công việc
		TextField taskNameField = new TextField();
		taskNameField.setPromptText("Tên công việc");

		TextField startTimeField = new TextField();
		startTimeField.setPromptText("Thời điểm bắt đầu (H:mm)");

		TextField focusTimeField = new TextField();
		focusTimeField.setPromptText("Quãng tập trung (phút)");

		TextField breakTimeField = new TextField();
		breakTimeField.setPromptText("Quãng nghỉ (phút)");

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
				new Label("Mức độ quan trọng:"), importanceLevelChoiceBox,
				new Label("Thời gian bắt buộc:"), mandatoryTimeField
		);

		dialog.getDialogPane().setContent(content);

		/* Đặt sự kiện cho nút thêm trong dialog
		Sử dụng addEventFilter để có thể hủy event bằng event.consume nếu người dùng nhập sai*/
		final Button addButton = (Button) dialog.getDialogPane().lookupButton(addButtonType);
		addButton.addEventFilter(ActionEvent.ACTION, event -> {
			try {
				String taskName = taskNameField.getText();
				LocalTime startTime = LocalTime.parse(startTimeField.getText(), DateTimeFormatter.ofPattern("H:mm"));
				Duration focusTime = Duration.minutes(Integer.parseInt(focusTimeField.getText()));
				Duration breakTime = Duration.minutes(Integer.parseInt(breakTimeField.getText()));
				Task.Priority importanceLevel = importanceLevelChoiceBox.getValue();
				Duration mandatoryTime = Duration.minutes(Integer.parseInt(mandatoryTimeField.getText()));

				Task newTask = new Task(taskName, startTime, focusTime, breakTime, importanceLevel, mandatoryTime);
				
				boolean valid = true;
				boolean isBeforeToday = day.getDate().isBefore(LocalDate.now());
				boolean isTodayButBeforeNow = day.getDate().isEqual(LocalDate.now()) && newTask.getStartTime().isBefore(LocalTime.now());
				
				// Chặn việc tạo task có start time trước thời điểm hiện tại
				if(isBeforeToday || isTodayButBeforeNow) {
					Alert alert = new Alert(Alert.AlertType.WARNING);
					alert.setHeaderText("Đã quá thời điểm bắt đầu task");
					alert.setContentText("Không thể tạo công việc với thời gian bắt đầu trước thời điểm hiện tại. Vui lòng chọn thời gian bắt đầu phù hợp.");
					alert.showAndWait();
					event.consume();
					valid = false;
				}
				
				// Xác nhận có tạo task với thời gian làm việc bị trùng lặp không
				if (isTimeConflict(newTask)) {
					Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
					alert.setHeaderText("Thời gian của công việc này trùng với một công việc khác.");
					alert.setContentText("Bạn có chắc chắn muốn tạo task này không ?");
					Optional<ButtonType> result = alert.showAndWait();
					if (result.isEmpty() || result.get() != ButtonType.OK) {
						event.consume();
						valid = false;
					}
				}
				if(valid) day.addTask(newTask);
			} catch (DateTimeParseException e) {
				showErrorDialog("Lỗi định dạng", "Thời gian bắt đầu phải theo định dạng HH:mm.");
				event.consume();
			} catch (NumberFormatException e) {
				showErrorDialog("Lỗi định dạng", "Thời gian tập trung, nghỉ và bắt buộc phải là số nguyên.");
				event.consume();
			} catch (Exception e) {
				showErrorDialog("Dữ liệu không hợp lệ", "Vui lòng đảm bảo tất cả các trường đều được nhập đúng định dạng.");
				event.consume();
			}
		});
		dialog.showAndWait();
	}
	
	// Kiểm tra newTask có trùng với quãng làm việc của các công việc đã có không
	private boolean isTimeConflict(Task newTask) {
		LocalTime newStart = newTask.getStartTime();
		LocalTime newEnd = newStart.plusSeconds((long) newTask.getMandatoryTime().toSeconds());
		
		return day.getTaskObservableList().stream().anyMatch(existingTask -> {
			LocalTime existingStart = existingTask.getStartTime();
			LocalTime existingEnd = existingStart.plusSeconds((long) existingTask.getMandatoryTime().toSeconds());
			return newStart.isBefore(existingEnd) && newEnd.isAfter(existingStart);
		});
	}
	
	private void showErrorDialog(String header, String content) {
		Alert alert = new Alert(Alert.AlertType.ERROR);
		alert.setHeaderText(header);
		alert.setContentText(content);
		alert.showAndWait();
	}
	
	@FXML
	public void handleDeleteTask() {
		// Kiểm tra đã task nào được chọn ở list view chưa
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

	public void handleStartTask() throws IOException {
		// Kiểm tra đã task nào được chọn ở list view chưa
		if(listView.getSelectionModel().getSelectedItem() == null){
			return;
		}
		if(AppManager.selectedTask.isReady()){
			Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
			alert.setContentText("Xác nhận bắt đầu làm việc");
			Optional<ButtonType> result = alert.showAndWait();
			if (result.isPresent() && result.get() == ButtonType.OK) {
				AppManager.switchToPomodoroWindow();
			}
		}
		else if(AppManager.selectedTask.isDone()){
			Alert alert = new Alert(Alert.AlertType.INFORMATION);
			alert.setHeaderText("TASK DONE");
			alert.setContentText("Bạn đã hoàn thành công việc này rồi");
			alert.show();
		}
		else if(AppManager.selectedTask.isFail()){
			Alert alert = new Alert(Alert.AlertType.INFORMATION);
			alert.setHeaderText("TASK FAILED");
			alert.setContentText("Đã quá hạn để làm việc này");
			alert.show();
		}
		listView.getSelectionModel().clearSelection();
	}
	
	@FXML
	public void handleExitButton(){
		AppManager.switchToMainWindow();
	}
	
	// Cập nhật phần trăm công việc đã thực hiện được trong ngày
	private void updateCompletion() {
		long completedTasks = day.getTaskObservableList().stream()
			.filter(Task::isDone)
			.count();
		long totalTasks = day.getTaskObservableList().size();
		double completion = (totalTasks > 0) ? (double) completedTasks / totalTasks : 0;
		
		completionBar.setProgress(completion);
		
		completionPercentage.setText(String.format("%.0f%%", completion * 100));
	}
	
	// Chỉ cho người dùng biết màu task trong list view tương ứng với mức độ quan trọng
	private void addImportanceLegend() {
		Label text = new Label("Importance");
		HBox lowPriority = createPriorityBox("LOW", Color.web("#A8E6CF"));
		HBox mediumPriority = createPriorityBox("MEDIUM", Color.web("#FFD54F"));
		HBox highPriority = createPriorityBox("HIGH", Color.web("#FFAB91"));
		
		lowPriority.setPadding(new Insets(5));
		mediumPriority.setPadding(new Insets(5));
		highPriority.setPadding(new Insets(5));
		text.setPadding(new Insets(5, 5, 0, 5));
		
		importanceLegendBox.getChildren().addAll(text, lowPriority, mediumPriority, highPriority);
	}
	
	private HBox createPriorityBox(String labelText, Color color) {
		Label label = new Label(labelText);
		Rectangle colorBox = new Rectangle(15, 15, color);
		colorBox.setStroke(Color.BLACK);
		
		HBox box = new HBox(colorBox, label);
		box.setSpacing(5);
		return box;
	}
}
