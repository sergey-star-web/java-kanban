package com.yandex.taskmanagerapp.service;

import com.yandex.taskmanagerapp.model.Task;

import java.util.ArrayList;
import java.util.List;

public interface HistoryManager {
    List<Task> historyTasks = new ArrayList<>();

    List<Task> getHistory();
    void add(Task task);
}
