package tracker.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tracker.model.Epic;
import tracker.model.Subtask;
import tracker.model.Task;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static tracker.Status.DONE;
import static tracker.Status.NEW;

class InMemoryTaskManagerTest {
    private InMemoryTaskManager taskManager;

    @BeforeEach
    public void beforeEach() {
        taskManager = new InMemoryTaskManager();
    }

    @Test
    public void getNextTaskId() {
        final int taskId = taskManager.getNextTaskId();
        final Task task = new Task(taskId, "Test getNextTaskId", "Test getNextTaskId description");
        taskManager.addTask(task);
        assertTrue(taskManager.getNextTaskId() > taskId, "Task Id did not increment.");
    }

    @Test
    public void getHistory() {
        assertTrue(taskManager.getHistory().isEmpty(), "History should be empty initially.");
    }

    @Test
    public void testTask() {
        final int taskId = taskManager.getNextTaskId();
        final Task task = new Task(taskId, "Test addTask", "Test addTask description");
        taskManager.addTask(task);

        final Task savedTask = taskManager.getTask(taskId);

        assertNotNull(savedTask, "Task not found.");
        assertEquals(task, savedTask, "Tasks not equal.");

        final List<Task> tasks = taskManager.getAllTasks();

        assertNotNull(tasks, "Tasks not returned");
        assertEquals(1, tasks.size(), "Tasks count is not correct");
        assertEquals(task, tasks.get(0), "Task in list is not equal to original Task.");

        task.setName("updateTask");
        task.setDescription("Test updateTask description");
        task.setStatus(DONE);
        taskManager.updateTask(task);

        final Task updatedTask = taskManager.getTask(taskId);

        assertEquals("updateTask", updatedTask.getName(), "Incorrect updated Task name");
        assertEquals("Test updateTask description", updatedTask.getDescription(), "Incorrect updated Task description");
        assertEquals(DONE, updatedTask.getStatus(), "Incorrect updated Task status");

        taskManager.deleteAllTasks();
        assertTrue(taskManager.getAllTasks().isEmpty(), "Tasks count is not correct after delete");
    }

    @Test
    public void testEpic() {
        final int epicId = taskManager.getNextTaskId();
        final Epic epic = new Epic(epicId, "Epic addEpic", "Epic addEpic description");
        taskManager.addEpic(epic);

        final Epic savedEpic = taskManager.getEpic(epicId);

        assertNotNull(savedEpic, "Epic not found.");
        assertEquals(epic, savedEpic, "Epics not equal.");

        final List<Epic> epics = taskManager.getAllEpics();

        assertNotNull(epics, "Epics not returned");
        assertEquals(1, epics.size(), "Epics count is not correct");
        assertEquals(epic, epics.get(0), "Epic in list is not equal to original Epic.");

        epic.setName("updateEpic");
        epic.setDescription("Test updateEpic description");
        epic.setStatus(DONE);
        taskManager.updateEpic(epic);

        final Epic updatedEpic = taskManager.getEpic(epicId);

        assertEquals("updateEpic", updatedEpic.getName(), "Incorrect updated Epic name");
        assertEquals("Test updateEpic description", updatedEpic.getDescription(), "Incorrect updated Epic description");
        assertEquals(NEW, updatedEpic.getStatus(), "Incorrect Epic status - should not be updated");

        taskManager.deleteAllEpics();
        assertTrue(taskManager.getAllEpics().isEmpty(), "Epics count is not correct after delete");
    }

    @Test
    public void testSubtask() {
        final int epicId = taskManager.getNextTaskId();
        final Epic epic = new Epic(epicId, "Epic addSubtask", "Epic addSubtask description");
        taskManager.addEpic(epic);

        final int subtaskId = taskManager.getNextTaskId();
        final Subtask subtask = new Subtask(subtaskId, "Subtask addSubtask", "Subtask addSubtask description", epic);
        taskManager.addSubtask(subtask);
        final Subtask savedSubtask = taskManager.getSubTask(subtaskId);

        assertNotNull(savedSubtask, "Subtask not found.");
        assertEquals(subtask, savedSubtask, "Subtasks not equal.");

        final List<Subtask> subtasks = taskManager.getAllSubtasks();

        assertNotNull(subtasks, "Subtasks not returned");
        assertEquals(1, subtasks.size(), "Subtasks count is not correct");
        assertEquals(subtask, subtasks.get(0), "Subtask in list is not equal to original Subtask.");

        subtask.setName("updateSubtasks");
        subtask.setDescription("Test updateSubtasks description");
        subtask.setStatus(DONE);
        taskManager.updateTask(subtask);

        final Subtask updatedSubtask = taskManager.getSubTask(subtaskId);

        assertEquals("updateSubtasks", updatedSubtask.getName(), "Incorrect updated Subtasks name");
        assertEquals("Test updateSubtasks description", updatedSubtask.getDescription(), "Incorrect updated Subtasks description");
        assertEquals(DONE, updatedSubtask.getStatus(), "Incorrect updated Subtasks status");

        taskManager.deleteAllSubtasks();
        assertTrue(taskManager.getAllSubtasks().isEmpty(), "Subtasks count is not correct after delete");
    }
}