package tracker.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static tracker.Status.*;

class EpicTest {
    @Test
    public void shouldEqualWhenEpicIdsMatch() {
        Epic epic1 = new Epic(10, "Epic 1", "Mega Epic");
        Epic epic2 = new Epic(10, "Epic 2", "Mega Epic");
        assertEquals(epic1, epic2, "Epics with the same Id should be equal.");
    }

    @Test
    public void setName() {
        Epic epic = new Epic(10, "Epic 1", "Easy Epic");
        assertEquals("Epic 1", epic.getName(), "Wrong Epic name after creation.");

        epic.setName("Epic 1 Altered");
        assertEquals("Epic 1 Altered", epic.getName(), "Wrong Epic name after change.");
    }

    @Test
    public void setDescription() {
        Epic epic = new Epic(10, "Epic 1", "Easy Epic");
        assertEquals("Easy Epic", epic.getDescription(), "Wrong Epic description after creation.");

        epic.setDescription("Easy Epic Altered");
        assertEquals("Easy Epic Altered", epic.getDescription(),
                "Wrong Epic description after change.");
    }

    @Test
    public void updateStatus() {
        Epic epic = new Epic(10, "Epic 1", "Easy Epic");

        assertEquals(NEW, epic.getStatus(), "Epic status not NEW after creation.");

        Subtask subtask1 = new Subtask(11, "ST1", "Desc", epic);
        epic.addSubtask(subtask1);

        subtask1.setStatus(IN_PROGRESS);
        epic.updateStatus();

        assertEquals(IN_PROGRESS, epic.getStatus(), "Epic status is incorrect after subtask in progress.");

        Subtask subtask2 = new Subtask(12, "ST12", "Desc", epic);
        epic.addSubtask(subtask2);
        subtask2.setStatus(DONE);

        epic.updateStatus();
        assertEquals(IN_PROGRESS, epic.getStatus(),
                "Epic status is incorrect: 1 subtask in progress, 1 subtask done.");

        subtask1.setStatus(DONE);
        epic.updateStatus();
        assertEquals(DONE, epic.getStatus(), "Epic status is incorrect after all subtasks are done.");
    }

    @Test
    public void getSubtasks() {
        Epic epic = new Epic(10, "Epic 1", "Easy Epic");
        assertTrue(epic.getSubtasks().isEmpty(), "Subtask list should be empty initially.");

        Subtask subtask1 = new Subtask(11, "ST1", "Desc", epic);
        epic.addSubtask(subtask1);

        Subtask subtask2 = new Subtask(12, "ST12", "Desc", epic);
        epic.addSubtask(subtask2);

        assertEquals(2, epic.getSubtasks().size(), "Incorrect Subtask list size.");
        assertEquals(subtask1, epic.getSubtasks().getFirst(),
                "Subtask 1 returned from Epic is not the same as original.");
        assertEquals(subtask2, epic.getSubtasks().get(1),
                "Subtask 2 Subtask returned from Epic is not the same as original.");
    }
}