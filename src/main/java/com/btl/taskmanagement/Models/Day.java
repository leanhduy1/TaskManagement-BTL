/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Duration;
import java.util.Comparator;
/**
 *
 * @author LUU DUNG
 */
public class Day {
    private final ObjectProperty<LocalDate> date;
    private final ObservableList<Task> taskObservableList;

    public Day(LocalDate date) {
        this.date = new SimpleObjectProperty<>(date);
        this.taskObservableList = FXCollections.observableArrayList();
    }

    public ObjectProperty<LocalDate> dateProperty() {
        return date;
    }

    public LocalDate getDate() {
        return date.get();
    }

    public void setDate(LocalDate date) {
        this.date.set(date);
    }

    // Ngăn trùng thời gian và trả về boolean
    public boolean addTask(Task task) {
        if (!isTimeConflict(task)) {
            taskObservableList.add(task);
            sortTasksByTime(); 
            return true;
        }
        return false; // Trả về false nếu trùng thời gian
    }

    // Phương thức kiểm tra nếu task mới bị trùng thời gian với task đã có
    private boolean isTimeConflict(Task newTask) {
        LocalTime newStart = newTask.getStartTime();
        LocalTime newEnd = newStart.plus(newTask.getFocusTime());

        return taskObservableList.stream()
                .anyMatch(existingTask -> {
                    LocalTime existingStart = existingTask.getStartTime();
                    LocalTime existingEnd = existingStart.plus(existingTask.getFocusTime());
                    return (newStart.isBefore(existingEnd) && newEnd.isAfter(existingStart));
                });
    }

    public void removeTask(Task task) {
        taskObservableList.remove(task);
    }

    private void sortTasksByTime() {
        taskObservableList.sort(Comparator.comparing(Task::getStartTime));
    }

    public ObservableList<Task> getTaskObservableList() {
        return taskObservableList;
    }
}
