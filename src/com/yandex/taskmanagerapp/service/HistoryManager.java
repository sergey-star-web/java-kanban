package com.yandex.taskmanagerapp.service;

import com.yandex.taskmanagerapp.model.Task;

import java.util.LinkedList;
import java.util.List;

public interface HistoryManager {
    void add(Task task);
    void remove(int id);
    List<Task> getHistory();
}