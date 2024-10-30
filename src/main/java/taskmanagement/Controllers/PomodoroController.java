package taskmanagement.Controllers;

import javafx.scene.media.AudioClip;
import taskmanagement.Models.Music;
import taskmanagement.Models.Task;
import taskmanagement.AppManager;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.Optional;
import java.util.ResourceBundle;

public class PomodoroController implements Initializable {
	
	private static final String NOTIFICATION_SOUND_PATH = "/Notification/notification.mp3";
	private AudioClip notificationSound;
	private Task task;
	private Timeline timeline;
	private Music music;
	private int songNumber;
	private MediaPlayer mediaPlayer;
	
	@FXML
	private Label countdownLabel, modeLabel, totalTimeLabel;
	@FXML
	private ComboBox<String> songPicker;
	@FXML
	private Button playPauseButton, exitButton;
	
	@Override
	public void initialize(URL url, ResourceBundle resourceBundle) {
		this.task = AppManager.selectedTask;
		countdownLabel.setText(formatTime((int) task.getFocusTime().toSeconds()));
		
		timeline = new Timeline(new KeyFrame(Duration.seconds(1), _ -> updateCountdown()));
		timeline.setCycleCount(Timeline.INDEFINITE);
		
		notificationSound = new AudioClip(
			Objects.requireNonNull(getClass().getResource(NOTIFICATION_SOUND_PATH)).toString()
		);
		
		this.music = new Music();
		if (!music.getSongs().isEmpty()) {
			setUpListSong();
		}
		
		AppManager.stage.setOnCloseRequest(event -> {
			event.consume();
			try {
				handleExit();
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		});
	}
	
	private void setUpListSong() {
		for (File song : music.getSongs()) {
			songPicker.getItems().add(
				song.getName().substring(0, song.getName().lastIndexOf('.'))
			);
		}
		songPicker.getSelectionModel().selectFirst();
		loadSelectedSong();
		songPicker.getSelectionModel().selectedIndexProperty().addListener((_, _, newVal) -> {
			songNumber = newVal.intValue();
			loadSelectedSong();
			playPauseButton.setText("Play");
		});
	}
	
	private void loadSelectedSong() {
		if (mediaPlayer != null && music.isPlaying()) {
			mediaPlayer.stop();
			mediaPlayer.dispose();
			music.stopPlaying();
		}
		
		File selectedSong = music.getSongs().get(songNumber);
		Media media = new Media(selectedSong.toURI().toString());
		mediaPlayer = new MediaPlayer(media);
		mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
	}
	
	@FXML
	public void handlePlayPause() {
		if (music.getSongs().isEmpty()) return;
		
		if (music.isPlaying()) {
			mediaPlayer.pause();
			music.stopPlaying();
			playPauseButton.setText("Play");
		} else {
			mediaPlayer.play();
			music.startPlaying();
			playPauseButton.setText("Pause");
		}
	}
	
	private void updateCountdown() {
		task.updateTime(Duration.seconds(1));
		if (task.isFinishedSession()) {
			notificationSound.play();
			if (task.isBreak()) {
				task.startFocus();
			} else {
				task.startBreak();
			}
		}
		updateUI();
	}
	
	private void updateUI() {
		countdownLabel.setText(formatTime((int) task.getSessionRemainingTime().toSeconds()));
		totalTimeLabel.setText("Focused time: " + formatTime((int) task.getTotalTime().toSeconds()));
	}
	
	private String formatTime(int seconds) {
		int hours = seconds / 3600;
		int minutes = (seconds % 3600) / 60;
		seconds = seconds % 60;
		
		if (hours > 0) {
			return String.format("%02d:%02d:%02d", hours, minutes, seconds);
		} else {
			return String.format("%02d:%02d", minutes, seconds);
		}
	}
	
	@FXML
	public void startPomodoro() {
		if (!task.isRunning()) {
			task.startFocus();
			updateUI();
			timeline.play();
			modeLabel.setText("Focus");
		}
	}
	
	@FXML
	public void stopPomodoro() {
		if (task.isRunning()) {
			task.stop();
			timeline.stop();
			modeLabel.setText("Break");
		}
	}
	
	@FXML
	private void handleExit() throws IOException {
		if (task.getTotalTime().greaterThanOrEqualTo(task.getMandatoryTime())) {
			task.setTaskDone();
			exitToDayWindow();
		} else {
			Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
			alert.setTitle("Xác nhận thoát");
			int totalRemainTime = (int) task.getMandatoryTime().subtract(task.getTotalTime()).toSeconds();
			int minutes = totalRemainTime / 60;
			int seconds = totalRemainTime % 60;
			alert.setContentText(String.format("Còn %d phút %d giây nữa để hoàn thành\nTask sẽ tính là không hoàn thành nếu bạn thoát bây giờ", minutes, seconds));
			Optional<ButtonType> result = alert.showAndWait();
			if (result.isPresent() && result.get() == ButtonType.OK) {
				task.setTaskFailed();
				exitToDayWindow();
			}
		}
		if (mediaPlayer != null) {
			mediaPlayer.stop();
		}
	}
	
	private void exitToDayWindow() throws IOException {
		if (mediaPlayer != null) mediaPlayer.stop();
		AppManager.switchToDayWindow();
		AppManager.stage.setOnCloseRequest(null);
	}
}
