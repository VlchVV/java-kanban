package tracker.http.handler;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sun.net.httpserver.HttpExchange;
import tracker.Status;
import tracker.model.Task;
import tracker.service.NotFoundException;
import tracker.service.TaskManager;
import tracker.service.TaskOverlappingException;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.regex.Pattern;

public class TaskHandler extends BaseHttpHandler {
    protected final String handlerRoot;

    public TaskHandler(TaskManager taskManager, String handlerRoot) {
        super(taskManager);
        this.handlerRoot = handlerRoot;
    }

    @Override
    protected void handlePostRequest(HttpExchange httpExchange) throws IOException {
        try {
            String path = httpExchange.getRequestURI().getPath();
            if (Pattern.matches(handlerRoot, path)) {
                String response;
                JsonObject jsonObject = JsonParser.parseString(readText(httpExchange)).getAsJsonObject();
                JsonElement jsonId = jsonObject.get("id");
                Task task;

                if (jsonId == null) {
                    task = new Task(taskManager.getNextTaskId(),
                            jsonObject.get("name").getAsString(),
                            jsonObject.get("description").getAsString(),
                            Duration.ofSeconds(jsonObject.get("duration").getAsInt()),
                            LocalDateTime.parse(jsonObject.get("startTime").getAsString(),
                                    DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss")));
                    task.setStatus(Status.valueOf(jsonObject.get("status").getAsString()));
                    taskManager.addTask(task);
                    response = "Задача добавлена c id = " + task.getId();
                } else {
                    task = new Task(jsonObject.get("id").getAsInt(),
                            jsonObject.get("name").getAsString(),
                            jsonObject.get("description").getAsString(),
                            gson.fromJson(jsonObject.get("duration"), Duration.class),
                            gson.fromJson(jsonObject.get("startTime"), LocalDateTime.class));
                    task.setStatus(Status.valueOf(jsonObject.get("status").getAsString()));
                    taskManager.updateTask(task);
                    response = "Задача обновлена c id = " + task.getId();
                }
                sendText(httpExchange, response, 201);
            } else {
                sendMethodUnknown(httpExchange);
            }
        } catch (NotFoundException exception) {
            sendNotFound(httpExchange);
        } catch (TaskOverlappingException exception) {
            sendIntersection(httpExchange);
        } catch (Exception exception) {
            sendInternalError(httpExchange);
            exception.printStackTrace();
        } finally {
            httpExchange.close();
        }
    }

    @Override
    protected void handleGetRequest(HttpExchange httpExchange) throws IOException {
        try {
            String path = httpExchange.getRequestURI().getPath();
            if (Pattern.matches(handlerRoot, path)) {
                sendText(httpExchange, getAllTasks(), 200);
            } else if (Pattern.matches(handlerRoot + "/\\d+$", path)) {
                String pathId = path.replaceFirst(handlerRoot + "/", "");
                int taskId = parsePathId(pathId);
                if (taskId != -1) {
                    sendText(httpExchange, getTask(taskId), 200);
                } else {
                    System.out.println("Получен некорретный id: " + pathId);
                    sendMethodUnknown(httpExchange);
                }
            } else {
                sendMethodUnknown(httpExchange);
            }
        } catch (NotFoundException exception) {
            sendNotFound(httpExchange);
        } catch (Exception exception) {
            sendInternalError(httpExchange);
            exception.printStackTrace();
        } finally {
            httpExchange.close();
        }
    }

    @Override
    protected void handleDeleteRequest(HttpExchange httpExchange) throws IOException {
        try {
            String path = httpExchange.getRequestURI().getPath();

            if (Pattern.matches(handlerRoot + "/\\d+$", path)) {
                String pathId = path.replaceFirst(handlerRoot + "/", "");
                int taskId = parsePathId(pathId);
                if (taskId != -1) {
                    deleteTask(taskId);
                    sendText(httpExchange, "", 200);
                } else {
                    System.out.println("Получен некорретный id: " + pathId);
                    sendMethodUnknown(httpExchange);
                }
            } else {
                sendMethodUnknown(httpExchange);
            }
        } catch (Exception exception) {
            sendInternalError(httpExchange);
            exception.printStackTrace();
        } finally {
            httpExchange.close();
        }
    }


    protected void deleteTask(int taskId) {
        taskManager.deleteTaskById(taskId);
        System.out.println("Удален таск по id: " + taskId);
    }

    protected String getAllTasks() {
        return gson.toJson(taskManager.getAllTasks());
    }

    protected String getTask(int taskId) {
        return gson.toJson(taskManager.getTask(taskId));
    }
}
