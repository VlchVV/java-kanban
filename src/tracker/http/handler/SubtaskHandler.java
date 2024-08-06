package tracker.http.handler;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sun.net.httpserver.HttpExchange;
import tracker.Status;
import tracker.model.Subtask;
import tracker.service.NotFoundException;
import tracker.service.TaskManager;
import tracker.service.TaskOverlappingException;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.regex.Pattern;

public class SubtaskHandler extends TaskHandler {
    public SubtaskHandler(TaskManager taskManager, String handlerRoot) {
        super(taskManager, handlerRoot);
    }

    @Override
    protected String getAllTasks() {
        return gson.toJson(taskManager.getAllSubtasks());
    }

    @Override
    protected void handlePostRequest(HttpExchange httpExchange) throws IOException {
        try {
            String path = httpExchange.getRequestURI().getPath();
            if (Pattern.matches(handlerRoot, path)) {
                String response;
                JsonObject jsonObject = JsonParser.parseString(readText(httpExchange)).getAsJsonObject();
                JsonElement jsonId = jsonObject.get("id");
                Subtask subtask;

                if (jsonId == null) {
                    subtask = new Subtask(taskManager.getNextTaskId(),
                            jsonObject.get("name").getAsString(),
                            jsonObject.get("description").getAsString(),
                            Duration.ofSeconds(jsonObject.get("duration").getAsInt()),
                            LocalDateTime.parse(jsonObject.get("startTime").getAsString(),
                                    DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss")),
                            taskManager.getEpic(jsonObject.get("epicId").getAsInt()));
                    subtask.setStatus(Status.valueOf(jsonObject.get("status").getAsString()));
                    taskManager.addSubtask(subtask);
                    response = "Задача добавлена c id = " + subtask.getId();
                } else {
                    subtask = new Subtask(jsonObject.get("id").getAsInt(),
                            jsonObject.get("name").getAsString(),
                            jsonObject.get("description").getAsString(),
                            gson.fromJson(jsonObject.get("duration"), Duration.class),
                            gson.fromJson(jsonObject.get("startTime"), LocalDateTime.class),
                            taskManager.getEpic(jsonObject.get("epicId").getAsInt()));
                    subtask.setStatus(Status.valueOf(jsonObject.get("status").getAsString()));
                    taskManager.updateSubtask(subtask);
                    response = "Задача обновлена c id = " + subtask.getId();
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
    protected String getTask(int taskId) {
        return gson.toJson(taskManager.getSubTask(taskId));
    }
}
