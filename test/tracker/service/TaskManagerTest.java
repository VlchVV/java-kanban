package tracker.service;

import org.junit.jupiter.api.Test;
import tracker.model.Epic;
import tracker.model.Subtask;
import tracker.model.Task;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static tracker.Status.DONE;
import static tracker.Status.NEW;

abstract class TaskManagerTest<T extends TaskManager> {
    public T taskManager;

    public void init(T taskManager) {
        this.taskManager = taskManager;
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
        assertEquals(task, tasks.getFirst(), "Task in list is not equal to original Task.");

        task.setName("updateTask");
        task.setDescription("Test updateTask description");
        task.setStatus(DONE);
        taskManager.updateTask(task);

        final Task updatedTask = taskManager.getTask(taskId);

        assertEquals("updateTask", updatedTask.getName(), "Incorrect updated Task name");
        assertEquals("Test updateTask description", updatedTask.getDescription(),
                "Incorrect updated Task description");
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
        assertEquals(epic, epics.getFirst(), "Epic in list is not equal to original Epic.");

        epic.setName("updateEpic");
        epic.setDescription("Test updateEpic description");
        epic.setStatus(DONE);
        taskManager.updateEpic(epic);

        final Epic updatedEpic = taskManager.getEpic(epicId);

        assertEquals("updateEpic", updatedEpic.getName(), "Incorrect updated Epic name");
        assertEquals("Test updateEpic description", updatedEpic.getDescription(),
                "Incorrect updated Epic description");
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
        assertEquals(subtask, subtasks.getFirst(), "Subtask in list is not equal to original Subtask.");

        subtask.setName("updateSubtasks");
        subtask.setDescription("Test updateSubtasks description");
        subtask.setStatus(DONE);
        taskManager.updateSubtask(subtask);

        final Subtask updatedSubtask = taskManager.getSubTask(subtaskId);

        assertEquals("updateSubtasks", updatedSubtask.getName(), "Incorrect updated Subtasks name");
        assertEquals("Test updateSubtasks description", updatedSubtask.getDescription(),
                "Incorrect updated Subtasks description");
        assertEquals(DONE, updatedSubtask.getStatus(), "Incorrect updated Subtasks status");

        taskManager.deleteAllSubtasks();
        assertTrue(taskManager.getAllSubtasks().isEmpty(), "Subtasks count is not correct after delete");
    }

    @Test
    void getPrioritizedTasks() {
        Task task1 = new Task(taskManager.getNextTaskId(), "Задача 1", "", Duration.ofDays(2),
                LocalDateTime.of(2022, 7, 17, 18, 30));
        taskManager.addTask(task1);
        Task task2 = new Task(taskManager.getNextTaskId(), "Задача 2", "", Duration.ofDays(2),
                LocalDateTime.of(2021, 7, 17, 18, 30));
        taskManager.addTask(task2);
        Task task3 = new Task(taskManager.getNextTaskId(), "Задача 3", "", Duration.ofDays(5),
                LocalDateTime.of(2021, 8, 17, 18, 30));
        taskManager.addTask(task3);

        ArrayList<Task> correctOrder = new ArrayList<>();
        correctOrder.add(task2);
        correctOrder.add(task3);
        correctOrder.add(task1);

        int i = 0;
        for (Task prioritizedTask : taskManager.getPrioritizedTasks()) {
            assertEquals(correctOrder.get(i), prioritizedTask);
            i++;
        }

        task3.setStartTime(LocalDateTime.of(2018, 8, 17, 18, 30));
        taskManager.updateTask(task3);

        correctOrder = new ArrayList<>();
        correctOrder.add(task3);
        correctOrder.add(task2);
        correctOrder.add(task1);

        i = 0;
        for (Task prioritizedTask : taskManager.getPrioritizedTasks()) {
            assertEquals(correctOrder.get(i), prioritizedTask);
            i++;
        }
    }

    @Test
    void overlappingTasks() {
        Task task = new Task(taskManager.getNextTaskId(), "Задача 1", "", Duration.ofDays(2),
                LocalDateTime.of(2022, 7, 17, 18, 30));
        taskManager.addTask(task);
        assertEquals(1, taskManager.getAllTasks().size(), "Tasks count is not correct");

        Task task2 = new Task(taskManager.getNextTaskId(), "Задача 2", "222", Duration.ofDays(2),
                LocalDateTime.of(2022, 7, 19, 18, 29));
        assertThrows(TaskOverlappingException.class, () -> taskManager.addTask(task2),
                "Tasks are overlapping");
        assertEquals(1, taskManager.getAllTasks().size(),
                "Tasks count is not correct. Tasks are overlapping");

        Task task3 = new Task(taskManager.getNextTaskId(), "Задача 3", "222", Duration.ofDays(2),
                LocalDateTime.of(2022, 7, 19, 18, 30));
        taskManager.addTask(task3);
        assertEquals(2, taskManager.getAllTasks().size(),
                "Tasks count is not correct. Tasks are not overlapping");

        Task updatedTask3 = new Task(task3.getId(), "Задача 3", "222", Duration.ofDays(2),
                LocalDateTime.of(2022, 7, 18, 18, 30));
        assertThrows(TaskOverlappingException.class, () -> taskManager.updateTask(updatedTask3),
                "Tasks are overlapping");

        assertEquals(LocalDateTime.of(2022, 7, 19, 18, 30),
                taskManager.getTask(task3.getId()).getStartTime(),
                "Incorrect start time - must not be update to overlapping value");

        task3 = new Task(task3.getId(), "Задача 3", "222", Duration.ofDays(2),
                LocalDateTime.of(2023, 7, 18, 18, 30));
        taskManager.updateTask(task3);

        assertEquals(LocalDateTime.of(2023, 7, 18, 18, 30),
                taskManager.getTask(task3.getId()).getStartTime(),
                "Incorrect start time - must be update to not overlapping value");

        final int epicId = taskManager.getNextTaskId();
        final Epic epic = new Epic(epicId, "Epic addSubtask", "Epic addSubtask description");
        taskManager.addEpic(epic);

        final int subtaskId = taskManager.getNextTaskId();
        Subtask subtask = new Subtask(subtaskId, "Subtask",
                "Subtask description", Duration.ofDays(2),
                LocalDateTime.of(2021, 1, 1, 10, 30), epic);
        taskManager.addSubtask(subtask);

        assertEquals(1, taskManager.getAllSubtasks().size(),
                "Tasks count is not correct. New subtask is not overlapping");

        final int subtask2Id = taskManager.getNextTaskId();
        Subtask subtask2 = new Subtask(subtask2Id, "Subtask 2",
                "Subtask 2 description", Duration.ofDays(2),
                LocalDateTime.of(2022, 7, 17, 18, 30), epic);
        assertThrows(TaskOverlappingException.class, () -> taskManager.addSubtask(subtask2),
                "Tasks are overlapping");

        assertEquals(1, taskManager.getAllSubtasks().size(),
                "Tasks count is not correct. New subtask is overlapping with task");
    }
}
