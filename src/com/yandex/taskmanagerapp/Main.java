package com.yandex.taskmanagerapp;

import com.yandex.taskmanagerapp.model.Task;
import com.yandex.taskmanagerapp.model.Subtask;
import com.yandex.taskmanagerapp.model.Epic;
import com.yandex.taskmanagerapp.service.TaskManager;
import com.yandex.taskmanagerapp.service.Status;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        TaskManager tm = new TaskManager();
        ArrayList<Task> tasksList;
        ArrayList<Subtask> subtaskList;
        ArrayList<Epic> epicList;
        Task getTask;
        Subtask getSubTask;
        Epic getEpic;

        Task task1 = new Task("first_task", "non desc", Status.NEW);
        Task task2 = new Task("second_task", "non desc", Status.NEW);
        Epic epic1 = new Epic("first_epic", "non desc");
        Subtask sub1 = new Subtask("first_sub", "non desc", Status.NEW );
        Subtask sub2 = new Subtask("second_sub", "non desc", Status.NEW );
        Epic epic2 = new Epic("second_epic", "non desc");
        Subtask sub3 = new Subtask("thirst_sub", "non desc", Status.NEW );

        tm.createTask(task1);
        tm.createTask(task2);
        tm.createEpic(epic1);
        tm.createEpic(epic2);
        sub1.setIdEpic(epic1.getId());
        sub2.setIdEpic(epic1.getId());
        sub3.setIdEpic(epic2.getId());
        tm.createSubtask(sub1);
        tm.createSubtask(sub2);
        tm.createSubtask(sub3);

        tasksList = tm.getTasks();
        System.out.println();
        System.out.println("Список задач: " + tasksList);
        System.out.println();

        subtaskList = tm.getSubtasks();
        System.out.println("Список подзадач: " + subtaskList);
        System.out.println();

        epicList = tm.getEpics();
        System.out.println("Список эпиков: " + epicList);
        System.out.println();

        sub3.setStatus(Status.DONE);
        tm.updateSubtask(sub3);

        sub1.setStatus(Status.IN_PROGRESS);
        tm.updateSubtask(sub1);

        task1.setStatus(Status.DONE);
        tm.updateTask(task1);

        getSubTask = tm.getSubtask(sub3.getId());
        System.out.println(getSubTask);

        getSubTask = tm.getSubtask(sub1.getId());
        System.out.println(getSubTask);

        getTask = tm.getTask(task1.getId());
        System.out.println(getTask);

        getEpic = tm.getEpic(epic1.getId()); // проверяем состояние эпиков
        System.out.println(getEpic);

        getEpic = tm.getEpic(epic2.getId()); // проверяем состояние эпиков
        System.out.println(getEpic);

        tm.deleteTask(1);
        tm.deleteEpic(6);

        tasksList = tm.getTasks();
        System.out.println();
        System.out.println("Список задач: " + tasksList);
        System.out.println();

        subtaskList = tm.getSubtasks();
        System.out.println("Список подзадач: " + subtaskList);
        System.out.println();

        epicList = tm.getEpics();
        System.out.println("Список эпиков: " + epicList);
        System.out.println();
    }
}
