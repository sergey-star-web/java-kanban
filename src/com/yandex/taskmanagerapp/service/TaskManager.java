package com.yandex.taskmanagerapp.service;

import com.yandex.taskmanagerapp.model.Task;
import com.yandex.taskmanagerapp.model.Subtask;
import com.yandex.taskmanagerapp.model.Epic;
import java.util.ArrayList;
import java.util.HashMap;

public class TaskManager {
    private Integer counterId = 0;
    private HashMap<Integer, Task> tasks = new HashMap<>();
    private HashMap<Integer, Subtask> subTasks = new HashMap<>();
    private HashMap<Integer, Epic> epics = new HashMap<>();

    public TaskManager() {
    }

    public ArrayList<Task> getTasks() {
        ArrayList<Task> tasksList = new ArrayList<>();

        if(!tasks.isEmpty()) {
            tasksList.addAll(tasks.values());
        }
        return (ArrayList<Task>) tasksList.clone();
    }

    public ArrayList<Subtask> getSubtasks() {
        ArrayList<Subtask> subTasksList = new ArrayList<>();

        if(!subTasks.isEmpty()) {
            subTasksList.addAll(subTasks.values());
        }
        return (ArrayList<Subtask>) subTasksList.clone();
    }

    public ArrayList<Epic> getEpics() {
        ArrayList<Epic> epicList = new ArrayList<>();

        if(!epics.isEmpty()) {
            epicList.addAll(epics.values());
        }
        return (ArrayList<Epic>) epicList.clone();
    }

    public Task getTask(Integer Id) {
        Task task = tasks.get(Id);

        return task;
    }

    public Subtask getSubtask(Integer Id) {
        Subtask subTask = subTasks.get(Id);

        return subTask;
    }

    public Epic getEpic(Integer Id) {
        Epic epic = epics.get(Id);

        return epic;
    }

    public ArrayList<Subtask> getSubtasksInEpic(Integer id) {
        Epic epic = epics.get(id);
        ArrayList<Subtask> subtasksInEpic = new ArrayList<>();

        if (epic != null) {
            if (epic.getSubtasks() != null) {
                subtasksInEpic = epic.getSubtasks();
            }
        }
        return (ArrayList<Subtask>) subtasksInEpic.clone();
    }

    public void deleteTasks() {
        if(!tasks.isEmpty()) {
            tasks.clear();
        }
    }

    public void deleteSubtasks() {
        if(!subTasks.isEmpty()) {
            for (Subtask subTask : subTasks.values()) {
                Epic epic = epics.get(subTask.getIdEpic());
                epic.setSubtasks(new ArrayList<>());
                updateStatusInEpic(subTask.getIdEpic());
            }
            subTasks.clear();
        }
    }

    public void deleteEpics() {
        if(!epics.isEmpty()) {
            subTasks.clear();
            epics.clear();
        }
    }

    public void deleteTask(Integer id) {
        tasks.remove(id);
    }

    public void deleteSubtask(Integer id) {
        Subtask subTask = subTasks.get(id);
        Epic epic = epics.get(subTask.getIdEpic());
        ArrayList<Subtask> subtasksInEpic = epic.getSubtasks();

        if(subTask != null) {
            subtasksInEpic.remove(subTask);
            epic.setSubtasks(subtasksInEpic);
            subTasks.remove(id);
            updateStatusInEpic(subTask.getIdEpic());
        }
    }

    public void deleteEpic(Integer id) {
        Epic epic = epics.get(id);

        if(epic != null) {
            for (Integer idSub : subTasks.keySet()) {
                if (subTasks.get(idSub).getIdEpic().equals(id)) {
                    subTasks.remove(idSub);
                }
            }
            epics.remove(id);
        }
    }

    public void createTask(Task task) {
        this.counterId++;
        task.setId(this.counterId);
        tasks.put(task.getId(), task);
    }

    public void createSubtask(Subtask subTask, Integer idEpic) {
        Epic epic = epics.get(idEpic);
        ArrayList<Subtask> subtasksInEpic = epic.getSubtasks();

        this.counterId++;
        subTask.setId(this.counterId);
        subTask.setIdEpic(idEpic);
        subTasks.put(subTask.getId(), subTask);
        subtasksInEpic.add(subTask);
        epic.setSubtasks(subtasksInEpic);
        updateStatusInEpic(subTask.getIdEpic());
    }

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

    public void updateTask(Task task) {
        tasks.put(task.getId(), task);
    }

    public void updateSubtask(Subtask subTask) {
        Epic epic = epics.get(subTask.getIdEpic());
        ArrayList<Subtask> subtasksInEpic = epic.getSubtasks();

        subTasks.put(subTask.getId(), subTask);
        subtasksInEpic.remove(subTask);
        subtasksInEpic.add(subTask);
        epic.setSubtasks(subtasksInEpic);
        updateStatusInEpic(subTask.getIdEpic());
    }

    public void updateEpic(Epic epic) {
        epics.put(epic.getId(), epic);
    }

}

