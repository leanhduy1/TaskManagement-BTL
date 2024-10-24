// Calendar.java
package com.btl.taskmanagement.Models;

import java.io.*;
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
	
	public Calendar() {
		this.startOfCurrentWeek = LocalDate.now().with(DayOfWeek.MONDAY);
		updateWeekMap();
	}
	
	public void updateWeekMap() {
		if(!weeks.containsKey(startOfCurrentWeek)) {
			weeks.put(startOfCurrentWeek, loadWeek());
		}
	}
	
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
	
	private String getWeekFilePath(LocalDate weekStart) {
		String userHome = System.getProperty("user.home");
		return userHome + "/Documents/saved-weeks/" + weekStart.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) + ".dat";
	}
	
	public void setToNextWeek() {
		startOfCurrentWeek = startOfCurrentWeek.plusWeeks(1);
		updateWeekMap();
	}
	
	public void setToPreviousWeek() {
		startOfCurrentWeek = startOfCurrentWeek.minusWeeks(1);
		updateWeekMap();
	}
	
	public void setToAnotherWeek(LocalDate date) {
		startOfCurrentWeek = date.with(DayOfWeek.MONDAY);
		updateWeekMap();
	}
	
	public LocalDate getStartOfCurrentWeek() {
		return startOfCurrentWeek;
	}
	
	public void saveWeeksToFile() throws IOException {
		String userHome = System.getProperty("user.home");
		File directory = new File(userHome + "/Documents/saved-weeks");
		if (!directory.exists()) {
			directory.mkdirs();
		}
		
		for (Map.Entry<LocalDate, Week> entry : weeks.entrySet()) {
			LocalDate weekStart = entry.getKey();
			Week week = entry.getValue();
			try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(getWeekFilePath(weekStart)))) {
				oos.writeObject(week);
			}
		}
	}
	
	public Week getWeek(LocalDate date) {
		return weeks.get(date);
	}
	
	public Week getCurrentWeek() {
		return weeks.get(startOfCurrentWeek);
	}
}
