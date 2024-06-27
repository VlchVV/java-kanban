package tracker.service;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import tracker.model.Epic;
import tracker.model.Subtask;
import tracker.model.Task;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static tracker.Status.DONE;

class FileBackedTaskManagerTest {
    private static final String fileName =
            "/Users/valchevski/IdeaProjects/java-kanban/resources/FileBackedTaskManagerTest.txt";
    private FileBackedTaskManager taskManager;

    @AfterAll
    public static void afterAll() throws IOException {
        // clears file after finishing
        FileWriter fileWriter = new FileWriter(fileName, false);
        fileWriter.close();
    }

    @Test
    void loadFromFile() {
        taskManager = FileBackedTaskManager.loadFromFile(fileName);
        assertEquals(1, taskManager.getNextTaskId(), "Next Id must be 1 initially");
        assertTrue(taskManager.getAllTasks().isEmpty(), "Tasks should be empty initially.");
        assertTrue(taskManager.getAllEpics().isEmpty(), "Epics should be empty initially.");
        assertTrue(taskManager.getAllSubtasks().isEmpty(), "Subtasks should be empty initially.");

        final int taskId = taskManager.getNextTaskId();
        final Task task = new Task(taskId, "Test addTask", "Test addTask description");
        taskManager.addTask(task);

        taskManager = FileBackedTaskManager.loadFromFile(fileName);
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

        taskManager = FileBackedTaskManager.loadFromFile(fileName);
        final Task updatedTask = taskManager.getTask(taskId);

        assertEquals("updateTask", updatedTask.getName(), "Incorrect updated Task name");
        assertEquals("Test updateTask description", updatedTask.getDescription(), "Incorrect updated Task description");
        assertEquals(DONE, updatedTask.getStatus(), "Incorrect updated Task status");

        final int epicId = taskManager.getNextTaskId();
        final Epic epic = new Epic(epicId, "Epic addSubtask", "Epic addSubtask description");
        taskManager.addEpic(epic);

        final int subtaskId = taskManager.getNextTaskId();
        final Subtask subtask = new Subtask(subtaskId, "Subtask addSubtask", "Subtask addSubtask description", epic);
        taskManager.addSubtask(subtask);


        taskManager = FileBackedTaskManager.loadFromFile(fileName);
        final Subtask savedSubtask = taskManager.getSubTask(subtaskId);

        assertEquals(1, epic.getSubtasks().size(), "Wrong count of epic subtasks");
        assertNotNull(savedSubtask, "Subtask not found.");
        assertEquals(subtask, savedSubtask, "Subtasks not equal.");

        final List<Subtask> subtasks = taskManager.getAllSubtasks();

        assertNotNull(subtasks, "Subtasks not returned");
        assertEquals(1, subtasks.size(), "Subtasks count is not correct");
        assertEquals(subtask, subtasks.get(0), "Subtask in list is not equal to original Subtask.");

        subtask.setName("updateSubtasks");
        subtask.setDescription("Test updateSubtasks description");
        subtask.setStatus(DONE);
        taskManager.updateSubtask(subtask);

        taskManager = FileBackedTaskManager.loadFromFile(fileName);
        final Subtask updatedSubtask = taskManager.getSubTask(subtaskId);

        assertEquals("updateSubtasks", updatedSubtask.getName(), "Incorrect updated Subtasks name");
        assertEquals("Test updateSubtasks description", updatedSubtask.getDescription(), "Incorrect updated Subtasks description");
        assertEquals(DONE, updatedSubtask.getStatus(), "Incorrect updated Subtasks status");

        taskManager.deleteAllSubtasks();
        taskManager.deleteAllTasks();

        taskManager = FileBackedTaskManager.loadFromFile(fileName);
        assertTrue(taskManager.getAllSubtasks().isEmpty(), "Subtasks count is not correct after delete");
        assertTrue(taskManager.getAllTasks().isEmpty(), "Tasks count is not correct after delete");
    }
}