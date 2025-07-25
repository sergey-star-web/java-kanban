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

    Subtask getSubtask(Integer id);

    Epic getEpic(Integer id);

    ArrayList<Subtask> getSubtasksInEpic(Integer id);

    List<Task> getHistory();

    void deleteTasks();

    void deleteSubtasks();

    void deleteEpics();

    void deleteTask(Integer id);

    void deleteSubtask(Integer id);

    void deleteEpic(Integer id);

    void createTask(Task task);

    void createSubtask(Subtask subTask);

    void createEpic(Epic epic);

    void updateTask(Task task);

    void updateSubtask(Subtask subTask);

    void updateEpic(Epic epic);
}
