package tracker.http.handler;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import tracker.http.adapter.DurationTypeAdapter;
import tracker.http.adapter.EpicAdapter;
import tracker.http.adapter.LocalDateTimeTypeAdapter;
import tracker.http.adapter.SubtaskAdapter;
import tracker.model.Epic;
import tracker.model.Subtask;
import tracker.service.TaskManager;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;

public class BaseHttpHandler implements HttpHandler {
    protected final TaskManager taskManager;
    private final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;
    protected Gson gson;

    public BaseHttpHandler(TaskManager taskManager) {
        this.taskManager = taskManager;
        gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeTypeAdapter())
                .registerTypeAdapter(Duration.class, new DurationTypeAdapter())
                .registerTypeAdapter(Epic.class, new EpicAdapter())
                .registerTypeAdapter(Subtask.class, new SubtaskAdapter(taskManager))
                .create();
    }

    protected void sendMethodUnknown(HttpExchange httpExchange) throws IOException {
        sendText(httpExchange, "Некорректный метод!", 405);
    }

    protected void sendInternalError(HttpExchange httpExchange) throws IOException {
        sendText(httpExchange, "Ошибка обработки запроса", 500);
    }

    protected void sendNotFound(HttpExchange httpExchange) throws IOException {
        sendText(httpExchange, "Не найдено!", 404);
    }

    protected void sendIntersection(HttpExchange httpExchange) throws IOException {
        sendText(httpExchange, "Обнаружено пересение задач по времени!", 406);
    }

    protected void sendText(HttpExchange exchange, String text, int rCode) throws IOException {
        byte[] resp = text.getBytes(DEFAULT_CHARSET);
        exchange.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
        exchange.sendResponseHeaders(rCode, resp.length);
        exchange.getResponseBody().write(resp);
        exchange.close();
    }

    protected String readText(HttpExchange exchange) throws IOException {
        return new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
    }

    protected int parsePathId(String path) {
        try {
            return Integer.parseInt(path);
        } catch (NumberFormatException exception) {
            return -1;
        }
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        String method = httpExchange.getRequestMethod();
        System.out.println("Получен запрос " + method + " " + httpExchange.getRequestURI().getPath());

        switch (method) {
            case "POST":
                handlePostRequest(httpExchange);
                break;
            case "GET":
                handleGetRequest(httpExchange);
                break;
            case "DELETE":
                handleDeleteRequest(httpExchange);
                break;
            default:
                sendMethodUnknown(httpExchange);
        }

    }

    protected void handlePostRequest(HttpExchange httpExchange) throws IOException {
        sendMethodUnknown(httpExchange);
    }

    protected void handleGetRequest(HttpExchange httpExchange) throws IOException {
        sendMethodUnknown(httpExchange);
    }

    protected void handleDeleteRequest(HttpExchange httpExchange) throws IOException {
        sendMethodUnknown(httpExchange);
    }
}