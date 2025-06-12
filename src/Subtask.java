import java.util.Objects;

public class Subtask extends Task {
    private Integer idEpic = 0;

    protected Subtask(String name, String description, Status status) {
        super(name, description, status);
    }

    @Override
    public String toString() {
        return "Subtask{" +
                "idEpic=" + this.idEpic +
                ", name='" + this.getName() + '\'' +
                ", description='" + this.getDescription() + '\'' +
                ", id=" + this.getId() +
                ", status=" + this.getStatus() +
                '}';
    }

    public Integer getIdEpic() {
        return idEpic;
    }

    public void setIdEpic(Integer idEpic) {
        this.idEpic = idEpic;
    }
}
