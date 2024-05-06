package tracker.service;

import tracker.model.Epic;
import tracker.model.Subtask;
import tracker.model.Task;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

public class TaskManager {
    private final HashMap<Integer, Task> tasks = new HashMap<>();
    private int nextTaskId = 1;

    public int getNextTaskId() {
        return nextTaskId;
    }

    public Collection<Task> getAllTasks() {
        return tasks.values();
    }

    public Task getTaskById(int id) {
        return tasks.get(id);
    }

    public Collection<Task> getTasksByType(Class<?> type) {
        Collection<Task> tasksByType = new ArrayList<>();
        for (Task task : tasks.values()) {
            if (type.isInstance(task)) {
                tasksByType.add(task);
            }
        }
        return tasksByType;
    }

    public void addTask(Task task) {
        tasks.put(task.getId(), task);
        if (task instanceof Subtask subtask) {
            subtask.getEpic().addSubtask(subtask);
            subtask.getEpic().updateStatus();
        }
        nextTaskId++;
    }

    public void updateTask(Task task) {
        tasks.put(task.getId(), task);
        if (task instanceof Subtask) {
            ((Subtask) task).getEpic().updateStatus();
        }
    }

    public void deleteTaskById(int id) {
        if (tasks.get(id) instanceof Epic epic) {
            // remove subtasks for deleted epic
            for (Subtask subtask : epic.getSubtasks()) {
                tasks.remove(subtask.getId());
            }
        } else if (tasks.get(id) instanceof Subtask subtask) {
            // update status for epic of deleted subtask
            subtask.getEpic().updateStatus();
        }
        tasks.remove(id);
    }

    public Collection<Subtask> getEpicSubTasks(Epic epic) {
        return epic.getSubtasks();
    }

    public void deleteAllTasks() {
        tasks.clear();
    }
}
