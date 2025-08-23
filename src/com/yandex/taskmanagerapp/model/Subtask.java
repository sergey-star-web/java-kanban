package com.yandex.taskmanagerapp.model;

import com.yandex.taskmanagerapp.enums.Status;
import com.yandex.taskmanagerapp.enums.TypeTask;

import java.time.Duration;
import java.time.LocalDateTime;

public class Subtask extends Task {
    private Integer idEpic = 0;

    public Subtask(String name, String description, Status status, Integer duration, String startTime) {
        super(name, description, status, duration, startTime);
    }

    public Subtask(String name, String description, Status status) {
        super(name, description, status);
    }

    public Subtask(Integer id, String name, String description, Status status, Integer idEpic) {
        super(id, name, description, status);
        this.idEpic = idEpic;
    }

    public Subtask(Integer id, String name, String description, Status status, Integer idEpic, String startTime,
                   Integer duration) {
        super(id, name, description, status, duration, startTime);
        this.idEpic = idEpic;
    }

    public Subtask(Integer id, String name, String description, Status status, Integer idEpic, Duration duration,
                   LocalDateTime startTime) {
        super(id, name, description, status, duration, startTime);
        this.idEpic = idEpic;
    }

    public Subtask() {
    }

    public Subtask(Subtask originalSubtask) {
        this(originalSubtask.getName(), originalSubtask.getDescription(), originalSubtask.getStatus(),
                Integer.parseInt(String.valueOf(originalSubtask.getDuration().toMinutes())),
                originalSubtask.getStartTime().toString());
        this.setId(originalSubtask.getId());
        this.idEpic = originalSubtask.getIdEpic();
    }

    @Override
    public TypeTask getType() {
        return TypeTask.SUBTASK;
    }

    @Override
    public String toString() {
        return getId() +
                "," + getType() +
                "," + getName() +
                "," + getStatus() +
                "," + getDescription() +
                "," + getDurationMinutes() +
                "," + getStartTime() +
                "," + getIdEpic();
    }

    public Integer getIdEpic() {
        return idEpic;
    }

    public void setIdEpic(Integer idEpic) {
        this.idEpic = idEpic;
    }
}
