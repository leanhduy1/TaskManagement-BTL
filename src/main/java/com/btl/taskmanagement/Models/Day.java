package com.btl.taskmanagement.Models;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Day implements Serializable {
	@Serial
	private static final long serialVersionUID = 1L;
	private final LocalDate date;
	private final LinkedList<Task> taskLinkedList = new LinkedList<>();
	private transient ObservableList<Task> tasks;
	
	public Day(LocalDate date) {
		this.date = date;
	}
	
	
}
