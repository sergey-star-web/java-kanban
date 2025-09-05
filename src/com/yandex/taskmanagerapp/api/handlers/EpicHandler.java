package com.yandex.taskmanagerapp.api.handlers;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonArray;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.yandex.taskmanagerapp.enums.Status;
import com.yandex.taskmanagerapp.enums.TypeTask;
import com.yandex.taskmanagerapp.model.Epic;
import com.yandex.taskmanagerapp.model.Subtask;
import com.yandex.taskmanagerapp.model.Task;
import com.yandex.taskmanagerapp.service.TaskManager;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class EpicHandler extends SubtaskHandler implements HttpHandler {
    public EpicHandler(TaskManager tm) {
        super(tm);
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        if (getPath(httpExchange)[1].equals("epics")) {
            callingHandlers(httpExchange);
        }
    }

    @Override
    protected void handleGetRequest(HttpExchange httpExchange) throws IOException {
        Integer pathId = getPathId(getPath(httpExchange));
        String[] path = getPath(httpExchange);
        String subtasksInEpicPath = null;
        if (path.length > 3) {
            subtasksInEpicPath = path[3];
        }

        if (pathId != null ) {
            Task task = this.taskManager.getTask(pathId);
            if (task != null && subtasksInEpicPath != null && task.getType() == TypeTask.EPIC) {
                sendText(httpExchange, gson.toJson(this.taskManager.getSubtasksInEpic(task.getId())));
            } else if (task != null && task.getType() == TypeTask.EPIC && subtasksInEpicPath == null) {
                sendText(httpExchange, gson.toJson(task));
            } else {
                sendNotFound(httpExchange,"");
            }
        } else {
            sendText(httpExchange, gson.toJson(this.taskManager.getEpics()));
        }
    }

    public Epic epicFromJson(String body) {
        JsonObject jsonBody = JsonParser.parseString(body).getAsJsonObject();
        Integer id = Integer.parseInt(jsonBody.get("id").getAsString());
        String name = jsonBody.get("name").getAsString();
        String description = jsonBody.get("description").getAsString();
        Status status = Status.valueOf(jsonBody.get("status").getAsString());
        ArrayList<Subtask> subtasks = new ArrayList<>();
        JsonArray jsonSubtasks = jsonBody.get("subtasks").getAsJsonArray();
        for (JsonElement subtask : jsonSubtasks) {
            Subtask sub = fromJson(String.valueOf(subtask.getAsJsonObject()));
            subtasks.add(sub);
        }

        Epic epic = new Epic(id, name, description, status);
        epic.setSubtasks(subtasks);
        return epic;
    }

    public void updateSubtasksInTaskManager(ArrayList<Subtask> subtasks) {
        ArrayList<Subtask> subCopy = new ArrayList<>(subtasks);
        for (Subtask subtask : subCopy) {
            if (this.taskManager.getSubtasks().contains(subtask)) {
                if(!subtask.equals(this.taskManager.getSubtasks().contains(subtask))) {
                    this.taskManager.updateSubtask(subtask);
                 }
             } else {
                 this.taskManager.addSubtask(subtask);
             }
        }
    }

    @Override
    protected void handlePostRequest(HttpExchange httpExchange) throws IOException {
        Integer pathId = getPathId(getPath(httpExchange));
        InputStream inputStream = httpExchange.getRequestBody();
        String body = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
        Epic epic = epicFromJson(body);

        if (pathId != null && !this.taskManager.getEpics().contains(epic)) {
            if (epic.getType() == TypeTask.EPIC && pathId == epic.getId()) {
                checkIntersectionProblem(epic, httpExchange);
                this.taskManager.addEpic(epic);
                ArrayList<Subtask> subtasks = new ArrayList<>(epic.getSubtasks());
                // обнуляем сабтаски в эпике так как они снова добавятся в updateSubtasksInTaskManager
                epic.setSubtasks(new ArrayList<>());
                updateSubtasksInTaskManager(subtasks);
            }
        } else {
            checkIntersectionProblem(epic, httpExchange);
            this.taskManager.updateEpic(epic);
            updateSubtasksInTaskManager(epic.getSubtasks());
        }
        sendEmpty(httpExchange);
    }
}
