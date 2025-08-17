package com.yandex.taskmanagerapp;

import com.yandex.taskmanagerapp.exceptions.ManagerSaveException;
import com.yandex.taskmanagerapp.model.Task;
import com.yandex.taskmanagerapp.model.Subtask;
import com.yandex.taskmanagerapp.model.Epic;
import com.yandex.taskmanagerapp.service.FileBackedTaskManager;
import com.yandex.taskmanagerapp.enums.Status;
import com.yandex.taskmanagerapp.service.Managers;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.List;
import java.util.TreeSet;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

public class Main {
    public static void main(String[] args) {
        FileBackedTaskManager ftm = (FileBackedTaskManager) Managers.getDefault();
        List<Task> tasksList;
        List<Task> historyTasks;
        Task getTask;
        Subtask getSubTask;
        Epic getEpic;
        File file;
        File fileTestLoad;

        Task task1 = new Task("first_task", "non desc", Status.NEW, 35, "2025-08-01 14:15:30");
        Task task2 = new Task("second_task", "non desc", Status.NEW, 45, "2025-08-01 10:00:00");
        Epic epic1 = new Epic("first_epic", "non desc");
        Subtask sub1 = new Subtask("first_sub", "non desc", Status.NEW, 60, "2025-08-04 10:30:00");
        Subtask sub2 = new Subtask("second_sub", "non desc", Status.NEW, 40, "2025-08-03 10:40:00");
        Epic epic2 = new Epic("second_epic", "non desc");
        Subtask sub3 = new Subtask("thirst_sub", "non desc", Status.NEW, 50, "2025-08-03 15:35:00");
        Subtask sub4 = new Subtask("four_sub", "non desc", Status.NEW, 100, "2025-08-04 10:00:00");
        Subtask sub5 = new Subtask("five_sub", "non desc", Status.NEW, 80, "2025-08-03 12:00:00");

        try {
            file = File.createTempFile("historyTasks", ".csv", new File("C:"));
            ftm = new FileBackedTaskManager(file);
        } catch (IOException e) {
            throw new ManagerSaveException("Произошла ошибка во время обработки файла.");
        }

        ftm.addTask(task1);
        ftm.addTask(task2);
        ftm.addEpic(epic1);
        ftm.addEpic(epic2);
        sub1.setIdEpic(epic1.getId());
        sub2.setIdEpic(epic1.getId());
        sub3.setIdEpic(epic2.getId());
        sub4.setIdEpic(epic1.getId());
        sub5.setIdEpic(epic2.getId());
        ftm.addSubtask(sub1);
        ftm.addSubtask(sub2);
        ftm.addSubtask(sub3);
        ftm.addSubtask(sub4);
        ftm.addSubtask(sub5);

        tasksList = ftm.getAllTasks();
        System.out.println();
        System.out.println("Список задач1: ");
        for (Task task : tasksList) {
            System.out.println(task);
        }
        System.out.println();

        TreeSet<Task> sortTasks = ftm.getPrioritizedTasks();
        System.out.println();
        System.out.println("Список задач отсортировано1: ");
        for (Task task : sortTasks) {
            System.out.println(task);
        }
        System.out.println();

        sub3.setStatus(Status.DONE);
        ftm.updateSubtask(sub3);

        sub1.setStatus(Status.IN_PROGRESS);
        ftm.updateSubtask(sub1);

        task1.setStatus(Status.DONE);
        ftm.updateTask(task1);

        getSubTask = (Subtask) ftm.getTask(sub3.getId());
        System.out.println(getSubTask);

        System.out.println();
        historyTasks = ftm.getHistory();
        System.out.println("История просмотров1:");
        for (Task task : historyTasks) {
            System.out.println(task);
        }
        System.out.println();

        getSubTask = (Subtask) ftm.getTask(sub1.getId());
        System.out.println(getSubTask);

        getTask = ftm.getTask(task1.getId());
        System.out.println(getTask);

        getSubTask = (Subtask) ftm.getTask(sub3.getId());
        System.out.println(getSubTask);

        System.out.println();
        historyTasks = ftm.getHistory();
        System.out.println("История просмотров1.5: ");
        for (Task task11 : historyTasks) {
            System.out.println(task11);
        }
        System.out.println();

        getEpic = (Epic) ftm.getTask(epic1.getId()); // проверяем состояние эпиков
        System.out.println(getEpic);

        getEpic = (Epic) ftm.getTask(epic2.getId()); // проверяем состояние эпиков
        System.out.println(getEpic);

        historyTasks = ftm.getHistory();
        System.out.println();
        System.out.println("История просмотров2:");
        for (Task task : historyTasks) {
            System.out.println(task);
        }
        System.out.println();

        ftm.getTask(task1.getId());
        ftm.getTask(task1.getId());
        ftm.getTask(task1.getId());

        historyTasks = ftm.getHistory();
        System.out.println("История просмотров3:");
        for (Task task : historyTasks) {
            System.out.println(task);
        }
        System.out.println();

        ftm.deleteTask(1);
        ftm.deleteTask(6);

        tasksList = ftm.getAllTasks();
        System.out.println();
        System.out.println("Список задач2: ");
        for (Task task : tasksList) {
            System.out.println(task);
        }
        System.out.println();

        try {
            List<String> tasksFromFile = Files.readAllLines(file.toPath(), StandardCharsets.UTF_8);
            System.out.println();
            System.out.println("Проверка строк в файле: ");
            for (String task : tasksFromFile) {
                System.out.println(task);
            }
            System.out.println();
        } catch (IOException e) {
            throw new ManagerSaveException("Произошла ошибка во время обработки файла.");
        }

        try {
            fileTestLoad = File.createTempFile("historyTasksTestLoad", ".csv", new File("C:"));
            Files.copy(file.toPath(), fileTestLoad.toPath(), REPLACE_EXISTING);
        } catch (IOException e) {
            throw new ManagerSaveException("Произошла ошибка во время обработки файла.");
        }
        ftm.deleteTasks(); // удалим все задачи для проверки восстановления данных из темп файла
        tasksList = ftm.getAllTasks();

        System.out.println();
        System.out.println("Список задач после их удаления: " + tasksList);
        System.out.println();

        FileBackedTaskManager ftmLoad = FileBackedTaskManager.loadFromFile(fileTestLoad);

        tasksList = ftmLoad.getAllTasks();
        System.out.println();
        System.out.println("Список задач после восстановления: ");
        for (Task task : tasksList) {
            System.out.println(task);
        }
        System.out.println();

    }
}
