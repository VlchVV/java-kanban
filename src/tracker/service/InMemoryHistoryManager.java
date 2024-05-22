package tracker.service;

import tracker.model.Task;

import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager{
    private static final int HISTORY_MAX_SIZE = 10;
    private final List<Task> taskHistory = new ArrayList<>();

    @Override
    public void add(Task task) {
        if (taskHistory.size() == HISTORY_MAX_SIZE) {
            taskHistory.removeFirst();
        }
        taskHistory.add(task);
    }

    @Override
    public List<Task> getHistory() {
        return taskHistory;
    }

    public final int getHistoryMaxSize() {
        return HISTORY_MAX_SIZE;
    }
}
