package com.btl.taskmanagement.Models;

import java.io.*;
import java.time.LocalDate;
import java.util.Map;
import java.util.TreeMap;

public class Calendar {
	private Map<LocalDate, Week> weeks = new TreeMap<>();
	private Week currentWeek;
	private LocalDate currentTime;
	
	public Calendar() {
		weeks = new TreeMap<>();
	}
	
	private void XacDinhDauTuan(){
	
	}
	
}
