package tracker.http.handler;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sun.net.httpserver.HttpExchange;
import tracker.model.Epic;
import tracker.service.NotFoundException;
import tracker.service.TaskManager;
import tracker.service.TaskOverlappingException;

import java.io.IOException;
import java.util.regex.Pattern;

public class EpicHandler extends TaskHandler {
    public EpicHandler(TaskManager taskManager, String handlerRoot) {
        super(taskManager, handlerRoot);
    }

    @Override
    protected void handlePostRequest(HttpExchange httpExchange) throws IOException {
        try {
            String path = httpExchange.getRequestURI().getPath();
            if (Pattern.matches(handlerRoot, path)) {
                String response;
                JsonObject jsonObject = JsonParser.parseString(readText(httpExchange)).getAsJsonObject();
                JsonElement jsonId = jsonObject.get("id");
                Epic epic;

                if (jsonId == null) {
                    epic = new Epic(taskManager.getNextTaskId(),
                            jsonObject.get("name").getAsString(),
                            jsonObject.get("description").getAsString());
                    taskManager.addEpic(epic);
                    response = "Эпик добавлен c id = " + epic.getId();
                } else {
                    epic = new Epic(jsonObject.get("id").getAsInt(),
                            jsonObject.get("name").getAsString(),
                            jsonObject.get("description").getAsString());
                    taskManager.updateEpic(epic);
                    response = "Эпик обновлен c id = " + epic.getId();
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
            if (Pattern.matches("/epics/\\d+/subtasks", path)) {
                String pathId = path.replaceFirst("/epics/", "")
                        .replaceFirst("/subtasks", "");
                int taskId = parsePathId(pathId);
                if (taskId != -1) {
                    sendText(httpExchange, getEpicSubTasks(taskId), 200);
                } else {
                    System.out.println("Получен некорретный id: " + pathId);
                    sendMethodUnknown(httpExchange);
                }
            } else {
                super.handleGetRequest(httpExchange);
            }
        } catch (NotFoundException exception) {
            sendNotFound(httpExchange);
        } catch (Exception exception) {
            exception.printStackTrace();
        } finally {
            httpExchange.close();
        }
    }

    protected String getEpicSubTasks(int taskId) {
        Epic epic = taskManager.getEpic(taskId);
        return gson.toJson(taskManager.getEpicSubTasks(epic));
    }

    @Override
    protected String getAllTasks() {
        return gson.toJson(taskManager.getAllEpics());
    }

    @Override
    protected String getTask(int taskId) {
        return gson.toJson(taskManager.getEpic(taskId));
    }
}
