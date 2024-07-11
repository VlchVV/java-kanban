package tracker.model;

import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static tracker.Status.*;

class TaskTest {

    @Test
    public void shouldBeEqualWhenTaskIdsMatch() {
        Task task1 = new Task(10, "Task 1", "Easy Task", Duration.ofDays(2),
                LocalDateTime.of(2022, 7, 17, 18, 30));
        Task task2 = new Task(10, "Task 2", "Difficult Task", Duration.ofDays(10),
                LocalDateTime.of(2024, 7, 17, 18, 30));
        assertEquals(task1, task2, "Tasks with the same Id should be equal");
    }

    @Test
    public void setStatus() {
        Task task = new Task(10, "Task 1", "Easy Task");
        assertEquals(NEW, task.getStatus(), "Task status not NEW after creation");

        task.setStatus(IN_PROGRESS);
        assertEquals(IN_PROGRESS, task.getStatus(), "Wrong status after change");

        task.setStatus(DONE);
        assertEquals(DONE, task.getStatus(), "Wrong status after change");
    }

    @Test
    public void setName() {
        Task task = new Task(10, "Task 1", "Easy Task");
        assertEquals("Task 1", task.getName(), "Wrong Task name after creation");

        task.setName("Task 1 Altered");
        assertEquals("Task 1 Altered", task.getName(), "Wrong Task name after change");
    }

    @Test
    public void setDescription() {
        Task task = new Task(10, "Task 1", "Easy Task");
        assertEquals("Easy Task", task.getDescription(), "Wrong Task description after creation");

        task.setDescription("Easy Task Altered");
        assertEquals("Easy Task Altered", task.getDescription(),
                "Wrong Task description after change");
    }

    @Test
    public void getEndTime() {
        Task task1 = new Task(10, "Task 1", "Easy Task", Duration.ofDays(2),
                LocalDateTime.of(2022, 7, 17, 18, 30));
        assertEquals(LocalDateTime.of(2022, 7, 19, 18, 30), task1.getEndTime(),
                "Incorrect End Time after creation");
        task1.setDuration(Duration.ofMinutes(3));
        assertEquals(LocalDateTime.of(2022, 7, 17, 18, 33), task1.getEndTime(),
                "Incorrect End Time after duration update");
        task1.setStartTime(LocalDateTime.of(2025, 7, 17, 19, 0));
        assertEquals(LocalDateTime.of(2025, 7, 17, 19, 3), task1.getEndTime(),
                "Incorrect End Time after Start Time update");
        assertEquals(LocalDateTime.of(2025, 7, 17, 19, 0), task1.getStartTime(),
                "Incorrect Start Time after Start Time update");
        assertEquals(Duration.ofMinutes(3), task1.getDuration(),
                "Incorrect Duration after Start Time update");
    }
}