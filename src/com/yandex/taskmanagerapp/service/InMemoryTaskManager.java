package com.yandex.taskmanagerapp.service;

import com.yandex.taskmanagerapp.model.Task;
import com.yandex.taskmanagerapp.model.Subtask;
import com.yandex.taskmanagerapp.model.Epic;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InMemoryTaskManager implements TaskManager {
    private Integer counterId = 0;
    private HashMap<Integer, Task> tasks = new HashMap<>();
    private HashMap<Integer, Subtask> subTasks = new HashMap<>();
    private HashMap<Integer, Epic> epics = new HashMap<>();
    private final HistoryManager historyManager = Managers.getDefaultHistory();

    public InMemoryTaskManager() {
    }

    @Override
    public ArrayList<Task> getTasks() {
        ArrayList<Task> tasksList = new ArrayList<>();

        if(!tasks.isEmpty()) {
            tasksList.addAll(tasks.values());
        }
        return tasksList;
    }

    @Override
    public ArrayList<Subtask> getSubtasks() {
        ArrayList<Subtask> subTasksList = new ArrayList<>();

        if(!subTasks.isEmpty()) {
            subTasksList.addAll(subTasks.values());
        }
        return subTasksList;
    }

    @Override
    public ArrayList<Epic> getEpics() {
        ArrayList<Epic> epicList = new ArrayList<>();

        epicList.addAll(epics.values());
        return epicList;
    }

    @Override
    public Task getTask(Integer Id) {
        Task task = tasks.get(Id);
        if(task != null) {
            task = new Task(task);
            historyManager.add(task);
        }
        return task;
    }

    @Override
    public Subtask getSubtask(Integer Id) {
        Subtask subtask = subTasks.get(Id);

        if(subtask != null) {
            subtask = new Subtask(subtask);
            historyManager.add(subtask);
        }
        return subtask;
    }

    @Override
    public Epic getEpic(Integer Id) {
        Epic epic = epics.get(Id);

        if(epic != null) {
            epic = new Epic(epic);
            historyManager.add(epic);
        }
        return epic;
    }

    @Override
    public ArrayList<Subtask> getSubtasksInEpic(Integer id) {
        Epic epic = epics.get(id);
        ArrayList<Subtask> subtasksInEpic = new ArrayList<>();

        if (epic != null) {
            if (epic.getSubtasks() != null) {
                subtasksInEpic.addAll(epic.getSubtasks());
            }
        }
        return subtasksInEpic;
    }

    @Override
    public void deleteTasks() {
        tasks.clear();
    }

    @Override
    public void deleteSubtasks() {
        if(!subTasks.isEmpty()) {
            for (Subtask subtask : subTasks.values()) {
                Epic epic = epics.get(subtask.getIdEpic());
                ArrayList<Subtask> subtasksInEpic = epic.getSubtasks();
                subtasksInEpic.clear();
                updateStatusInEpic(subtask.getIdEpic());
            }
            subTasks.clear();
        }
    }

    @Override
    public void deleteEpics() {
        subTasks.clear();
        epics.clear();
    }

    @Override
    public void deleteTask(Integer id) {
        tasks.remove(id);
    }

    @Override
    public void deleteSubtask(Integer id) {
        Subtask subtask = subTasks.get(id);
        Epic epic = epics.get(subtask.getIdEpic());
        ArrayList<Subtask> subtasksInEpic = epic.getSubtasks();

        if(subtask != null) {
            subtasksInEpic.remove(subtask);
            subTasks.remove(id);
            updateStatusInEpic(subtask.getIdEpic());
        }
    }

    @Override
    public void deleteEpic(Integer id) {
        Epic epic = epics.get(id);

        if(epic != null) {
            for (Subtask subtask : epic.getSubtasks()) {
                subTasks.remove(subtask.getId());
            }
            epics.remove(id);
        }
    }

    @Override
    public void createTask(Task task) {
        this.counterId++;
        task.setId(this.counterId);
        tasks.put(task.getId(), task);
    }

    @Override
    public void createSubtask(Subtask subtask) {
        Epic epic = epics.get(subtask.getIdEpic());

        this.counterId++;
        subtask.setId(this.counterId);
        subTasks.put(subtask.getId(), subtask);
        if (epic != null) {
            ArrayList<Subtask> subtasksInEpic = epic.getSubtasks();
            subtasksInEpic.add(subtask);
        }
        updateStatusInEpic(subtask.getIdEpic());
    }

    @Override
    public void createEpic(Epic epic) { //нужно скорректировать создание эпика
        this.counterId++;
        epic.setId(this.counterId);
        epics.put(epic.getId(), epic);
    }

    private void updateStatusInEpic(Integer idEpic) {
        Epic epic = epics.get(idEpic);
        ArrayList<Subtask> subtasksInEpic = new ArrayList<>();
        boolean existsNew = false;
        boolean existsDone = false;
        boolean existsIN_PROGRESS = false;

        if (epic != null) {
            if (epic.getSubtasks() != null) {
                subtasksInEpic = epic.getSubtasks();
            }
            for (Subtask subtask : subtasksInEpic) {
                if (subtask.getStatus() == Status.NEW) {
                    existsNew = true;
                }
                if (subtask.getStatus() == Status.DONE) {
                    existsDone  = true;
                }
                if (subtask.getStatus() == Status.IN_PROGRESS) {
                    existsIN_PROGRESS = true;
                }
            }
            if (existsNew && !existsDone && !existsIN_PROGRESS) {
                if (epic.getStatus() != Status.NEW) {
                    epic.setStatus(Status.NEW);
                }
            } else if (existsDone && !existsNew && !existsIN_PROGRESS) {
                if (epic.getStatus() != Status.DONE) {
                    epic.setStatus(Status.DONE);
                }
            } else if (!subtasksInEpic.isEmpty()) {
                if (epic.getStatus() != Status.IN_PROGRESS) {
                    epic.setStatus(Status.IN_PROGRESS);
                }
            } else {
                if (epic.getStatus() != Status.NEW) {
                    epic.setStatus(Status.NEW);
                }
            }
        }
    }

    @Override
    public void updateTask(Task task) {
        tasks.put(task.getId(), task);
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        Epic epic = epics.get(subtask.getIdEpic());

        subTasks.put(subtask.getId(), subtask);
        if (epic != null) {
            ArrayList<Subtask> subtasksInEpic = epic.getSubtasks();
            subtasksInEpic.remove(subtask);
            subtasksInEpic.add(subtask);
        }
        updateStatusInEpic(subtask.getIdEpic());
    }

    @Override
    public void updateEpic(Epic epic) {
        epics.put(epic.getId(), epic);
    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }
}