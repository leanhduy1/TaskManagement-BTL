package com.btl.taskmanagement.Controllers;

import com.btl.taskmanagement.Models.Calendar;
import com.btl.taskmanagement.Models.Day;
import com.btl.taskmanagement.Models.Task;
import com.btl.taskmanagement.ViewController;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;

public class CalendarWindowController implements Initializable {
    @FXML
    private AnchorPane rootPane;
    @FXML
    private ListView<Task> mondayListView, tuesdayListView, wednesdayListView, thursdayListView, fridayListView, saturdayListView, sundayListView;
    @FXML
    private Label mondayLabel, tuesdayLabel, wednesdayLabel, thursdayLabel, fridayLabel, saturdayLabel, sundayLabel;
    @FXML
    private Button nextButton, previousButton;
    @FXML
    private DatePicker datePicker;
    
    private Calendar calendar;
    private boolean isUpdatingDatePicker = false;
    
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        calendar = ViewController.calendar;
        datePicker.setValue(LocalDate.now());
        setupListViewCellFactories();
        updateListViews();
        setupListViewWidths();
        rootPane.widthProperty().addListener((_, _, _) -> setupListViewWidths());
    }
    
    private void setupListViewCellFactories() {
        List<ListView<Task>> listViews = List.of(mondayListView, tuesdayListView, wednesdayListView, thursdayListView, fridayListView, saturdayListView, sundayListView);
        for (ListView<Task> listView : listViews) {
            listView.setCellFactory(_ -> new TaskCellCalendarWindow());
        }
    }
    
    public void updateListViews() {
        List<Day> dayList = calendar.getCurrentWeek().getDayList();
        mondayListView.setItems(dayList.get(0).getTaskObservableList());
        tuesdayListView.setItems(dayList.get(1).getTaskObservableList());
        wednesdayListView.setItems(dayList.get(2).getTaskObservableList());
        thursdayListView.setItems(dayList.get(3).getTaskObservableList());
        fridayListView.setItems(dayList.get(4).getTaskObservableList());
        saturdayListView.setItems(dayList.get(5).getTaskObservableList());
        sundayListView.setItems(dayList.get(6).getTaskObservableList());
    }
    
    private void setupListViewWidths() {
        double width = rootPane.getWidth() / 7;
        double startX = 0;
        
        List<Label> labels = List.of(mondayLabel, tuesdayLabel, wednesdayLabel, thursdayLabel, fridayLabel, saturdayLabel, sundayLabel);
        List<ListView<Task>> listViews = List.of(mondayListView, tuesdayListView, wednesdayListView, thursdayListView, fridayListView, saturdayListView, sundayListView);
        
        for (int i = 0; i < 7; i++) {
            double x = startX + (i * width);
            
            Label label = labels.get(i);
            label.setPrefWidth(width);
            label.setLayoutX(x);
            label.setLayoutY(80);
            
            ListView<Task> listView = listViews.get(i);
            listView.setPrefWidth(width);
            listView.setLayoutX(x);
            listView.setLayoutY(110);
        }
    }
    
    
    
    @FXML
    public void mondayClicked() throws IOException {
        ViewController.selectedDay = calendar.getCurrentWeek().getDayList().getFirst();
        ViewController.switchToDayWindow();
        mondayListView.getSelectionModel().clearSelection();
    }
    @FXML
    public void tuesdayClicked() throws IOException {
        ViewController.selectedDay = calendar.getCurrentWeek().getDayList().get(1);
        ViewController.switchToDayWindow();
        tuesdayListView.getSelectionModel().clearSelection();
    }
    @FXML
    public void wednesdayClicked() throws IOException {
        ViewController.selectedDay = calendar.getCurrentWeek().getDayList().get(2);
        ViewController.switchToDayWindow();
        wednesdayListView.getSelectionModel().clearSelection();
    }
    @FXML
    public void thursdayClicked() throws IOException {
        ViewController.selectedDay = calendar.getCurrentWeek().getDayList().get(3);
        ViewController.switchToDayWindow();
        thursdayListView.getSelectionModel().clearSelection();
    }
    @FXML
    public void fridayClicked() throws IOException {
        ViewController.selectedDay = calendar.getCurrentWeek().getDayList().get(4);
        ViewController.switchToDayWindow();
        fridayListView.getSelectionModel().clearSelection();
    }
    @FXML
    public void saturdayClicked() throws IOException {
        ViewController.selectedDay = calendar.getCurrentWeek().getDayList().get(5);
        ViewController.switchToDayWindow();
        saturdayListView.getSelectionModel().clearSelection();
    }
    @FXML
    public void sundayClicked() throws IOException {
        ViewController.selectedDay = calendar.getCurrentWeek().getDayList().get(6);
        ViewController.switchToDayWindow();
        sundayListView.getSelectionModel().clearSelection();
    }
    
    @FXML
    private void handleChangeDate() {
        if (!isUpdatingDatePicker) {
            LocalDate date = datePicker.getValue();
            if (date != null) {
                calendar.setToAnotherWeek(date);
                updateListViews();
            }
        }
    }
    
    @FXML
    private void handleNextButtonAction() {
        calendar.setToNextWeek();
        updateDatePicker();
        updateListViews();
    }
    
    @FXML
    private void handlePreviousButtonAction() {
        calendar.setToPreviousWeek();
        updateDatePicker();
        updateListViews();
    }
    
    private void updateDatePicker() {
        isUpdatingDatePicker = true;
        datePicker.setValue(calendar.getStartOfCurrentWeek());
        isUpdatingDatePicker = false;
    }
}
