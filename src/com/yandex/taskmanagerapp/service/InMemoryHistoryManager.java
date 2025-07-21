package com.yandex.taskmanagerapp.service;

import com.yandex.taskmanagerapp.model.LinkedHashMapAnalogue;
import com.yandex.taskmanagerapp.model.Node;
import com.yandex.taskmanagerapp.model.Task;


import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

public class InMemoryHistoryManager implements HistoryManager {
    public static final int MAX_VALUE = 10;
    private LinkedHashMapAnalogue<Task> historyTasks = new LinkedHashMapAnalogue<>();
    private HashMap<Integer, Node> placeInspectionTask = new HashMap<>();

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
    public void remove(int id) {
        Integer index = 0;
        for (Task task : historyTasks) {
            index++;
            if (task.getId() == id) {
                historyTasks.remove(index);
            }
        }
    }

    @Override
    public List<Task> getHistory() {
        ArrayList<Task> newHistoryTasks = new ArrayList<>();
        for (int i = 0; i < historyTasks.size(); i++) {
            newHistoryTasks.add(historyTasks.getValue(i));
        }
        return newHistoryTasks;
    }

    public void linkLast(Task task) {
        historyTasks.addLast(task);
    }

    public ArrayList<Task> getTasks() {
        ArrayList<Task> newHistoryTasks = new ArrayList<>();
        for (int i = 0; i < historyTasks.size(); i++) {
            newHistoryTasks.add(historyTasks.getValue(i));
        }
        return newHistoryTasks;
    }

    public void removeNode(Node node) {
        placeInspectionTask.values().remove(node);
    }
}