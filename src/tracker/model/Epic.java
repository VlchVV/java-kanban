package tracker.model;

import tracker.Status;

import java.util.ArrayList;
import java.util.List;

public class Epic extends Task {
    private final List<Subtask> subtasks = new ArrayList<>();
    public Epic(int id, String name, String description) {
        super(id, name, description);
    }

    @Override
    public void setStatus(Status status) {
        updateStatus();
    }

    @Override
    public String toString() {
        return "Epic{" +
                "subtasks.size=" + subtasks.size() +
                ", id=" + id +
                ", status=" + status +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                '}';
    }

    public void addSubtask(Subtask subtask) {
        subtasks.add(subtask);
    }

    public List<Subtask> getSubtasks() {
        return subtasks;
    }

    // Обновление статуса эпика в зависимости от статусов подзадач
    public void updateStatus() {
        // NEW if no subtasks
        if (subtasks.isEmpty()) {
            super.setStatus(Status.NEW);
            return;
        }

        // Checking status for all subtasks
        boolean allNew = true;
        boolean allDone = true;
        for (Subtask subtask : subtasks) {
            if (subtask.getStatus() != Status.NEW) {
                allNew = false;
            }
            if (subtask.getStatus() != Status.DONE) {
                allDone = false;
            }
            if (!allDone && !allNew) {
                break; // exit if already not NEW and not DONE
            }
        }

        if (allNew) {
            super.setStatus(Status.NEW);
        } else if (allDone) {
            super.setStatus(Status.DONE);
        } else {
            super.setStatus(Status.IN_PROGRESS);
        }
    }
}
