package com.yandex.taskmanagerapp.model;

import java.util.ArrayList;
import com.yandex.taskmanagerapp.enums.Status;

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
    public String toString() {
        return getId() +
                "," + getType() +
                "," + getName() +
                "," + getStatus() +
                "," + getDescription();
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

}