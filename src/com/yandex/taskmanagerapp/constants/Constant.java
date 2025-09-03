package com.yandex.taskmanagerapp.constants;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.yandex.taskmanagerapp.api.jsonadapters.DurationAdapter;
import com.yandex.taskmanagerapp.api.jsonadapters.LocalDateTimeAdapter;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Constant {
    public static final DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    public static final Integer port = 8080;
    public static final String host = "http://localhost:" + port + "/";

    public static Gson getGsonObject() {
        LocalDateTimeAdapter localTimeTypeAdapter = new LocalDateTimeAdapter();
        DurationAdapter durationTypeAdapter = new DurationAdapter();
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(LocalDateTime.class, localTimeTypeAdapter);
        gsonBuilder.registerTypeAdapter(Duration.class, durationTypeAdapter);
        Gson gson = gsonBuilder.create();
        return gson;
    }
}
