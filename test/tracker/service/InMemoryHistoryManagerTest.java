package tracker.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tracker.model.Epic;
import tracker.model.Task;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class InMemoryHistoryManagerTest {
    private InMemoryHistoryManager historyManager;

    @BeforeEach
    void beforeEach() {
        historyManager = new InMemoryHistoryManager();
    }

    @Test
    public void shouldBeEmptyInitially() {
        assertTrue(historyManager.getHistory().isEmpty(), "History should be empty initially.");
    }

    @Test
    public void add() {
        final int HISTORY_MAX_SIZE = historyManager.getHistoryMaxSize();
        final Task task = new Task(1, "Test addTask", "Test addTask description");
        final Epic epic = new Epic(2, "Epic addEpic", "Epic addEpic description");

        // Add 1
        historyManager.add(task);
        assertEquals(1, historyManager.getHistory().size(), "Incorrect history size");

        // Add epic HISTORY_MAX_SIZE - 1 times
        for (int i = 1; i < HISTORY_MAX_SIZE; i++) {
            historyManager.add(epic);
        }

        assertEquals(HISTORY_MAX_SIZE, historyManager.getHistory().size(), "Incorrect history size");
        assertEquals(task, historyManager.getHistory().getFirst(), "Wrong first history element");
        assertEquals(epic, historyManager.getHistory().getLast(), "Wrong last history element");

        // Testing history after reaching Max
        historyManager.add(epic);

        assertEquals(HISTORY_MAX_SIZE, historyManager.getHistory().size(), "Incorrect history size after reaching Max");
        assertEquals(epic, historyManager.getHistory().getFirst(), "Wrong first history element after reaching Max");
    }
}