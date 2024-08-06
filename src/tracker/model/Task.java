package tracker.model;

import tracker.Status;
import tracker.TaskTypes;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;

public class Task {
    protected final int id;
    protected Status status = Status.NEW;
    protected String name;
    protected String description;
    protected Duration duration;
    protected LocalDateTime startTime;
    protected TaskTypes type;


    public Task(int id, String name, String description, Duration duration, LocalDateTime startTime) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.duration = duration;
        this.startTime = startTime;
        this.type = TaskTypes.TASK;
    }

    public Task(int id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.type = TaskTypes.TASK;
    }

    public int getId() {
        return id;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
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

    public LocalDateTime getEndTime() {
        if (duration == null || startTime == null) {
            return startTime;
        } else {
            return startTime.plus(duration);
        }
    }

    @Override
    public String toString() {
        return "Task{" + "id=" + id + ", status=" + status + ", name='" + name + '\'' + ", description='" + description + '\'' + '}';
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        Task task = (Task) object;
        return id == task.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public Duration getDuration() {
        return duration;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public TaskTypes getType() {
        return type;
    }
}
