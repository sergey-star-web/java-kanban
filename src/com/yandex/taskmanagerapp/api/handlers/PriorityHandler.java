package com.yandex.taskmanagerapp.api.handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.yandex.taskmanagerapp.service.TaskManager;

import java.io.IOException;

public class PriorityHandler extends BaseHttpHandler implements HttpHandler {
    public PriorityHandler(TaskManager tm) {
        super(tm);
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        switch (httpExchange.getRequestMethod()) {
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
        if (pathMap.equals("prioritized")) {
            sendText(httpExchange, gson.toJson(this.taskManager.getPrioritizedTasks()));
        }
    }
}
