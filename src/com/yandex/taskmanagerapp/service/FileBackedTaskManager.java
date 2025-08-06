package com.yandex.taskmanagerapp.service;

import com.yandex.taskmanagerapp.enums.Status;
import com.yandex.taskmanagerapp.enums.TypeTask;
import com.yandex.taskmanagerapp.model.Epic;
import com.yandex.taskmanagerapp.model.Subtask;
import com.yandex.taskmanagerapp.model.Task;

import java.util.ArrayList;

public class FileBackedTaskManager extends InMemoryTaskManager {
    private String file;

    public FileBackedTaskManager(String file) {
        this.file = file;
    }

    public String toStringTask(Task task) {
        if (task instanceof Subtask) {
            return TypeTask.SUBTASK + "," + task.getName() + "," + task.getStatus() + "," + "Description " + task.getDescription();
        } else if (task instanceof Epic) {
            return TypeTask.EPIC + "," + task.getName() + "," + task.getStatus() + "," + "Description " + task.getDescription();
        } else if (task instanceof Task) {
            return TypeTask.TASK + "," + task.getName() + "," + task.getStatus() + "," + "Description " + task.getDescription();
        }
        return null;
    }

    public Task fromString(String value) {
        String[] taskElems = value.split(",");
        if (taskElems.length > 0) {
            TypeTask typeTask = TypeTask.valueOf(taskElems[0]);
            String name = taskElems[1];
            Status status = Status.valueOf(taskElems[2]);
            String description = taskElems[3].replaceFirst("Description ", "");

            if (typeTask == TypeTask.SUBTASK) {
                return new Subtask(name, description, status);
            } else if (typeTask == TypeTask.EPIC) {
                Epic epic = new Epic(name, description);
                epic.setStatus(status);
                return epic;
            } else if (typeTask == TypeTask.TASK) {
                return new Task(name, description, status);
            }
        }
        return null;
    }

    private void save() {
        int val = 0;
    }

    @Override
    public void deleteTasks() {
        super.deleteTasks();
        save();
    }

    @Override
    public void deleteSubtasks() {
        super.deleteSubtasks();
        save();
    }

    @Override
    public void deleteEpics() {
        super.deleteEpics();
        save();
    }

    @Override
    public void deleteTask(Integer id) {
        super.deleteTask(id);
        save();
    }

    @Override
    public void deleteSubtask(Integer id) {
        super.deleteSubtask(id);
        save();
    }

    @Override
    public void deleteEpic(Integer id) {
        super.deleteEpic(id);
        save();
    }

    @Override
    public void createTask(Task task) {
        super.createTask(task);
        save();
    }

    @Override
    public void createSubtask(Subtask subtask) {
        super.createSubtask(subtask);
        save();
    }

    @Override
    public void createEpic(Epic epic) {
        super.createEpic(epic);
        save();
    }

    @Override
    protected void updateStatusInEpic(Integer idEpic) {
        super.updateStatusInEpic(idEpic);
        save();
    }

    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
        save();
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        super.updateSubtask(subtask);
        save();
    }

    @Override
    public void updateEpic(Epic epic) {
        super.updateEpic(epic);
        save();
    }

}
