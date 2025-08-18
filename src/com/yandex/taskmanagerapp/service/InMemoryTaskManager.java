package com.yandex.taskmanagerapp.service;

import com.yandex.taskmanagerapp.enums.Status;
import com.yandex.taskmanagerapp.enums.TypeTask;
import com.yandex.taskmanagerapp.model.Task;
import com.yandex.taskmanagerapp.model.Subtask;
import com.yandex.taskmanagerapp.model.Epic;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class InMemoryTaskManager implements TaskManager {
    private Integer counterId = 0;
    protected final HashMap<Integer, Task> tasks = new HashMap<>();
    private final HistoryManager historyManager = Managers.getDefaultHistory();
    private final Comparator startDateCompare = Comparator.comparing(Task::getStartTime);

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
        ArrayList<Task> tasksList = (ArrayList<Task>) tasks.values().stream()
                .filter(t -> !(t instanceof Subtask) && !(t instanceof Epic))
                .map(t -> {
                    historyManager.add(t);
                    return t;
                })
                .collect(Collectors.toList());
        return tasksList;
    }

    @Override
    public ArrayList<Subtask> getSubtasks() {
        ArrayList<Subtask> subTasksList = (ArrayList<Subtask>) tasks.values().stream()
                .filter(t -> t instanceof Subtask)
                .map(t -> {
                    historyManager.add(t);
                    return (Subtask) t;
                })
                .collect(Collectors.toList());
        return subTasksList;
    }

    @Override
    public ArrayList<Epic> getEpics() {
        ArrayList<Epic> epicList = (ArrayList<Epic>) tasks.values().stream()
                .filter(t -> t instanceof Epic)
                .map(t -> {
                    historyManager.add(t);
                    return (Epic) t;
                })
                .collect(Collectors.toList());
        return epicList;
    }

    @Override
    public Task getTask(Integer id) {
        Optional<Task> task = tasks.values()
                .stream()
                .filter(t -> t.getId() == id)
                .map(t -> {
                        historyManager.add(t);
                        return t;
                })
                .findFirst();
        if (task.isPresent()) {
            return task.get();
        } else {
            return null;
        }
    }

    @Override
    public ArrayList<Subtask> getSubtasksInEpic(Integer id) {
        if (!(tasks.get(id) instanceof Epic epic)) return null;
        ArrayList<Subtask> subtasks = (ArrayList<Subtask>) epic.getSubtasks()
                .stream()
                .map(s -> {
                    Task task = tasks.get(s.getId());
                    historyManager.add(task);
                    return (Subtask) task;
                })
                .collect(Collectors.toList());
        return subtasks;
    }

    @Override
    public void deleteTasks() {
        tasks.clear();
    }

    @Override
    public void deleteTask(Integer id) {
        Epic epic = (Epic) tasks.values().stream()
                .filter(t -> t.getId() == id && t instanceof Epic)
                .findFirst()
                .orElse(null);
        if (epic != null) {
            epic.getSubtasks()
                    .stream()
                    .map(t -> tasks.remove(t.getId()));
        }
        if (tasks.get(id) instanceof Subtask) {
            Subtask subtask = (Subtask) tasks.get(id);
            epic = (Epic) tasks.get(subtask.getIdEpic());
            epic.removeSubtask(subtask);
            updateDataInEpic(epic.getId());
        }
        tasks.remove(id);
    }

    private boolean checkIdForCorrect(Task task) {
        if (task.getId() == null || task.getId() < 1 || tasks.containsKey(task.getId())) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void addTask(Task task) {
        if (checkIdForCorrect(task)) {
            this.counterId++;
            task.setId(this.counterId);
        } else {
            this.counterId = task.getId() + 1;
        }
        if (!problemIntersectionSearch(task)) {
            tasks.put(task.getId(), task);
        } else {
            System.out.println("Таск: " + task + " Не добавлен, так как имеется пересечение.");
        }
    }

    @Override
    public void addSubtask(Subtask subtask) {
        Epic epic = null;
        if (tasks.get(subtask.getIdEpic()) != null) {
            Task task = tasks.get(subtask.getIdEpic());
            if (task.getType() == TypeTask.EPIC) {
                epic = (Epic) task;
            }
        }
        if (checkIdForCorrect(subtask)) {
            this.counterId++;
            subtask.setId(this.counterId);
        } else {
            this.counterId = subtask.getId() + 1;
        }
        if (!problemIntersectionSearch(subtask)) {
            tasks.put(subtask.getId(), subtask);
            if (epic != null) {
                ArrayList<Subtask> subtasksInEpic = epic.getSubtasks();
                subtasksInEpic.add(subtask);
            }
            updateDataInEpic(subtask.getIdEpic());
        } else {
            System.out.println("Сабтаск: " + subtask + " Не добавлен, так как имеется пересечение.");
        }
    }

    @Override
    public void addEpic(Epic epic) {
        if (checkIdForCorrect(epic)) {
            this.counterId++;
            epic.setId(this.counterId);
        } else {
            this.counterId = epic.getId() + 1;
        }
        tasks.put(epic.getId(), epic);
    }

    protected void updateDataInEpic(Integer idEpic) {
        if (tasks.get(idEpic) != null && tasks.get(idEpic).getType() == TypeTask.EPIC) {
            Epic epic = (Epic) tasks.get(idEpic);
            ArrayList<Subtask> subtasksInEpic = new ArrayList<>();
            boolean existsNew = false;
            boolean existsDone = false;
            boolean existsInProgress = false;

            if (epic != null) {
                LocalDateTime startTime = null;
                LocalDateTime endTime = null;
                Duration duration = null;
                if (epic.getSubtasks() != null) {
                    subtasksInEpic = epic.getSubtasks();
                    startTime = subtasksInEpic.stream()
                            .filter(t -> t.getStartTime() != null)
                            .min(Comparator.comparing(t -> t.getStartTime()))
                            .get()
                            .getStartTime();
                    endTime = subtasksInEpic.stream()
                            .filter(t -> t.getEndTime() != null)
                            .max(Comparator.comparing(t -> t.getEndTime()))
                            .get()
                            .getEndTime();
                    duration = Duration.ofMinutes(subtasksInEpic.stream()
                            .mapToInt(c -> c.getDurationMinutes())
                            .sum());
                }
                if (startTime != null) {
                    epic.setStartTime(startTime);
                }
                if (endTime != null) {
                    epic.setEndTime(endTime);
                }
                if (duration != null) {
                    epic.setDuration(duration);
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
        if (!problemIntersectionSearch(task)) {
            tasks.put(task.getId(), task);
        } else {
            System.out.println("Таск: " + task + " не обновлён, так как имеется пересечение.");
        }
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        Epic epic = (Epic) tasks.get(subtask.getIdEpic());

        if (!problemIntersectionSearch(subtask)) {
            tasks.put(subtask.getId(), subtask);
            if (epic != null) {
                ArrayList<Subtask> subtasksInEpic = epic.getSubtasks();
                subtasksInEpic.remove(subtask);
                subtasksInEpic.add(subtask);
            }
            updateDataInEpic(subtask.getIdEpic());
        } else {
            System.out.println("Сабтаск: " + subtask + " не обновлён, так как имеется пересечение.");
        }
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
            ArrayList<Subtask> subtasksInEpic = (ArrayList<Subtask>) tasks.values()
                    .stream()
                    .filter(t -> t.getType() == TypeTask.SUBTASK && ((Subtask) t).getIdEpic() == epic.getId())
                    .map(t -> (Subtask) t)
                    .collect(Collectors.toList());
            if (subtasksInEpic != null && !subtasksInEpic.isEmpty()) {
                epic.setSubtasks(subtasksInEpic);
            }
        }
    }

    public TreeSet<Task> getPrioritizedTasks() {
        TreeSet<Task> prioritizedTasks = new TreeSet<>(startDateCompare);
        prioritizedTasks.addAll(getAllTasks()
                .stream()
                .filter(t -> t.getStartTime() != null && t.getType() != TypeTask.EPIC)
                .toList());
        return prioritizedTasks;
    }

    public boolean problemIntersectionSearch(Task task) {
        if (task.getType() == TypeTask.EPIC || getPrioritizedTasks().isEmpty()) {
            return false;
        } else {
            LocalDateTime endTime;
            LocalDateTime startTime;
            if (task.getEndTime() != null && task.getStartTime() != null){
                endTime = task.getEndTime();
                startTime = task.getStartTime();
                Task existTask;
                TreeSet<Task> prioritizedTasks = getPrioritizedTasks();
                existTask = prioritizedTasks.stream()
                        .filter(t -> t.getStartTime() != null && t.getEndTime() != null
                                && (
                                        (startTime.isBefore(t.getEndTime()) || t.getEndTime().equals(startTime))
                                            &&
                                        (endTime.isAfter(t.getStartTime()) || t.getStartTime().equals(endTime))
                                )
                        )
                        .findFirst().orElse(null);
                if (existTask != null) {
                    return true;
                }
            }
            return false;
        }
    }
}