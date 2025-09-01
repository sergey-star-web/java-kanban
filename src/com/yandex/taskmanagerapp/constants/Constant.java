package com.yandex.taskmanagerapp.constants;

import java.time.format.DateTimeFormatter;

public class Constant {
    public static final DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    public static final Integer port = 8080;
    public static final String host = "http://localhost:" + port + "/";
}
