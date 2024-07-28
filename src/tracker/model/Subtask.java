package tracker.model;

import tracker.TaskTypes;

import java.time.Duration;
import java.time.LocalDateTime;

public class Subtask extends Task {
    private final Epic epic;

    public Subtask(int id, String name, String description, Epic epic) {
        super(id, name, description);
        this.epic = epic;
        this.type = TaskTypes.SUBTASK;
    }

    public Subtask(int id, String name, String description, Duration duration, LocalDateTime startTime, Epic epic) {
        super(id, name, description, duration, startTime);
        this.epic = epic;
        this.type = TaskTypes.SUBTASK;
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
