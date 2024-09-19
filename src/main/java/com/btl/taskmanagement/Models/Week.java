package com.btl.taskmanagement.Models;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Week {
	private List<Day> dayList;
	private LocalDate startDate;
	
	public ArrayList<Day> getDayList() {
		return (ArrayList<Day>) dayList;
	}
	
	public Week(LocalDate startDate) {
		this.startDate = startDate;
		this.dayList = new ArrayList<>();
		for (int i = 0; i < 7; i++) {
			dayList.add(new Day(startDate.plusDays(i)));
		}
	}
	
	
	
	
	
}
