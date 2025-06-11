import java.util.ArrayList;

public class Epic extends Task {
    private ArrayList<Subtask> subtasks;

    public Epic(String name, String description, Status status) {
        super(name, description, status);
        TaskManager.put(this);
    }

    @Override
    public String toString() {
        return "Epic{" +
                "subtasks=" + this.subtasks +
                "name='" + this.getName() + '\'' +
                ", description='" + this.getDescription() + '\'' +
                ", id=" + this.getId() +
                ", status=" + this.getStatus() +
                '}';
    }
}
