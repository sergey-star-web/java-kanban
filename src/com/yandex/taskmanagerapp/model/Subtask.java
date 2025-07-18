package com.yandex.taskmanagerapp.model;

import com.yandex.taskmanagerapp.service.Status;

public class Subtask extends Task {
    private Integer idEpic = 0;

    public Subtask(String name, String description, Status status) {
        super(name, description, status);
    }

    public Subtask() {
    }

    public Subtask(Subtask originalSubtask) {
        this(originalSubtask.getName(), originalSubtask.getDescription(), originalSubtask.getStatus());
        this.setId(originalSubtask.getId());
        this.idEpic = originalSubtask.getIdEpic();
    }

    @Override
    public String toString() {
        return "Subtask{" +
                "name='" + this.getName() + '\'' +
                ", description='" + this.getDescription() + '\'' +
                ", id=" + this.getId() +
                ", status=" + this.getStatus() +
                ", idEpic=" + this.idEpic +
                '}';
    }

    public Integer getIdEpic() {
        return idEpic;
    }

    public void setIdEpic(Integer idEpic) {
        this.idEpic = idEpic;
    }
}
