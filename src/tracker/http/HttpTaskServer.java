package tracker.http;

import com.sun.net.httpserver.HttpServer;
import tracker.http.handler.*;
import tracker.service.Managers;
import tracker.service.TaskManager;

import java.io.IOException;
import java.net.InetSocketAddress;

public class HttpTaskServer {

    private static final int PORT = 8080;
    private static HttpServer server;
    private final TaskManager taskManager;

    public HttpTaskServer() throws IOException {
        this(Managers.getDefault());
    }

    public HttpTaskServer(TaskManager taskManager) throws IOException {
        this.taskManager = taskManager;

        server = HttpServer.create(new InetSocketAddress(PORT), 0);
        server.createContext("/tasks", new TaskHandler(taskManager, "/tasks"));
        server.createContext("/epics", new EpicHandler(taskManager, "/epics"));
        server.createContext("/subtasks", new SubtaskHandler(taskManager, "/subtasks"));
        server.createContext("/history", new HistoryHandler(taskManager));
        server.createContext("/prioritized", new PrioritizedHandler(taskManager));
    }

    public static void main(String[] args) throws IOException {
        HttpTaskServer taskServer = new HttpTaskServer();
        taskServer.start();
    }

    public TaskManager getTaskManager() {
        return taskManager;
    }

    public void start() {
        System.out.println("HTTP-сервер запущен на " + PORT + " порту!");
        server.start();
    }

    public void stop() {
        server.stop(0);
    }
}
