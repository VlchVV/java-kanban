package tracker.model;

public class Subtask extends Task {
    private final Epic epic;

    public Subtask(int id, String name, String description, Epic epic) {
        super(id, name, description);
        this.epic = epic;
    }

    public Epic getEpic() {
        return epic;
    }

    @Override
    public String toString() {
        return "Subtask{" +
                "epic.getId()=" + epic.getId() +
                ", id=" + id +
                ", status=" + status +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
