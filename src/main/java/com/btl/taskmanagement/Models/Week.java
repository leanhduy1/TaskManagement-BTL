package com.btl.taskmanagement.Models;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Week implements Serializable {
	@Serial
	private static final long serialVersionUID = 1L;
	private final List<Day> dayList;
	private final LocalDate startDate;
	
	public Week(LocalDate startDate) {
		this.startDate = startDate;
		this.dayList = new ArrayList<>();
		for (int i = 0; i < 7; i++) {
			dayList.add(new Day(startDate.plusDays(i)));
		}
	}
}
