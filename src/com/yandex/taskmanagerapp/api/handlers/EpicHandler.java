package com.yandex.taskmanagerapp.api.handlers;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.yandex.taskmanagerapp.enums.TypeTask;
import com.yandex.taskmanagerapp.model.Epic;
import com.yandex.taskmanagerapp.model.Subtask;
import com.yandex.taskmanagerapp.service.TaskManager;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpRequest;
import java.util.List;

public class EpicHandler extends BaseHttpHandler implements HttpHandler {
    public EpicHandler(TaskManager tm, HttpRequest request) {
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

    private void handleGetRequest(HttpExchange httpExchange) throws IOException {
        String method = httpExchange.getRequestMethod();
        String[] path = httpExchange.getRequestURI().toString().split("/");
        String pathMap = path[1];
        Epic epic = null;
        Integer pathId = null;
        String checkSubsInEpic = null;

        if (path.length > 2) {
            pathId = Integer.parseInt(path[2]);
            if (this.taskManager.getTask(pathId).getType() == TypeTask.EPIC) {
                epic = (Epic) this.taskManager.getTask(pathId);
            }
        }
        if (path.length > 3) {
            checkSubsInEpic = path[3];
        }
        // Обработаем каждый метод запроса
        if (pathMap.equals("epics")) {
            if (checkSubsInEpic != null && epic != null) {
                sendText(httpExchange, gson.toJson(this.taskManager.getSubtasksInEpic(epic.getId())));
            } else if (pathId != null) {
                if (epic != null) {
                    sendText(httpExchange, gson.toJson(epic));
                } else {
                    sendNotFound(httpExchange,"");
                }
            }
            else {
                sendText(httpExchange, gson.toJson(this.taskManager.getEpics()));
            }
        }
    }

    private void handlePostRequest(HttpExchange httpExchange) throws IOException {
        // обработайте POST-запрос в соответствии с условиями задания
        URI requestURI = httpExchange.getRequestURI();
        // извлеките path из запроса
        String path = httpExchange.getRequestURI().getPath();
        String[] splitStrings = path.split("/");
        // а из path — профессию и имя
        String profession = splitStrings[2];
        String name = splitStrings[3];

        // извлеките тело запроса
        Headers requestHeaders = httpExchange.getRequestHeaders();
        String body = httpExchange.getRequestBody().toString();
        // объедините полученные данные из тела и пути запроса
        String response = body + ", " + profession;
        // извлеките заголовок и в зависимости от условий дополните ответ
        List<String> wishGoodDay =  requestHeaders.get("X-Wish-Good-Day");

    }
}
