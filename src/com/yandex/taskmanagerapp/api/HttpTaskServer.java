package com.yandex.taskmanagerapp.api;

import com.sun.net.httpserver.*;
import com.yandex.taskmanagerapp.api.handlers.*;
import com.yandex.taskmanagerapp.constants.Constant;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.yandex.taskmanagerapp.enums.Status;
import com.yandex.taskmanagerapp.model.Epic;
import com.yandex.taskmanagerapp.model.Subtask;
import com.yandex.taskmanagerapp.model.Task;
import com.yandex.taskmanagerapp.service.Managers;
import com.yandex.taskmanagerapp.service.TaskManager;

public class HttpTaskServer {
    private static HttpClient httpClient;
    private static HttpServer httpServer;
    private static TaskManager taskManager;

    public static void main(String[] args) {
        Task task1 = new Task("first_task", "non desc", Status.NEW, 35, "2025-08-01 14:15:30");
        Task task2 = new Task("second_task", "non desc", Status.NEW, 45, "2025-08-01 10:00:00");
        Epic epic1 = new Epic("first_epic", "non desc");
        Subtask sub1 = new Subtask("first_sub", "non desc", Status.NEW, 60, "2025-08-04 10:30:00");
        Subtask sub2 = new Subtask("second_sub", "non desc", Status.NEW, 40, "2025-08-03 13:00:00");
        Epic epic2 = new Epic("second_epic", "non desc");
        Subtask sub3 = new Subtask("thirst_sub", "non desc", Status.NEW, 50, "2025-08-03 15:35:00");
        Subtask sub4 = new Subtask("four_sub", "non desc", Status.NEW, 100, null);
        Subtask sub5 = new Subtask("five_sub", "non desc", Status.NEW, 80, "2025-08-03 12:00:00");
        HttpTaskServer taskserver = new HttpTaskServer(Managers.getMemoryTaskManager());
        TaskManager taskManager = taskserver.getTaskManager();

        taskManager.addTask(task1);
        taskManager.addTask(task2);
        taskManager.addEpic(epic1);
        taskManager.addEpic(epic2);
        sub1.setIdEpic(epic1.getId());
        sub2.setIdEpic(epic1.getId());
        sub3.setIdEpic(epic2.getId());
        sub4.setIdEpic(epic1.getId());
        sub5.setIdEpic(epic2.getId());
        taskManager.addSubtask(sub1);
        taskManager.addSubtask(sub2);
        taskManager.addSubtask(sub3);
        taskManager.addSubtask(sub4);
        taskManager.addSubtask(sub5);

        taskManager.getTask(sub3.getId());
        taskManager.getTask(sub1.getId());
        taskManager.getTask(task1.getId());
        taskManager.getTask(sub3.getId());

        String path = "http://localhost:" + Constant.port + "/tasks";
        URI uri = URI.create(path);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .header("Content-Type", "application/json;charset=utf-8")
                .build();

        taskserver.start();
        taskserver.handleRequest(request);

        System.out.println("Сервер запущен на порту: " + Constant.port);
        System.out.println("Путь: " + path);
        taskserver.stop();
    }

    public HttpTaskServer(TaskManager taskManager) {
        HttpTaskServer.taskManager = taskManager;
    }

    public void start() {
        init(8080, 5, 5);
    }

    public void stop() {
        httpServer.stop(3);
    }

    public void handleRequest(HttpRequest req) {
        // Получаем метод запроса (GET, POST и DELETE)
        String uriPath = req.uri().getPath();
        String[] path = uriPath.split("/");
        String pathMap = path[1];
        TaskManager taskManager = getTaskManager();

        // Обработаем каждый метод запроса
        if (pathMap.equals("tasks")) {
            httpServer.createContext(uriPath, new TaskHandler(taskManager));
        } else if (pathMap.equals("subtasks")) {
            httpServer.createContext(uriPath, new SubtaskHandler(taskManager));
        } else if (pathMap.equals("epics")) {
            httpServer.createContext(uriPath, new EpicHandler(taskManager));
        } else if (pathMap.equals("history")) {
            httpServer.createContext(uriPath, new HistoryHandler(taskManager));
        } else if (pathMap.equals("prioritized")) {
            httpServer.createContext(uriPath, new PriorityHandler(taskManager));
        }
        httpServer.start();
    }

    public void init(Integer port, Integer backlog, Integer maxConnections) {
        try {
            httpClient = HttpClient.newHttpClient();
            httpServer = HttpServer.create();
            httpServer.bind(new InetSocketAddress(port), backlog);
            // Настройка максимального количества подключений
            ExecutorService executor = Executors.newFixedThreadPool(maxConnections);
            httpServer.setExecutor(executor);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public TaskManager getTaskManager() {
        return taskManager;
    }
}