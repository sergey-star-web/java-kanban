package com.yandex.taskmanagerapp.constants;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.yandex.taskmanagerapp.api.jsonadapters.DurationAdapter;
import com.yandex.taskmanagerapp.api.jsonadapters.LocalDateTimeAdapter;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Constant {
    public static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static Gson getGsonObject() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter());
        gsonBuilder.registerTypeAdapter(Duration.class, new DurationAdapter());
        Gson gson = gsonBuilder.create();
        return gson;
    }
}
