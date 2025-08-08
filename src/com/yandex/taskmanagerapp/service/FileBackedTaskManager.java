package com.yandex.taskmanagerapp.service;

import com.yandex.taskmanagerapp.enums.Status;
import com.yandex.taskmanagerapp.enums.TypeTask;
import com.yandex.taskmanagerapp.exceptions.ManagerSaveException;
import com.yandex.taskmanagerapp.model.Epic;
import com.yandex.taskmanagerapp.model.Subtask;
import com.yandex.taskmanagerapp.model.Task;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.List;

public class FileBackedTaskManager extends InMemoryTaskManager {
    private File file;

    public FileBackedTaskManager(File file) {
        this.file = file;
    }

    private FileBackedTaskManager(File file, Boolean loadFromFile) {
        this.file = file;
        if (loadFromFile) {
            load(file);
        }
    }

    public String toStringTask(Task task) {
        if (task instanceof Subtask) {
            return TypeTask.SUBTASK + "," + task.getName() + "," + task.getStatus() + "," + "Description " + task.getDescription();
        } else if (task instanceof Epic) {
            return TypeTask.EPIC + "," + task.getName() + "," + task.getStatus() + "," + "Description " + task.getDescription();
        } else if (task instanceof Task) {
            return TypeTask.TASK + "," + task.getName() + "," + task.getStatus() + "," + "Description " + task.getDescription();
        }
        return null;
    }

    public Task fromString(String value) {
        String[] taskElems = value.split(",");

        if (taskElems.length > 0) {
            Integer id = Integer.parseInt(taskElems[0]);
            TypeTask typeTask;
            String name;
            Status status;
            String description;

            typeTask = TypeTask.valueOf(taskElems[1]);
            if (typeTask == null) return null;
            name = taskElems[2];
            if (name.equals(null)) return null;
            status = Status.valueOf(taskElems[3]);
            if (status == null) return null;
            description = taskElems[4].replaceFirst("Description ", "");
            if (description.equals(null)) return null;

            if (typeTask == TypeTask.SUBTASK) {
                Integer idEpic = Integer.parseInt(taskElems[5]);
                Subtask subtask = new Subtask(name, description, status);
                subtask.setId(id);
                subtask.setIdEpic(idEpic);
                return subtask;
            } else if (typeTask == TypeTask.EPIC) {
                Epic epic = new Epic(name, description);
                epic.setId(id);
                epic.setStatus(status);
                return epic;
            } else if (typeTask == TypeTask.TASK) {
                Task task = new Task(name, description, status);
                task.setId(id);
                return task;
            }
        }
        return null;
    }

    private void save() {
        try (FileWriter writer = new FileWriter(this.file)) {
            List<Task> allTasks = getAllTasks();
            Integer counter = 0;
            for (Task task : allTasks) {
                counter++;
                if (task instanceof Epic) {
                    writer.write(counter + "," + toStringTask(task) + "\n");
                    Integer counterEpic = counter;
                    for (Subtask subtask : ((Epic) task).getSubtasks()) {
                        counter++;
                        writer.write(counter + "," + toStringTask(subtask) + "," + counterEpic + "\n");
                    }
                } else if (!(task instanceof Subtask)) {
                    writer.write(counter + "," + toStringTask(task) + "\n");
                }
            }
        } catch (IOException e) {
            throw new ManagerSaveException("Произошла ошибка во время обработки файла.");
        }
    }

    public void load(File file) {
        try {
            List<String> tasksFromFile = Files.readAllLines(file.toPath(), StandardCharsets.UTF_8);
            for (String taskFromFile : tasksFromFile) {
                Task task = fromString(taskFromFile);
                if (task == null) {
                    System.out.println("Ошибка конвертации строки в задачу: " + taskFromFile);
                } else {
                    if (task instanceof Subtask) {
                        createSubtask((Subtask) task);
                    } else if (task instanceof Epic) {
                        createEpic((Epic) task);
                    } else if (task instanceof Task) {
                        createTask(task);
                    }
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
    public void createTask(Task task) {
        super.createTask(task);
        save();
    }

    @Override
    public void createSubtask(Subtask subtask) {
        super.createSubtask(subtask);
        save();
    }

    @Override
    public void createEpic(Epic epic) {
        super.createEpic(epic);
        save();
    }

    @Override
    protected void updateStatusInEpic(Integer idEpic) {
        super.updateStatusInEpic(idEpic);
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
