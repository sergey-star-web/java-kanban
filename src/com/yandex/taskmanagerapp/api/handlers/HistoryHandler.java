package com.yandex.taskmanagerapp.api.handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.yandex.taskmanagerapp.service.TaskManager;

import java.io.IOException;

public class HistoryHandler extends BaseHttpHandler implements HttpHandler {
    public HistoryHandler(TaskManager tm) {
        super(tm);
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        switch(httpExchange.getRequestMethod()) {
            case "GET":
                handleGetRequest(httpExchange);
            default:
                sendNotFound(httpExchange, "");
        }
    }

    private void handleGetRequest(HttpExchange httpExchange) throws IOException {
        String[] path = httpExchange.getRequestURI().toString().split("/");
        String pathMap = path[1];

        // Обработаем каждый метод запроса
        if (pathMap.equals("history")) {
            sendText(httpExchange, gson.toJson(this.taskManager.getHistory()));
        }
    }
}
