package com.yandex.taskmanagerapp.api.handlers;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.yandex.taskmanagerapp.enums.Status;
import com.yandex.taskmanagerapp.enums.TypeTask;
import com.yandex.taskmanagerapp.model.Task;
import com.yandex.taskmanagerapp.service.TaskManager;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class TaskHandler extends BaseHttpHandler implements HttpHandler {

    public TaskHandler(TaskManager tm) {
        super(tm);
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        if (getPath(httpExchange)[1].equals("tasks")) {
            callingHandlers(httpExchange);
        }
    }

    protected void callingHandlers(HttpExchange httpExchange) throws IOException {
        switch (httpExchange.getRequestMethod()) {
            case "POST":
                handlePostRequest(httpExchange);
            case "GET":
                handleGetRequest(httpExchange);
            case "DELETE":
                handleDeleteRequest(httpExchange);
            default:
                sendNotFound(httpExchange, "");
        }
    }

    protected String[] getPath(HttpExchange httpExchange) {
        String[] path = httpExchange.getRequestURI().toString().split("/");
        return path;
    }

    protected Integer getPathId(String[] path) {
        Integer pathId = null;
        if (path.length > 2) {
            pathId = Integer.parseInt(path[2]);
        }
        return pathId;
    }

    protected void handleGetRequest(HttpExchange httpExchange) throws IOException {
        Integer pathId = getPathId(getPath(httpExchange));

        if (pathId != null) {
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

    protected Task fromJson(String body) {
        JsonObject jsonBody = JsonParser.parseString(body).getAsJsonObject();
        Integer id = Integer.parseInt(jsonBody.get("id").getAsString());
        String name = jsonBody.get("name").getAsString();
        String description = jsonBody.get("description").getAsString();
        Status status = Status.valueOf(jsonBody.get("status").getAsString());
        Integer duration = Integer.parseInt(jsonBody.get("duration").getAsString());
        String startTime = jsonBody.get("startTime").getAsString();

        Task task = new Task(id, name, description, status, duration, startTime);
        return task;
    }

    protected void checkIntersectionProblem(Task task, HttpExchange httpExchange) throws IOException {
        if (this.taskManager.isIntersectionProblem(task)) {
            sendHasOverlaps(httpExchange);
        }
    }

    protected void handlePostRequest(HttpExchange httpExchange) throws IOException {
        Integer pathId = getPathId(getPath(httpExchange));

        InputStream inputStream = httpExchange.getRequestBody();
        String body = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
        Task task = this.fromJson(body);

        if (pathId != null && !this.taskManager.getTasks().contains(task)) {
            if (task.getType() == TypeTask.TASK) {
                checkIntersectionProblem(task, httpExchange);
                this.taskManager.updateTask(task);
                sendEmpty(httpExchange);
            }
        } else {
            checkIntersectionProblem(task, httpExchange);
            this.taskManager.addTask(task);
            sendEmpty(httpExchange);
        }
    }

    protected void handleDeleteRequest(HttpExchange httpExchange) throws IOException {
        Integer pathId = getPathId(getPath(httpExchange));
        if (pathId != null) {
            this.taskManager.deleteTask(pathId);
        }
        sendText(httpExchange, "");
    }
}
