package com.yandex.taskmanagerapp.api;

import com.sun.net.httpserver.*;
import com.yandex.taskmanagerapp.api.handlers.*;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.yandex.taskmanagerapp.service.TaskManager;

public class HttpTaskServer {
    private static final Integer PORT = 8080;
    public static final String HOST = "http://localhost:" + PORT + "/";
    private static final Integer BACKLOG = 5;
    private static final Integer MAX_CONNECTION = 5;
    private static final Integer DELAY = 3;
    private HttpClient httpClient;
    private HttpServer httpServer;
    private TaskManager taskManager;

    public HttpTaskServer(TaskManager taskManager) {
        this.taskManager = taskManager;
    }

    public void start() {
        init(PORT, BACKLOG, MAX_CONNECTION);
    }

    public void stop() {
        httpServer.stop(DELAY);
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
        System.out.println("Сервер запущен на порту: " + PORT);
        System.out.println("Путь: " + uriPath);
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