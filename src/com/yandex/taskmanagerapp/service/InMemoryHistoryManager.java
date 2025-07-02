package com.yandex.taskmanagerapp.service;

import com.yandex.taskmanagerapp.model.Task;

import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {

    @Override
    public void add(Task task) {
        if (historyTasks.size() < 10) {
            historyTasks.add(task);
        } else {
            historyTasks.removeFirst();
            historyTasks.add(task);
        }
    }

    @Override
    public List<Task> getHistory() {
        return List.copyOf(historyTasks);
    }
}