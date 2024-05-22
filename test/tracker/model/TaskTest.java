package tracker.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static tracker.Status.*;

class TaskTest {

    @Test
    public void shouldBeEqualWhenTaskIdsMatch() {
        Task task1 = new Task(10, "Task 1", "Easy Task");
        Task task2 = new Task(10, "Task 2", "Difficult Task");
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
}