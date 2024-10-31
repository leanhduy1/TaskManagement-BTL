package taskmanagement.Models;

// Calendar dùng để tải và lưu lại các week object

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

public class Calendar implements Serializable {
	@Serial
	private static final long serialVersionUID = 1L;
	private final Map<LocalDate, Week> weeks = new HashMap<>();
	private LocalDate startOfCurrentWeek;
	
	// Đặt startOfCurrentWeek là đầu tuần
	public Calendar() {
		this.startOfCurrentWeek = LocalDate.now().with(DayOfWeek.MONDAY);
		updateWeekMap();
	}
	
	public void updateWeekMap() {
		if(!weeks.containsKey(startOfCurrentWeek)) {
			weeks.put(startOfCurrentWeek, loadWeek());
		}
	}
	
	// Trả về 1 week object đã được lưu hoặc 1 object mới nếu chưa có
	private Week loadWeek() {
		File file = new File(getWeekFilePath(startOfCurrentWeek));
		if (!file.exists()) {
			return new Week(startOfCurrentWeek);
		}
		try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
			return (Week) ois.readObject();
		} catch (IOException | ClassNotFoundException e) {
			return new Week(startOfCurrentWeek);
		}
	}
	
	// Trả về đường dẫn tới week object file
	private String getWeekFilePath(LocalDate weekStart) {
		return Paths.get(System.getProperty("user.home"), "Documents", "saved-weeks",
			weekStart.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) + ".dat").toString();
	}
	
	
	public void setToNextWeek() {
		startOfCurrentWeek = startOfCurrentWeek.plusWeeks(1);
		updateWeekMap();
	}
	
	public void setToPreviousWeek() {
		startOfCurrentWeek = startOfCurrentWeek.minusWeeks(1);
		updateWeekMap();
	}
	
	// Trả về đầu tuần của 1 date
	public void setToAnotherWeek(LocalDate date) {
		startOfCurrentWeek = date.with(DayOfWeek.MONDAY);
		updateWeekMap();
	}
	
	public LocalDate getStartOfCurrentWeek() {
		return startOfCurrentWeek;
	}
	
	// Lưu các week object có trong map lại
	public void saveWeeksToFile() throws IOException {
		Path directoryPath = Paths.get(System.getProperty("user.home"), "Documents", "saved-weeks");
		// Tạo thư mục saved-weeks trong thư mục Documents
		if (Files.notExists(directoryPath)) {
			Files.createDirectories(directoryPath);
		}
		for (Map.Entry<LocalDate, Week> entry : weeks.entrySet()) {
			LocalDate weekStart = entry.getKey();
			Week week = entry.getValue();
			try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(getWeekFilePath(weekStart)))) {
				oos.writeObject(week);
			}
		}
	}
	
	
	public Week getCurrentWeek() {
		return weeks.get(startOfCurrentWeek);
	}
}
