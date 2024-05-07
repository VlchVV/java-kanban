package tracker.service;

import tracker.model.Epic;
import tracker.model.Subtask;
import tracker.model.Task;
import java.util.Collection;
import java.util.HashMap;

public class TaskManager {
    private final HashMap<Integer, Task> tasks = new HashMap<>();
    private final HashMap<Integer, Epic> epics = new HashMap<>();
    private final HashMap<Integer, Subtask> subtasks = new HashMap<>();
    private int nextTaskId = 1;

    public int getNextTaskId() {
        return nextTaskId;
    }

    public Collection<Task> getAllTasks() {
        return tasks.values();
    }

    public Collection<Epic> getAllEpics() {
        return epics.values();
    }

    public Collection<Subtask> getAllSubtasks() {
        return subtasks.values();
    }

    public Task getTaskById(int id) {
        if (tasks.containsKey(id)) {
            return tasks.get(id);
        } else if (epics.containsKey(id)) {
            return epics.get(id);
        } else if (subtasks.containsKey(id)) {
            return subtasks.get(id);
        }
        return null;
    }

    public void addTask(Task task) {
        tasks.put(task.getId(), task);
        nextTaskId++;
    }

    public void addTask(Epic epic) {
        epics.put(epic.getId(), epic);
        nextTaskId++;
    }

    public void addTask(Subtask subtask) {
        subtasks.put(subtask.getId(), subtask);
        subtask.getEpic().addSubtask(subtask);
        subtask.getEpic().updateStatus();
        nextTaskId++;
    }

    public void updateTask(Task task) {
        tasks.put(task.getId(), task);
    }

    public void updateTask(Epic epic) {
        epics.put(epic.getId(), epic);
    }

    public void updateTask(Subtask subtask) {
        subtasks.put(subtask.getId(), subtask);
        subtask.getEpic().updateStatus();
    }

    public void deleteTaskById(int id) {
        if (tasks.containsKey(id)) {
            tasks.remove(id);
        } else if (epics.containsKey(id)) {
            // remove subtasks for deleted epic
            for (Subtask subtask : epics.get(id).getSubtasks()) {
                subtasks.remove(subtask.getId());
            }
            epics.remove(id);
        } else if (subtasks.containsKey(id)) {
            // update status for epic of deleted subtask
            subtasks.get(id).getEpic().updateStatus();
            subtasks.remove(id);
        }
    }

    public Collection<Subtask> getEpicSubTasks(Epic epic) {
        return epic.getSubtasks();
    }

    public void deleteAllTasks() {
        tasks.clear();
    }

    public void deleteAllSubtasks() {
        subtasks.clear();
        for (Epic epic : epics.values()) {
            epic.updateStatus();
        }
    }

    public void deleteAllEpics() {
        epics.clear();
        subtasks.clear();
    }

    public void deleteAll() {
        tasks.clear();
        subtasks.clear();
        epics.clear();
    }
}
