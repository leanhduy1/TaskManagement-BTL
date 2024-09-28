package com.btl.taskmanagement.Controllers;

import com.btl.taskmanagement.Models.Calendar;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;

import java.io.IOException;
import java.time.LocalDate;

public class CalendarWindowController {

    @FXML
    private Button nextButton;

    @FXML
    private Button previousButton;

    @FXML
    private DatePicker datePicker;

    private Calendar calendar;

    public void initialize() {
        try {
            calendar = new Calendar();
            updateDatePicker();

            nextButton.setOnAction(event -> {
                try {
                    calendar.setToNextWeek();
                    updateDatePicker();
                } catch (IOException | ClassNotFoundException e) {
                    e.printStackTrace();
                }
            });

            previousButton.setOnAction(event -> {
                try {
                    calendar.setToPreviousWeek();
                    updateDatePicker();
                } catch (IOException | ClassNotFoundException e) {
                    e.printStackTrace();
                }
            });
            
            datePicker.setOnAction(event -> {
                LocalDate selectedDate = datePicker.getValue();
                if (selectedDate != null) {
                    try {
                        calendar.setToWeekOfDate(selectedDate); // New method to set week based on selected date
                        updateDatePicker();
                    } catch (IOException | ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            });

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void updateDatePicker() {
    	if (calendar.isInitial()) {
            datePicker.setValue(LocalDate.now());
        } else {
            datePicker.setValue(calendar.getCurrentDate());
        }
    }
}
