package com.yandex.taskmanagerapp.model;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;

import com.yandex.taskmanagerapp.constants.Constant;
import com.yandex.taskmanagerapp.enums.Status;
import com.yandex.taskmanagerapp.enums.TypeTask;

public class Epic extends Task {
    private ArrayList<Subtask> subtasks = new ArrayList<>();

    public Epic(String name, String description) {
        super(name, description, Status.NEW);
    }

    public Epic(Integer id, String name, String description, Status status) {
        super(id, name, description, status);
    }

    public Epic() {
    }

    public Epic(Epic originalEpic) {
        this(originalEpic.getName(), originalEpic.getDescription());
        this.setId(originalEpic.getId());
        this.subtasks = originalEpic.getSubtasks();
    }

    @Override
    public TypeTask getType() {
        return TypeTask.EPIC;
    }

    @Override
    public String toString() {
        return getId() +
                "," + getType() +
                "," + getName() +
                "," + getStatus() +
                "," + getDescription() +
                "," + getDurationMinutes() +
                "," + getStartTime() +
                "," + getEndTime();
    }

    public ArrayList<Subtask> getSubtasks() {
        return subtasks;
    }

    public void setSubtasks(ArrayList<Subtask> subtasks) {
        this.subtasks = subtasks;
    }

    public void removeSubtask(Subtask subtask) {
        this.subtasks.remove(subtask);
    }

    public void addSubtask(Subtask subtask) {
        this.subtasks.add(subtask);
    }

    @Override
    public void setEndTime(LocalDateTime endTime) {
        super.endTime = endTime;
    }

    @Override
    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    @Override
    public void setDurationInt(Integer duration) {
        this.duration = Duration.ofMinutes(duration);
    }

    @Override
    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    @Override
    public void setStartTimeString(String startTime) {
        if (startTime != null) {
            this.startTime = LocalDateTime.parse(startTime, Constant.dateFormat);
        } else {
            this.startTime = null;
        }
    }
}