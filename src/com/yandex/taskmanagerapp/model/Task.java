package com.yandex.taskmanagerapp.model;

import com.yandex.taskmanagerapp.constants.Constant;
import com.yandex.taskmanagerapp.enums.Status;
import com.yandex.taskmanagerapp.enums.TypeTask;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;

public class Task {
    private Integer id;
    private String name;
    private String description;
    private Status status;
    private Duration duration;
    private LocalDateTime startTime;

    public Task(String name, String description, Status status, Integer duration, String startTime) {
        this.name = name;
        this.description = description;
        this.status = status;
        if (duration == null) {
            this.duration = Duration.ZERO;
        } else {
            setDurationInt(duration);
        }
        setStartTimeString(startTime);
    }

    public Task(Integer id, String name, String description, Status status, Integer duration, String startTime) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.status = status;
        if (duration == null) {
            this.duration = Duration.ZERO;
        } else {
            setDurationInt(duration);
        }
        setStartTimeString(startTime);
    }

    public Task(Integer id, String name, String description, Status status, Duration duration,
                LocalDateTime startTime) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.status = status;
        if (duration == null) {
            this.duration = Duration.ZERO;
        } else {
            this.duration = duration;
        }
        this.startTime = startTime;
    }

    public Task(Integer id, String name, String description, Status status) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.status = status;
    }

    public Task(String name, String description, Status status) {
        this.name = name;
        this.description = description;
        this.status = status;
    }

    public Task() {
    }

    public Task(Task originalTask) {
        this(originalTask.getName(), originalTask.getDescription(), originalTask.getStatus(),
                Integer.parseInt(String.valueOf(originalTask.getDuration().toMinutes())),
                originalTask.getStartTime().toString());
        this.id = originalTask.getId();
    }

    public TypeTask getType() {
        return TypeTask.TASK;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return Objects.equals(id, task.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, description, id, status);
    }

    @Override
    public String toString() {
        return getId() +
                "," + getType() +
                "," + getName() +
                "," + getStatus() +
                "," + getDescription() +
                "," + getDurationMinutes() +
                "," + getStartTime();
    }

    public LocalDateTime getEndTime() {
        if (this.startTime != null) {
            return this.startTime.plusMinutes(this.duration.toMinutes());
        }
        return null;
    }

    public LocalDateTime getStartTime() {
        Optional<LocalDateTime> startTimeOPtional = Optional.ofNullable(this.startTime);
        return startTimeOPtional.orElse(null);
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public void setStartTimeString(String startTime) {
        if (startTime != null) {
            this.startTime = LocalDateTime.parse(startTime, Constant.dateFormat);
        } else {
            this.startTime = null;
        }
    }

    public Duration getDuration() {
        return this.duration;
    }

    public Integer getDurationMinutes() {
        if (this.duration == null) {
            return 0;
        } else {
            return (int) this.duration.toMinutes();
        }
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public void setDurationInt(Integer duration) {
        this.duration = Duration.ofMinutes(duration);
    }
}
