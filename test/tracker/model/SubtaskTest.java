package tracker.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static tracker.Status.*;

class SubtaskTest {
    private Epic epic;

    @BeforeEach
    public void beforeEach() {
        epic = new Epic(10, "Epic 1", "Easy Epic");
    }

    @Test
    public void shouldBeEqualWhenSubtaskIdsMatch() {
        Subtask subtask1 = new Subtask(10, "Subtask 1", "Easy Subtask", epic);
        Subtask subtask2 = new Subtask(10, "Subtask 2", "Difficult Subtask", epic);
        assertEquals(subtask1, subtask2, "Subtasks with the same Id should be equal");
    }

    @Test
    public void setStatus() {
        Subtask subtask = new Subtask(10, "Subtask 1", "Easy Subtask", epic);
        assertEquals(NEW, subtask.getStatus(), "Subtask status not NEW after creation");

        subtask.setStatus(IN_PROGRESS);
        assertEquals(IN_PROGRESS, subtask.getStatus(), "Wrong status after change");

        subtask.setStatus(DONE);
        assertEquals(DONE, subtask.getStatus(), "Wrong status after change");
    }

    @Test
    public void setName() {
        Subtask subtask = new Subtask(10, "Subtask 1", "Easy Subtask", epic);
        assertEquals("Subtask 1", subtask.getName(), "Wrong Subtask name after creation");

        subtask.setName("Subtask 1 Altered");
        assertEquals("Subtask 1 Altered", subtask.getName(), "Wrong Subtask name after change");
    }

    @Test
    public void setDescription() {
        Subtask subtask = new Subtask(10, "Subtask 1", "Easy Subtask", epic);
        assertEquals("Easy Subtask", subtask.getDescription(), "Wrong Subtask description after creation");

        subtask.setDescription("Easy Subtask Altered");
        assertEquals("Easy Subtask Altered", subtask.getDescription(),
                "Wrong Subtask description after change");
    }
}