package com.yandex.taskmanagerapp.service;

public class Managers {
    private Managers() {
    }

    public static FileBackedTaskManager getDefault() {
        return new FileBackedTaskManager();
    }

    public static InMemoryTaskManager getTaskManager() {
        return new InMemoryTaskManager();
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }
}
