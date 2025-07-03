package com.yandex.taskmanagerapp.service;

import com.yandex.taskmanagerapp.model.Task;

import java.util.LinkedList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {
    public static final int MAX_VALUE = 10;
    LinkedList<Task> historyTasks = new LinkedList<>();

    @Override
    public void add(Task task) {
        if (historyTasks.size() < MAX_VALUE) {
            historyTasks.addFirst(task);
        } else {
            historyTasks.removeLast();
            historyTasks.addFirst(task);
        }
    }

    @Override
    public List<Task> getHistory() {
        return List.copyOf(historyTasks);
    }
}