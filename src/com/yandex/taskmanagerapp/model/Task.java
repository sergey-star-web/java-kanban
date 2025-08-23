package com.yandex.taskmanagerapp.model;

import com.yandex.taskmanagerapp.constants.Constant;
import com.yandex.taskmanagerapp.enums.Status;
import com.yandex.taskmanagerapp.enums.TypeTask;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;

public class Task {
    private Integer id;
    private String name;
    private String description;
    private Status status;
    private Duration duration;
    private LocalDateTime startTime;
    private LocalDateTime endTime;

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
        calcEndTime();
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

    protected void calcEndTime() {
        if (this.startTime != null && this.duration != null) {
            this.endTime = this.startTime.plusMinutes(this.duration.toMinutes());
        } else if (this.startTime != null) {
            this.endTime = this.startTime;
        } else {
            this.endTime = null;
        }
    }

    public LocalDateTime getEndTime() {
        return this.endTime;
    }

    public LocalDateTime getStartTime() {
        LocalDateTime startTimeOPtional = this.startTime;
        return startTimeOPtional;
    }

    public String getStartTimeString() {
        LocalDateTime startTimeOPtional = this.startTime;
        return startTimeOPtional.format(Constant.dateFormat);
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
        calcEndTime();
    }

    public void setStartTimeString(String startTime) {
        if (startTime != null) {
            this.startTime = LocalDateTime.parse(startTime, Constant.dateFormat);
            calcEndTime();
        } else {
            this.startTime = null;
        }
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
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
        calcEndTime();
    }

    public void setDurationInt(Integer duration) {
        this.duration = Duration.ofMinutes(duration);
        calcEndTime();
    }
}
