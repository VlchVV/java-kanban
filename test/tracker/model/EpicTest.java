package tracker.model;

import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
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
    public void updateEpic() {
        Epic epic = new Epic(10, "Epic 1", "Easy Epic");

        assertEquals(NEW, epic.getStatus(), "Epic status not NEW after creation.");

        Subtask subtask1 = new Subtask(11, "ST1", "Desc", epic);
        epic.addSubtask(subtask1);

        assertEquals(NEW, epic.getStatus(), "Epic status not NEW when all subtasks NEW.");

        subtask1.setStatus(IN_PROGRESS);
        epic.updateEpic();

        assertEquals(IN_PROGRESS, epic.getStatus(), "Epic status is incorrect after subtask in progress.");

        Subtask subtask2 = new Subtask(12, "ST12", "Desc", epic);
        epic.addSubtask(subtask2);
        subtask2.setStatus(DONE);

        epic.updateEpic();
        assertEquals(IN_PROGRESS, epic.getStatus(),
                "Epic status is incorrect: 1 subtask in progress, 1 subtask done.");

        subtask1.setStatus(DONE);
        epic.updateEpic();
        assertEquals(DONE, epic.getStatus(), "Epic status is incorrect after all subtasks are done.");
    }

    @Test
    public void duration() {
        Epic epic = new Epic(10, "Epic 1", "Easy Epic");
        assertNull(epic.getEndTime(), "Epic End Time must be empty with no subtasks");
        assertNull(epic.getStartTime(), "Epic Start Time must be empty with no subtasks");
        assertNull(epic.getDuration(), "Epic Duration must be empty with no subtasks");

        Subtask subtask = new Subtask(20, "Task 1", "Easy Task no time", epic);
        epic.addSubtask(subtask);
        epic.updateEpic();

        assertNull(epic.getEndTime(), "Epic End Time must be empty with subtask with no time");
        assertNull(epic.getStartTime(), "Epic Start Time must be empty with subtask with no time");
        assertNull(epic.getDuration(), "Epic Duration must be empty with subtask with no time");

        subtask = new Subtask(20, "Task 1", "Easy Task with time", Duration.ofDays(2),
                LocalDateTime.of(2022, 7, 17, 18, 30), epic);
        epic.addSubtask(subtask);
        epic.updateEpic();

        assertEquals(LocalDateTime.of(2022, 7, 17, 18, 30), epic.getStartTime(),
                "Incorrect Start Time after Start Time update");
        assertEquals(LocalDateTime.of(2022, 7, 19, 18, 30), epic.getEndTime(),
                "Incorrect End Time after Start Time update");
        assertEquals(Duration.ofDays(2), epic.getDuration(),
                "Incorrect Duration after Start Time update");

        Subtask subtask2 = new Subtask(30, "Task 2", "Easy Task", Duration.ofDays(2),
                LocalDateTime.of(2022, 8, 15, 18, 30), epic);
        epic.addSubtask(subtask2);
        epic.updateEpic();
        assertEquals(LocalDateTime.of(2022, 7, 17, 18, 30), epic.getStartTime(),
                "Incorrect Start Time after Start Time update");
        assertEquals(LocalDateTime.of(2022, 8, 17, 18, 30), epic.getEndTime(),
                "Incorrect End Time after Start Time update");
        assertEquals(Duration.ofDays(31), epic.getDuration(),
                "Incorrect Duration after Start Time update");
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