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
        final Task task = new Task(1, "Test addTask", "Test addTask description");
        final Epic epic = new Epic(2, "Epic addEpic", "Epic addEpic description");

        // Add 1 task
        historyManager.add(task);
        assertEquals(1, historyManager.getHistory().size(), "Incorrect history size");

        // Add epic
        historyManager.add(epic);

        assertEquals(2, historyManager.getHistory().size(), "Incorrect history size");
        assertEquals(task, historyManager.getHistory().getFirst(), "Wrong first history element");
        assertEquals(epic, historyManager.getHistory().getLast(), "Wrong last history element");

        // Testing history after adding duple task
        historyManager.add(task);
        assertEquals(2, historyManager.getHistory().size(), "Incorrect history size");
        assertEquals(epic, historyManager.getHistory().getFirst(), "Wrong first history element");
        assertEquals(task, historyManager.getHistory().getLast(), "Wrong last history element");
    }

    @Test
    public void remove() {
        final Task task = new Task(1, "Test addTask", "Test addTask description");
        final Epic epic = new Epic(2, "Epic addEpic", "Epic addEpic description");

        historyManager.add(task);
        historyManager.add(epic);

        historyManager.remove(1);
        assertEquals(1, historyManager.getHistory().size(), "Incorrect history size");
        historyManager.remove(2);
        assertTrue(historyManager.getHistory().isEmpty(), "History should be empty after deleting all.");
    }
}