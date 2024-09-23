package com.btl.taskmanagement.Models;

import java.io.*;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.TreeMap;

public class Calendar {
	private final Map<LocalDate, Week> weeks = new TreeMap<>();
	private Week currentWeek;
	private LocalDate startOfWeek;
	
	public Calendar() throws IOException, ClassNotFoundException {
		LocalDate today = LocalDate.now();
		this.startOfWeek = today.with(DayOfWeek.MONDAY);
		updateCurrentWeek();
	}
	
	public Week getCurrentWeek() {
		return currentWeek;
	}
	
	public LocalDate getStartOfWeek() {
		return startOfWeek;
	}
	
	public void setToNextWeek() throws IOException, ClassNotFoundException {
		this.startOfWeek = this.startOfWeek.plusWeeks(1);
		updateCurrentWeek();
	}
	
	public void setToPreviousWeek() throws IOException, ClassNotFoundException {
		this.startOfWeek = this.startOfWeek.minusWeeks(1);
		updateCurrentWeek();
	}
	
	
	public void updateCurrentWeek() throws IOException, ClassNotFoundException {
		if (weeks.containsKey(startOfWeek)) {
			currentWeek = weeks.get(startOfWeek);
			return;
		}
		
		String userHome = System.getProperty("user.home");
		File directory = new File(userHome + "/Documents/saved-weeks");
		if (!directory.exists()) {
			directory.mkdirs();
		}
		
		String filename = directory.getPath() + "/" + startOfWeek.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) + ".dat";
		File file = new File(filename);
		if (!file.exists()) {
			currentWeek = new Week(startOfWeek);
			weeks.put(startOfWeek, currentWeek);
			return;
		}
		ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file));
		currentWeek = (Week) ois.readObject();
		weeks.put(startOfWeek, currentWeek);
	}
	
	public void saveWeeksToFile() throws IOException {
		String userHome = System.getProperty("user.home");
		File directory = new File(userHome + "/Documents/saved-weeks");
		if (!directory.exists()) {
			directory.mkdirs();
		}
		
		for (Map.Entry<LocalDate, Week> entry : weeks.entrySet()) {
			LocalDate startOfWeek = entry.getKey();
			Week week = entry.getValue();
			
			String filename = directory.getPath() + "/" + startOfWeek.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) + ".dat";
			
			ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filename));
			oos.writeObject(week);
		}
	}
	
}


