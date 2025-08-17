package com.yandex.taskmanagerapp.service;

import com.yandex.taskmanagerapp.enums.Status;
import com.yandex.taskmanagerapp.enums.TypeTask;
import com.yandex.taskmanagerapp.exceptions.ManagerSaveException;
import com.yandex.taskmanagerapp.model.Epic;
import com.yandex.taskmanagerapp.model.Subtask;
import com.yandex.taskmanagerapp.model.Task;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.List;

public class FileBackedTaskManager extends InMemoryTaskManager {
    private File file;

    public FileBackedTaskManager() {
        super();
    }

    public FileBackedTaskManager(File file) {
        this.file = file;
    }

    private FileBackedTaskManager(File file, Boolean loadFromFile) {
        this.file = file;
        if (loadFromFile) {
            load(file);
        }
    }

    private Task fromString(String value) {
        String[] taskElems = value.split(",");

        if (taskElems.length > 0) {
            Integer id = Integer.parseInt(taskElems[0]);
            TypeTask typeTask = null;
            String name = null;
            Status status = null;
            String description = null;
            Duration duration = null;
            LocalDateTime startTime = null;

            typeTask = TypeTask.valueOf(taskElems[1]);
            name = taskElems[2];
            status = Status.valueOf(taskElems[3]);
            description = taskElems[4].replaceFirst("Description ", "");

            if (typeTask == TypeTask.SUBTASK || typeTask == TypeTask.TASK) {
                duration = Duration.ofMinutes(Integer.parseInt(taskElems[5]));
                try {
                    startTime = LocalDateTime.parse(taskElems[6]);
                }
                catch (DateTimeParseException e) {
                    startTime = null;
                }
            }

            if (typeTask == TypeTask.SUBTASK) {
                Integer idEpic = Integer.parseInt(taskElems[7]);
                Subtask subtask = new Subtask(id, name, description, status, idEpic, duration, startTime);
                return subtask;
            } else if (typeTask == TypeTask.EPIC) {
                Epic epic = new Epic(id, name, description, status);
                updateSubtasksInEpic(epic);
                return epic;
            } else if (typeTask == TypeTask.TASK) {
                Task task = new Task(id, name, description, status, duration, startTime);
                return task;
            }
        }
        return null;
    }

    private void save() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(this.file, StandardCharsets.UTF_8))) {
            List<Task> allTasks = getAllTasks();
            for (Task task : allTasks) {
                bw.write(task.toString() + '\n');
            }
        } catch (IOException e) {
            throw new ManagerSaveException("Произошла ошибка во время обработки файла.");
        }
    }

    private void addTaskFromFile(Task task) {
        if (task == null) {
            return;
        }
        if (task.getType() == TypeTask.SUBTASK) {
            super.addSubtask((Subtask) task);
        } else if (task.getType() == TypeTask.EPIC) {
            super.addEpic((Epic) task);
        } else if (task.getType() == TypeTask.TASK) {
            super.addTask(task);
        }
    }

    private void load(File file) {
        try (BufferedReader reader = new BufferedReader(new FileReader(file, StandardCharsets.UTF_8))) {
            List<String> tasksFromFile = reader.lines().toList();
            for (String taskFromFile : tasksFromFile) {
                Task task = fromString(taskFromFile);
                if (task == null) {
                    System.out.println("Ошибка конвертации строки в задачу: " + taskFromFile);
                } else {
                    addTaskFromFile(task);
                }
            }
        } catch (IOException e) {
            throw new ManagerSaveException("Произошла ошибка во время обработки файла.");
        }
    }

    public static FileBackedTaskManager loadFromFile(File file) {
        return new FileBackedTaskManager(file, true);
    }

    @Override
    public void deleteTasks() {
        super.deleteTasks();
        save();
    }

    @Override
    public void deleteTask(Integer id) {
        super.deleteTask(id);
        save();
    }

    @Override
    public void addTask(Task task) {
        super.addTask(task);
        save();
    }

    @Override
    public void addSubtask(Subtask subtask) {
        super.addSubtask(subtask);
        save();
    }

    @Override
    public void addEpic(Epic epic) {
        super.addEpic(epic);
        save();
    }

    @Override
    protected void updateDataInEpic(Integer idEpic) {
        super.updateDataInEpic(idEpic);
        save();
    }

    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
        save();
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        super.updateSubtask(subtask);
        save();
    }

    @Override
    public void updateEpic(Epic epic) {
        super.updateEpic(epic);
        save();
    }

}
