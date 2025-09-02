package com.yandex.taskmanagerapp.api;

import com.google.gson.*;
import com.sun.net.httpserver.*;
import com.yandex.taskmanagerapp.api.handlers.BaseHttpHandler;
import com.yandex.taskmanagerapp.api.handlers.TaskHandler;
import com.yandex.taskmanagerapp.constants.Constant;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.yandex.taskmanagerapp.enums.Status;
import com.yandex.taskmanagerapp.exceptions.ManagerSaveException;
import com.yandex.taskmanagerapp.model.Epic;
import com.yandex.taskmanagerapp.model.Subtask;
import com.yandex.taskmanagerapp.model.Task;
import com.yandex.taskmanagerapp.service.FileBackedTaskManager;
import com.yandex.taskmanagerapp.service.InMemoryTaskManager;
import com.yandex.taskmanagerapp.service.Managers;
import com.yandex.taskmanagerapp.service.TaskManager;

public class HttpTaskServer {
    private static HttpClient httpClient;
    private static HttpServer httpServer;
    private static TaskManager taskManager;

    public static void main(String[] args) {

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
        Subtask sub2 = new Subtask("second_sub", "non desc", Status.NEW, 40, "2025-08-03 13:00:00");
        Epic epic2 = new Epic("second_epic", "non desc");
        Subtask sub3 = new Subtask("thirst_sub", "non desc", Status.NEW, 50, "2025-08-03 15:35:00");
        Subtask sub4 = new Subtask("four_sub", "non desc", Status.NEW, 100, null);
        Subtask sub5 = new Subtask("five_sub", "non desc", Status.NEW, 80, "2025-08-03 12:00:00");

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

        try {
            start();

            URI uri = URI.create("http://localhost:" + Constant.port + "/epics/3/subtasks");
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(uri)
                    .GET()
                    .build();
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            //BaseHttpHandler baseHttpHandler = new BaseHttpHandler();
            //HttpContext httpContext = httpServer.createContext("/hello", new BaseHttpHandler());
            handleRequest(request, response);

            System.out.println("Сервер запущен на порту " + Constant.port);
            stop();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void start() {
        init(8080, 5, 5);
        httpServer.start();
    }

    public static void stop() {
        httpServer.stop(3);
    }

    public static void handleRequest(HttpRequest request, HttpResponse response) {
        // Получаем метод запроса (GET, POST и DELETE)
        String method = request.method();
        String[] path = request.uri().getPath().split("/");
        String pathMap = path[1];
        Integer pathId = null;
        if (path.length > 2) {
            pathId = Integer.parseInt(path[2]);
        }
        // Обработаем каждый метод запроса
        if (method.equals("GET")) {

            //BaseHttpHandler handler = new BaseHttpHandler(taskManager, request);
            GsonBuilder gsonBuilder = new GsonBuilder();
            Gson gson = gsonBuilder.create();
            if (pathMap.equals("tasks")) {
                String text = null;
                if (pathId != null) {
                    text = taskManager.getTask(pathId).toString();
                } else {
                    text = taskManager.getTasks().toString();
                }
               // try {
                    httpServer.createContext(request.uri().getPath(), new TaskHandler(taskManager, request));
                    //handler.sendText(response, gson.toJson(text));
                //} catch (IOException e) {
                 //   throw new RuntimeException(e);
                //}
            }

            if (pathMap.equals("subtasks")) {
                String text = null;
                if (pathId != null) {
                    text = taskManager.getTask(pathId).toString();
                } else {
                    text = taskManager.getTasks().toString();
                }
                try {
                    handler.sendText(response, gson.toJson(text));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

            if (pathMap.equals("epics")) {
                String pathS = null;
                String text = null;
                if (path.length > 3) {
                    pathS = path[3];
                }
                if (pathS != null && pathS.equals("subtasks")) {
                    text = taskManager.getSubtasksInEpic(pathId).toString();
                } else if (pathId != null) {
                    text = taskManager.getTask(pathId).toString();
                } else {
                    text = taskManager.getTasks().toString();
                }
                try {
                    handler.sendText(response, gson.toJson(text));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

            if (pathMap.equals("history")) {
                try {
                    handler.sendText(response, gson.toJson(taskManager.getHistory()));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

            if (pathMap.equals("prioritized")) {
                try {
                    handler.sendText(response, gson.toJson(taskManager.getPrioritizedTasks().toString()));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

        } else if (method.equals("POST")) {
            String responseBody = "Hello, world!";
        } else if (method.equals("DELETE")) {
            String responseBody = "Hello, world!";
        } else {
            System.out.println("Метод: " + method + " не обрабатывается");
        }
    }

    public static void init(Integer port, Integer backlog, Integer maxConnections) {
        taskManager = Managers.getMemoryTaskManager();
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

    public static TaskManager getTaskManager() {
        return taskManager;
    }
}
