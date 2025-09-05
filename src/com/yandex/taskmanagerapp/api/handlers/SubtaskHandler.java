package com.yandex.taskmanagerapp.api.handlers;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.yandex.taskmanagerapp.constants.Constant;
import com.yandex.taskmanagerapp.enums.Status;
import com.yandex.taskmanagerapp.enums.TypeTask;
import com.yandex.taskmanagerapp.model.Subtask;
import com.yandex.taskmanagerapp.model.Task;
import com.yandex.taskmanagerapp.service.TaskManager;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;

public class SubtaskHandler extends TaskHandler implements HttpHandler {
    public SubtaskHandler(TaskManager tm) {
        super(tm);
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        if (getPath(httpExchange)[1].equals("subtasks")) {
            callingHandlers(httpExchange);
        }
    }

    @Override
    protected void handleGetRequest(HttpExchange httpExchange) throws IOException {
        Integer pathId = getPathId(getPath(httpExchange));

        if (pathId != null ) {
            Task task = this.taskManager.getTask(pathId);
            if (task != null && task.getType() == TypeTask.SUBTASK) {
                sendText(httpExchange, gson.toJson(task));
            } else {
                sendNotFound(httpExchange,"");
            }
        } else {
            sendText(httpExchange, gson.toJson(this.taskManager.getSubtasks()));
        }
    }

    @Override
    public Subtask fromJson(String body) {
        JsonObject jsonBody = JsonParser.parseString(body).getAsJsonObject();
        Integer id = Integer.parseInt(jsonBody.get("id").getAsString());
        String name = jsonBody.get("name").getAsString();
        String description = jsonBody.get("description").getAsString();
        Status status = Status.valueOf(jsonBody.get("status").getAsString());
        Duration duration = Duration.ofMinutes(Integer.parseInt(jsonBody.get("duration").getAsString()));
        LocalDateTime startTime = null;
        try {
            startTime = LocalDateTime.parse(jsonBody.get("startTime").getAsString(), Constant.dateFormat);
        } catch (DateTimeParseException | NullPointerException e) {
            startTime = null;
        }
        Integer idEpic = Integer.parseInt(jsonBody.get("idEpic").getAsString());
        Subtask subtask = new Subtask(id, name, description, status, idEpic, duration, startTime);
        return subtask;
    }

    @Override
    protected void handlePostRequest(HttpExchange httpExchange) throws IOException {
        Integer pathId = getPathId(getPath(httpExchange));
        InputStream inputStream = httpExchange.getRequestBody();
        String body = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
        Subtask subtask = this.fromJson(body);

        if (pathId != null && !this.taskManager.getTasks().contains(subtask)) {
            if (subtask.getType() == TypeTask.SUBTASK) {
                checkIntersectionProblem(subtask, httpExchange);
                this.taskManager.updateSubtask(subtask);
                sendEmpty(httpExchange);
            }
        } else {
            checkIntersectionProblem(subtask, httpExchange);
            this.taskManager.addSubtask(subtask);
            sendEmpty(httpExchange);
        }
    }
}
