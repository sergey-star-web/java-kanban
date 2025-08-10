package com.yandex.taskmanagerapp.model;

import com.yandex.taskmanagerapp.enums.Status;

public class Subtask extends Task {
    private Integer idEpic = 0;

    public Subtask(String name, String description, Status status) {
        super(name, description, status);
    }

    public Subtask(Integer id, String name, String description, Status status, Integer idEpic) {
        super(id, name, description, status);
        this.idEpic = idEpic;
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
        return getId() +
                "," + getType() +
                "," + getName() +
                "," + getStatus() +
                "," + getDescription() +
                "," + getIdEpic();
    }

    public Integer getIdEpic() {
        return idEpic;
    }

    public void setIdEpic(Integer idEpic) {
        this.idEpic = idEpic;
    }
}
