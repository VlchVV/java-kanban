package tracker.model;

import tracker.Status;
import tracker.TaskTypes;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Epic extends Task {
    private final List<Subtask> subtasks = new ArrayList<>();
    protected LocalDateTime endTime;

    public Epic(int id, String name, String description) {
        super(id, name, description);
        this.type = TaskTypes.EPIC;
    }

    @Override
    public void setStatus(Status status) {
        updateStatus();
    }

    private void updateStartTime() {
        startTime = subtasks.stream()
                .filter(subtask -> subtask.getStartTime() != null)
                .min(Comparator.comparing(Subtask::getStartTime))
                .map(Subtask::getStartTime)
                .orElse(null);
    }

    @Override
    public LocalDateTime getEndTime() {
        return endTime;
    }

    private void updateEndTime() {
        endTime = subtasks.stream()
                .filter(subtask -> subtask.getEndTime() != null)
                .max(Comparator.comparing(Subtask::getEndTime))
                .map(Subtask::getEndTime)
                .orElse(null);
    }

    @Override
    public Duration getDuration() {
        if (endTime == null || startTime == null) {
            return null;
        } else {
            return Duration.between(startTime, endTime);
        }
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

    public void updateEpic() {
        updateStatus();
        updateStartTime();
        updateEndTime();
    }

    // Обновление статуса эпика в зависимости от статусов подзадач
    private void updateStatus() {
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
