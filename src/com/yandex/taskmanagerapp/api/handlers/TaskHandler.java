package com.yandex.taskmanagerapp.api.handlers;

import com.yandex.taskmanagerapp.api.HttpTaskServer;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.yandex.taskmanagerapp.service.TaskManager;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.net.http.HttpRequest;
import java.util.List;

public class TaskHandler extends BaseHttpHandler implements HttpHandler {

    public TaskHandler(TaskManager tm, HttpRequest request) {
        super(tm, request);
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        String response;

        // извлеките метод из запроса
        String method = httpExchange.getRequestMethod();

        switch(method) {
            case "POST":
                response = handlePostRequest(httpExchange);
            case "GET":
                response = handleGetRequest(httpExchange);
                // не забудьте про ответ для остальных методов
            default:
                response = null;
        }

        httpExchange.sendResponseHeaders(200, 0);
        try (OutputStream os = httpExchange.getResponseBody()) {
            os.write(response.getBytes());
        }
    }

    private String handleGetRequest(HttpExchange httpExchange) {
        // обработайте GET-запрос в соответствии с условиями задания
        String method = this.request.method();
        String[] path = this.request.uri().getPath().split("/");
        String pathMap = path[1];
        Integer pathId = null;
        String text = null;

        if (path.length > 2) {
            pathId = Integer.parseInt(path[2]);
        }
        // Обработаем каждый метод запроса
        if (method.equals("GET")) {

            GsonBuilder gsonBuilder = new GsonBuilder();
            Gson gson = gsonBuilder.create();
            if (pathMap.equals("tasks")) {
                if (pathId != null) {
                    text = this.taskManager.getTask(pathId).toString();
                } else {
                    text = this.taskManager.getTasks().toString();
                }

            }
        }
        return text;
    }

    private String handlePostRequest(HttpExchange httpExchange) throws IOException {
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
        if ((wishGoodDay != null) && (wishGoodDay.contains("true"))) {
            return response + " " + name + "!" + " Хорошего дня!";
        } else if (wishGoodDay == null) {
            return response;
        } else {
            return "Некорректный метод!";
        }
    }
}
