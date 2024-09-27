package com.btl.taskmanagement.Models;

import java.io.Serial;
import java.io.Serializable;
import java.time.Duration;
import java.time.LocalTime;

public class Task implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    // Các thuộc tính của Task
    private final String taskName;
    private final LocalTime startTime;
    private final Duration focusTime;
    private final Duration breakTime;
    private final String taskCategory;
    private final int importanceLevel;
    private boolean completed;

    // Constructor để khởi tạo Task
    public Task(String taskName, LocalTime startTime, Duration focusTime, Duration breakTime, String taskCategory, int importanceLevel) {
        this.taskName = taskName;
        this.startTime = startTime;
        this.focusTime = focusTime;
        this.breakTime = breakTime;
        this.taskCategory = taskCategory;
        this.importanceLevel = importanceLevel;
        this.completed = false; // Mặc định task chưa hoàn thành
    }

    // Getter cho taskName
    public String getTaskName() {
        return taskName;
    }

    // Getter cho startTime
    public LocalTime getStartTime() {
        return startTime;
    }

    // Getter cho focusTime
    public Duration getFocusTime() {
        return focusTime;
    }

    // Getter cho breakTime
    public Duration getBreakTime() {
        return breakTime;
    }

    // Getter cho taskCategory
    public String getTaskCategory() {
        return taskCategory;
    }

    // Getter cho importanceLevel
    public int getImportanceLevel() {
        return importanceLevel;
    }

    // Getter và Setter cho completed
    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    // Hiển thị thông tin của Task
    @Override
    public String toString() {
        return "Task{" +
                "taskName='" + taskName + '\'' +
                ", startTime=" + startTime +
                ", focusTime=" + focusTime +
                ", breakTime=" + breakTime +
                ", taskCategory='" + taskCategory + '\'' +
                ", importanceLevel=" + importanceLevel +
                ", completed=" + completed +
                '}';
    }
}
