package com.yandex.taskmanagerapp.api.jsonadapters;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.yandex.taskmanagerapp.constants.Constant;

import java.io.IOException;
import java.time.LocalDateTime;

public class LocalDateTimeAdapter extends TypeAdapter<LocalDateTime> {
    @Override
    public void write(final JsonWriter jsonWriter, final LocalDateTime localDate) throws IOException {
        if (localDate != null) {
            jsonWriter.value(localDate.format(Constant.DATE_FORMAT));
        } else {
            jsonWriter.nullValue();
        }
    }

    @Override
    public LocalDateTime read(final JsonReader jsonReader) throws IOException {
        return LocalDateTime.parse(jsonReader.nextString(), Constant.DATE_FORMAT);
    }
}
