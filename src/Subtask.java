public class Subtask extends Task {
    private Integer idEpic = 0;

    public Subtask(String name, String description, Status status) {
        super(name, description, status);
        TaskManager.put(this);
    }

    @Override
    public String toString() {
        return "Subtask{" +
                "idEpic=" + this.idEpic +
                "name='" + this.getName() + '\'' +
                ", description='" + this.getDescription() + '\'' +
                ", id=" + this.getId() +
                ", status=" + this.getStatus() +
                '}';
    }
}
