package com.yandex.taskmanagerapp.service;

import com.yandex.taskmanagerapp.model.Epic;
import com.yandex.taskmanagerapp.model.Subtask;
import com.yandex.taskmanagerapp.model.Task;

import java.util.ArrayList;
import java.util.List;

public interface TaskManager {
    ArrayList<Task> getTasks();

    ArrayList<Subtask> getSubtasks();

    ArrayList<Epic> getEpics();

    Task getTask(Integer id);

    ArrayList<Subtask> getSubtasksInEpic(Integer id);

    List<Task> getHistory();

    void deleteTasks();

    void deleteTask(Integer id);

    void addTask(Task task);

    void addSubtask(Subtask subTask);

    void addEpic(Epic epic);

    void updateTask(Task task);

    void updateSubtask(Subtask subTask);

    void updateEpic(Epic epic);

    List<Task> getAllTasks();

    void updateSubtasksInEpic(Epic epic);
}
