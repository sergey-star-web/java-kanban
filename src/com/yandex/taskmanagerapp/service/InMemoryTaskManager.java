package com.yandex.taskmanagerapp.service;

import com.yandex.taskmanagerapp.enums.Status;
import com.yandex.taskmanagerapp.enums.TypeTask;
import com.yandex.taskmanagerapp.model.Task;
import com.yandex.taskmanagerapp.model.Subtask;
import com.yandex.taskmanagerapp.model.Epic;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InMemoryTaskManager implements TaskManager {
    private Integer counterId = 0;
    protected final HashMap<Integer, Task> tasks = new HashMap<>();
    private final HistoryManager historyManager = Managers.getDefaultHistory();

    public InMemoryTaskManager() {
    }

    @Override
    public List<Task> getAllTasks() {
        List<Task> tasksList = new ArrayList<>();
        tasksList.addAll(tasks.values());
        return tasksList;
    }

    @Override
    public ArrayList<Task> getTasks() {
        ArrayList<Task> tasksList = new ArrayList<>();

        for (Task task : tasks.values()) {
            if (!(task instanceof Subtask) && !(task instanceof Epic)) {
                tasksList.add(task);
                historyManager.add(task);
            }
        }
        return tasksList;
    }

    @Override
    public ArrayList<Subtask> getSubtasks() {
        ArrayList<Subtask> subTasksList = new ArrayList<>();

        for (Task task : tasks.values()) {
            if (task instanceof Subtask) {
                subTasksList.add((Subtask) task);
                historyManager.add(task);
            }
        }
        return subTasksList;
    }

    @Override
    public ArrayList<Epic> getEpics() {
        ArrayList<Epic> epicList = new ArrayList<>();

        for (Task task : tasks.values()) {
            if (task instanceof Epic) {
                epicList.add((Epic) task);
                historyManager.add(task);
            }
        }
        return epicList;
    }

    @Override
    public Task getTask(Integer id) {
        for (Task task : tasks.values()) {
            if (task.getId() == id) {
                historyManager.add(task);
                return task;
            }
        }
        return null;
    }

    @Override
    public ArrayList<Subtask> getSubtasksInEpic(Integer id) {
        if (!(tasks.get(id) instanceof Epic epic)) return null;

        ArrayList<Subtask> subtasks = new ArrayList<>();

        for (Subtask subtask : epic.getSubtasks()) {
            Task task = tasks.get(subtask.getId());
            subtasks.add((Subtask) task);
            historyManager.add(task);
        }

        return subtasks;
    }

    @Override
    public void deleteTasks() {
        tasks.clear();
    }

    @Override
    public void deleteTask(Integer id) {
        Epic epic = null;

        for (Task task : tasks.values()) {
            if (task.getId() == id && task instanceof Epic) {
                epic = (Epic) task;
                for (Subtask subtask : epic.getSubtasks()) {
                    tasks.remove(subtask.getId());
                }
            }
        }
        if (tasks.get(id) instanceof Subtask) {
            Subtask subtask = (Subtask) tasks.get(id);
            epic = (Epic) tasks.get(subtask.getIdEpic());
            epic.removeSubtask(subtask);
        }
        tasks.remove(id);
    }

    @Override
    public void createTask(Task task) {
        this.counterId++;
        task.setId(this.counterId);
        tasks.put(task.getId(), task);
    }

    @Override
    public void createSubtask(Subtask subtask) {
        Epic epic = (Epic) tasks.get(subtask.getIdEpic());

        this.counterId++;
        subtask.setId(this.counterId);
        tasks.put(subtask.getId(), subtask);
        if (epic != null) {
            ArrayList<Subtask> subtasksInEpic = epic.getSubtasks();
            subtasksInEpic.add(subtask);
        }
        updateStatusInEpic(subtask.getIdEpic());
    }

    @Override
    public void createEpic(Epic epic) {
        this.counterId++;
        epic.setId(this.counterId);
        tasks.put(epic.getId(), epic);
    }

    protected void updateStatusInEpic(Integer idEpic) {
        if (tasks.get(idEpic) != null && tasks.get(idEpic) instanceof Epic) {
            Epic epic = (Epic) tasks.get(idEpic);
            ArrayList<Subtask> subtasksInEpic = new ArrayList<>();
            boolean existsNew = false;
            boolean existsDone = false;
            boolean existsInProgress = false;

            if (epic != null) {
                if (epic.getSubtasks() != null) {
                    subtasksInEpic = epic.getSubtasks();
                }
                for (Subtask subtask : subtasksInEpic) {
                    if (subtask.getStatus() == Status.NEW) {
                        existsNew = true;
                    }
                    if (subtask.getStatus() == Status.DONE) {
                        existsDone = true;
                    }
                    if (subtask.getStatus() == Status.IN_PROGRESS) {
                        existsInProgress = true;
                    }
                }
                if (existsNew && !existsDone && !existsInProgress) {
                    if (epic.getStatus() != Status.NEW) {
                        epic.setStatus(Status.NEW);
                    }
                } else if (existsDone && !existsNew && !existsInProgress) {
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
    }

    @Override
    public void updateTask(Task task) {
        tasks.put(task.getId(), task);
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        Epic epic = (Epic) tasks.get(subtask.getIdEpic());

        tasks.put(subtask.getId(), subtask);
        if (epic != null) {
            ArrayList<Subtask> subtasksInEpic = epic.getSubtasks();
            subtasksInEpic.remove(subtask);
            subtasksInEpic.add(subtask);
        }
        updateStatusInEpic(subtask.getIdEpic());
    }

    @Override
    public void updateEpic(Epic epic) {
        tasks.put(epic.getId(), epic);
    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

    @Override
    public void updateSubtasksInEpic(Epic epic) {
        if (epic != null) {
            ArrayList<Subtask> subtasksInEpic = null;
            for (Task task : tasks.values()) {
                if (task.getType() == TypeTask.SUBTASK) {
                    if (((Subtask) task).getIdEpic() == epic.getId()) {
                        subtasksInEpic.add((Subtask) task);
                    }
                }
            }
            if (subtasksInEpic != null && !subtasksInEpic.isEmpty()) {
                epic.setSubtasks(subtasksInEpic);
            }
        }
    }
}