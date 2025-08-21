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
    private final TreeSet<Task> prioritizedTasks = new TreeSet<>(startDateCompare);

    public InMemoryTaskManager() {
    }

    @Override
    public List<Task> getAllTasks() {
        List<Task> tasksList = new ArrayList<>();
        tasksList.addAll(this.tasks.values());
        return tasksList;
    }

    @Override
    public ArrayList<Task> getTasks() {
        ArrayList<Task> tasksList = (ArrayList<Task>) this.tasks.values().stream()
                .filter(t -> t.getType() == TypeTask.TASK)
                .map(t -> {
                    this.historyManager.add(t);
                    return t;
                })
                .collect(Collectors.toList());
        return tasksList;
    }

    @Override
    public ArrayList<Subtask> getSubtasks() {
        ArrayList<Subtask> subTasksList = (ArrayList<Subtask>) this.tasks.values().stream()
                .filter(t -> t.getType() == TypeTask.SUBTASK)
                .map(t -> {
                    this.historyManager.add(t);
                    return (Subtask) t;
                })
                .collect(Collectors.toList());
        return subTasksList;
    }

    @Override
    public ArrayList<Epic> getEpics() {
        ArrayList<Epic> epicList = (ArrayList<Epic>) this.tasks.values().stream()
                .filter(t -> t.getType() == TypeTask.EPIC)
                .map(t -> {
                    this.historyManager.add(t);
                    return (Epic) t;
                })
                .collect(Collectors.toList());
        return epicList;
    }

    @Override
    public Task getTask(Integer id) {
        Task task = this.tasks.get(id);
        if (task != null) {
            this.historyManager.add(task);
        }
        return task;
    }

    @Override
    public ArrayList<Subtask> getSubtasksInEpic(Integer id) {
        Epic epic;
        if (this.tasks.get(id).getType() == TypeTask.EPIC) {
            epic = (Epic) this.tasks.get(id);
        } else {
            return null;
        }
        ArrayList<Subtask> subtasks = (ArrayList<Subtask>) epic.getSubtasks()
                .stream()
                .map(s -> {
                    Task task = this.tasks.get(s.getId());
                    this.historyManager.add(task);
                    return (Subtask) task;
                })
                .collect(Collectors.toList());
        return subtasks;
    }

    @Override
    public void deleteTasks() {
        this.tasks.clear();
        this.prioritizedTasks.clear();
    }

    @Override
    public void deleteTask(Integer id) {
        Task task = this.tasks.get(id);
        Epic epic = null;
        if (task.getType() == TypeTask.EPIC) {
            epic = (Epic) this.tasks.get(id);
        }
        if (epic != null) {
            epic.getSubtasks()
                    .stream()
                    .map(t -> this.tasks.remove(t.getId()));
        }
        if (task.getType() == TypeTask.SUBTASK) {
            Subtask subtask = (Subtask) task;
            epic = (Epic) this.tasks.get(subtask.getIdEpic());
            epic.removeSubtask(subtask);
            updateDataInEpic(epic.getId());
        }
        if (task.getType() != TypeTask.EPIC) {
            this.prioritizedTasks.remove(task);
        }
        this.tasks.remove(id);
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
        if (!isIntersectionProblem(task)) {
            this.tasks.put(task.getId(), task);
            if (canInsertInPrioritizedTasks(task)) {
                this.prioritizedTasks.add(task);
            }
        } else {
            System.out.println("Таск: " + task + " Не добавлен, так как имеется пересечение.");
        }
    }

    @Override
    public void addSubtask(Subtask subtask) {
        Epic epic = null;
        if (this.tasks.get(subtask.getIdEpic()) != null) {
            Task task = this.tasks.get(subtask.getIdEpic());
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
        if (!isIntersectionProblem(subtask)) {
            this.tasks.put(subtask.getId(), subtask);
            if (epic != null) {
                ArrayList<Subtask> subtasksInEpic = epic.getSubtasks();
                subtasksInEpic.add(subtask);
            }
            updateDataInEpic(subtask.getIdEpic());
            if (canInsertInPrioritizedTasks(subtask)) {
                this.prioritizedTasks.add(subtask);
            }
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
        this.tasks.put(epic.getId(), epic);
    }

    protected void updateDataInEpic(Integer idEpic) {
        if (this.tasks.get(idEpic) != null && this.tasks.get(idEpic).getType() == TypeTask.EPIC) {
            Epic epic = (Epic) this.tasks.get(idEpic);
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
        if (!isIntersectionProblem(task)) {
            this.tasks.put(task.getId(), task);
            if (canInsertInPrioritizedTasks(task)) {
                this.prioritizedTasks.remove(task);
                this.prioritizedTasks.add(task);
            }
        } else {
            System.out.println("Таск: " + task + " не обновлён, так как имеется пересечение.");
        }
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        Epic epic = (Epic) this.tasks.get(subtask.getIdEpic());

        if (!isIntersectionProblem(subtask)) {
            this.tasks.put(subtask.getId(), subtask);
            if (canInsertInPrioritizedTasks(subtask)) {
                this.prioritizedTasks.remove(subtask);
                this.prioritizedTasks.add(subtask);
            }
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
        this.tasks.put(epic.getId(), epic);
    }

    @Override
    public List<Task> getHistory() {
        return this.historyManager.getHistory();
    }

    public HistoryManager getInMemoryHistory() {
        return this.historyManager;
    }

    @Override
    public void updateSubtasksInEpic(Epic epic) {
        if (epic != null) {
            ArrayList<Subtask> subtasksInEpic = (ArrayList<Subtask>) this.tasks.values()
                    .stream()
                    .filter(t -> t.getType() == TypeTask.SUBTASK && ((Subtask) t).getIdEpic() == epic.getId())
                    .map(t -> (Subtask) t)
                    .collect(Collectors.toList());
            if (subtasksInEpic != null && !subtasksInEpic.isEmpty()) {
                epic.setSubtasks(subtasksInEpic);
            }
        }
    }

    public boolean canInsertInPrioritizedTasks(Task task) {
        if (task.getStartTime() != null) {
            return true;
        } else {
            return false;
        }
    }

    public List<Task> getPrioritizedTasks() {
        return this.prioritizedTasks.stream().collect(Collectors.toList());
    }

    public boolean isIntersectionProblem(Task task) {
        if (task.getType() == TypeTask.EPIC || getPrioritizedTasks().isEmpty()) {
            return false;
        } else {
            LocalDateTime endTime = task.getEndTime();
            LocalDateTime startTime = task.getStartTime();
            if (endTime != null && startTime != null) {
                Task existTask;
                List<Task> prioritizedTasks = getPrioritizedTasks();
                existTask = prioritizedTasks.stream()
                        .filter(t -> t.getStartTime() != null && t.getEndTime() != null && t.getId() != task.getId()
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