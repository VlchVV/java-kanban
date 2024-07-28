package tracker.http;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tracker.http.adapter.DurationTypeAdapter;
import tracker.http.adapter.EpicAdapter;
import tracker.http.adapter.LocalDateTimeTypeAdapter;
import tracker.http.adapter.SubtaskAdapter;
import tracker.model.Epic;
import tracker.model.Subtask;
import tracker.model.Task;
import tracker.service.InMemoryTaskManager;
import tracker.service.TaskManager;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class HttpTaskManagerTasksTest {

    TaskManager manager = new InMemoryTaskManager();
    HttpTaskServer taskServer = new HttpTaskServer(manager);
    Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeTypeAdapter())
            .registerTypeAdapter(Duration.class, new DurationTypeAdapter())
            .registerTypeAdapter(Epic.class, new EpicAdapter())
            .registerTypeAdapter(Subtask.class, new SubtaskAdapter(manager))
            .create();

    public HttpTaskManagerTasksTest() throws IOException {
    }

    @BeforeEach
    public void setUp() {
        manager.deleteAllTasks();
        manager.deleteAllSubtasks();
        manager.deleteAllEpics();
        taskServer.start();
    }

    @AfterEach
    public void shutDown() {
        taskServer.stop();
    }

    @Test
    public void testAddTask() throws IOException, InterruptedException {
        Task task = new Task(manager.getNextTaskId(), "Test 2", "Testing task 2",
                Duration.ofMinutes(5), LocalDateTime.now());
        String taskJson = gson.toJson(task).replaceFirst("id", "id_old");

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks");
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(HttpRequest.BodyPublishers.ofString(taskJson)).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(201, response.statusCode());

        List<Task> tasksFromManager = manager.getAllTasks();

        assertNotNull(tasksFromManager, "No task returned");
        assertEquals(1, tasksFromManager.size(), "Wrong tasks count");
        assertEquals("Test 2", tasksFromManager.getFirst().getName(), "Wrong task name");
    }

    @Test
    public void testGetAllTasks() throws IOException, InterruptedException {
        int id = manager.getNextTaskId();
        Task task = new Task(id, "Test 2", "Testing task 2",
                Duration.ofMinutes(5), LocalDateTime.now());

        manager.addTask(task);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
    }

    @Test
    public void testGetTaskByCorrectId() throws IOException, InterruptedException {
        int id = manager.getNextTaskId();
        Task task = new Task(id, "Test 2", "Testing task 2",
                Duration.ofMinutes(5), LocalDateTime.now());

        manager.addTask(task);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/" + id);
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
    }

    @Test
    public void testGetTaskByIncorrectId() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/0");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(404, response.statusCode());
    }

    @Test
    public void testDeleteTaskByIncorrectId() throws IOException, InterruptedException {
        int id = manager.getNextTaskId();
        Task task = new Task(id, "Test 2", "Testing task 2",
                Duration.ofMinutes(5), LocalDateTime.now());

        manager.addTask(task);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/" + id);
        HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        assertEquals(0, manager.getAllTasks().size(), "Wrong tasks count");
    }

    /////

    @Test
    public void testAddEpicAndSubtask() throws IOException, InterruptedException {
        Epic epic = new Epic(manager.getNextTaskId(), "Test 1", "Testing epic 1");
        String epicJson = gson.toJson(epic).replaceFirst("id", "id_old");

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/epics");
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(HttpRequest.BodyPublishers.ofString(epicJson)).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(201, response.statusCode());

        List<Epic> tasksFromManager = manager.getAllEpics();

        assertNotNull(tasksFromManager, "No task returned");
        assertEquals(1, tasksFromManager.size(), "Wrong tasks count");
        assertEquals("Test 1", tasksFromManager.getFirst().getName(), "Wrong task name");

        Subtask subtask = new Subtask(manager.getNextTaskId(), "Test 2", "Testing task 2",
                Duration.ofMinutes(5), LocalDateTime.now(), epic);
        String subtaskJson = gson.toJson(subtask).replaceFirst("id", "id_old");

        url = URI.create("http://localhost:8080/subtasks");
        request = HttpRequest.newBuilder().uri(url).POST(HttpRequest.BodyPublishers.ofString(subtaskJson)).build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(201, response.statusCode());

        List<Subtask> subtasksFromManager = manager.getAllSubtasks();

        assertNotNull(tasksFromManager, "No task returned");
        assertEquals(1, subtasksFromManager.size(), "Wrong tasks count");
        assertEquals("Test 2", subtasksFromManager.getFirst().getName(), "Wrong task name");
    }

    @Test
    public void testGetAllEpicsAndSubtasks() throws IOException, InterruptedException {
        int epicId = manager.getNextTaskId();
        Epic epic = new Epic(epicId, "Test 1", "Testing epic 1");
        manager.addTask(epic);

        int subtaskId = manager.getNextTaskId();
        Subtask subtask = new Subtask(subtaskId, "Test 2", "Testing task 2",
                Duration.ofMinutes(5), LocalDateTime.now(), epic);
        manager.addSubtask(subtask);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/epics");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());

        url = URI.create("http://localhost:8080/subtasks");
        request = HttpRequest.newBuilder().uri(url).GET().build();

        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
    }

    @Test
    public void testGetEpicsAndSubtasksByCorrectId() throws IOException, InterruptedException {
        int epicId = manager.getNextTaskId();
        Epic epic = new Epic(epicId, "Test 1", "Testing epic 1");
        manager.addEpic(epic);

        int subtaskId = manager.getNextTaskId();
        Subtask subtask = new Subtask(subtaskId, "Test 2", "Testing task 2",
                Duration.ofMinutes(5), LocalDateTime.now(), epic);
        manager.addSubtask(subtask);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/epics/" + epicId);
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());

        url = URI.create("http://localhost:8080/subtasks/" + subtaskId);
        request = HttpRequest.newBuilder().uri(url).GET().build();

        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());

        url = URI.create("http://localhost:8080/epics/" + epicId +"/subtasks");
        request = HttpRequest.newBuilder().uri(url).GET().build();

        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
    }

    @Test
    public void testGetEpicByIncorrectId() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/epics/0");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(404, response.statusCode());
    }

    @Test
    public void testGetSubtaskByIncorrectId() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/subtasks/0");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(404, response.statusCode());
    }

    @Test
    public void testDeleteEpicsAndSubtasksByIncorrectId() throws IOException, InterruptedException {
        int epicId = manager.getNextTaskId();
        Epic epic = new Epic(epicId, "Test 1", "Testing epic 1");
        manager.addEpic(epic);

        int subtaskId = manager.getNextTaskId();
        Subtask subtask = new Subtask(subtaskId, "Test 2", "Testing task 2",
                Duration.ofMinutes(5), LocalDateTime.now(), epic);
        manager.addSubtask(subtask);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/subtasks/" + subtaskId);
        HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        assertEquals(0, manager.getAllSubtasks().size(), "Wrong subtasks count");

        url = URI.create("http://localhost:8080/epics/" + epicId);
        request = HttpRequest.newBuilder().uri(url).DELETE().build();

        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        assertEquals(0, manager.getAllEpics().size(), "Wrong epics count");

    }
}
