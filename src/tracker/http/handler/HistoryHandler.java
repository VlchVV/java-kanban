package tracker.http.handler;

import com.sun.net.httpserver.HttpExchange;
import tracker.service.TaskManager;

import java.io.IOException;
import java.util.regex.Pattern;

public class HistoryHandler extends BaseHttpHandler {
    public HistoryHandler(TaskManager taskManager) {
        super(taskManager);
    }

    @Override
    protected void handleGetRequest(HttpExchange httpExchange) throws IOException {
        try {
            String path = httpExchange.getRequestURI().getPath();
            if (Pattern.matches("/history", path)) {
                sendText(httpExchange, gson.toJson(taskManager.getHistory()), 200);
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
}
