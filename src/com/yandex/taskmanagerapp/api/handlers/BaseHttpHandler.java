package com.yandex.taskmanagerapp.api.handlers;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.yandex.taskmanagerapp.constants.Constant;
import com.yandex.taskmanagerapp.service.TaskManager;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public abstract class BaseHttpHandler {
    protected TaskManager taskManager;
    protected Gson gson = Constant.getGsonObject();

    public BaseHttpHandler(TaskManager tm) {
        this.taskManager = tm;
    }

    protected void sendText(HttpExchange h, String text) throws IOException {
        byte[] resp = text.getBytes(StandardCharsets.UTF_8);
        h.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
        h.sendResponseHeaders(200, resp.length);
        h.getResponseBody().write(resp);
        h.close();
    }

    protected void sendNotFound(HttpExchange h, String text) throws IOException {
        byte[] resp = text.getBytes(StandardCharsets.UTF_8);
        h.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
        h.sendResponseHeaders(404, resp.length);
        h.getResponseBody().write(resp);
        h.close();
    }

    protected void sendHasOverlaps(HttpExchange h) throws IOException {
        h.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
        h.sendResponseHeaders(406, 0);
        h.close();
    }

    protected void sendEmpty(HttpExchange h) throws IOException {
        h.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
        h.sendResponseHeaders(201, 0);
        h.close();
    }
}

