package com.yandex.taskmanagerapp.api.handlers;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.yandex.taskmanagerapp.enums.TypeTask;
import com.yandex.taskmanagerapp.model.Task;
import com.yandex.taskmanagerapp.service.TaskManager;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpRequest;
import java.util.List;

public class TaskHandler extends BaseHttpHandler implements HttpHandler {

    public TaskHandler(TaskManager tm, HttpRequest request) {
        super(tm, request);
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        String method = httpExchange.getRequestMethod();
        switch(method) {
            case "POST":
                handlePostRequest(httpExchange);
            case "GET":
                handleGetRequest(httpExchange);
            default:
                sendNotFound(httpExchange,"");
        }
    }

    private String[] getPath(HttpExchange httpExchange) {
        String[] path = httpExchange.getRequestURI().toString().split("/");
        return path;
    }

    private Integer getPathId(String[] path) {
        Integer pathId = null;
        if (path.length > 2) {
            pathId = Integer.parseInt(path[2]);
        }
        return pathId;
    }

    private void handleGetRequest(HttpExchange httpExchange) throws IOException {
        Integer pathId = getPathId(getPath(httpExchange));

        // Обработаем каждый метод запроса
        if (getPath(httpExchange)[1].equals("tasks")) {
            if (pathId != null ) {
                Task task = this.taskManager.getTask(pathId);
                if (task != null && task.getType() == TypeTask.TASK) {
                    sendText(httpExchange, gson.toJson(task));
                } else {
                    sendNotFound(httpExchange,"");
                }
            } else {
                sendText(httpExchange, gson.toJson(this.taskManager.getTasks()));
            }
        }
    }

    private void handlePostRequest(HttpExchange httpExchange) throws IOException {
        Integer pathId = getPathId(getPath(httpExchange));

        // Обработаем каждый метод запроса
        if (getPath(httpExchange)[1].equals("tasks")) {
            if (pathId != null ) {
                Task task = this.taskManager.getTask(pathId);
                if (task != null && task.getType() == TypeTask.TASK) {
                    //sendHasOverlaps(httpExchange, gson.toJson(task));
                }
            } else {
                //sendHasOverlaps(httpExchange, gson.toJson(this.taskManager.getTasks()));
            }
        }
    }
}
