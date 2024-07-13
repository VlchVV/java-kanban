package tracker.service;

import tracker.model.Epic;
import tracker.model.Subtask;
import tracker.model.Task;

import java.util.*;

public class InMemoryTaskManager implements TaskManager {
    private final Map<Integer, Task> tasks = new HashMap<>();
    private final Map<Integer, Epic> epics = new HashMap<>();
    private final Map<Integer, Subtask> subtasks = new HashMap<>();
    private final HistoryManager historyManager = Managers.getDefaultHistory();
    private final TreeSet<Task> prioritizedTasks = new TreeSet<>(Comparator.comparing(Task::getStartTime));
    protected int nextTaskId = 1;

    @Override
    public int getNextTaskId() {
        return nextTaskId;
    }

    @Override
    public List<Task> getAllTasks() {
        return new ArrayList<>(tasks.values());
    }

    @Override
    public List<Epic> getAllEpics() {
        return new ArrayList<>(epics.values());
    }

    @Override
    public List<Subtask> getAllSubtasks() {
        return new ArrayList<>(subtasks.values());
    }

    @Override
    public List<Task> getHistory() {
        return new ArrayList<>(historyManager.getHistory());
    }

    @Override
    public Task getTask(int id) {
        if (tasks.containsKey(id)) {
            historyManager.add(tasks.get(id));
            return tasks.get(id);
        } else {
            return null;
        }
    }

    @Override
    public Epic getEpic(int id) {
        if (epics.containsKey(id)) {
            historyManager.add(epics.get(id));
            return epics.get(id);
        } else {
            return null;
        }
    }

    @Override
    public Subtask getSubTask(int id) {
        if (subtasks.containsKey(id)) {
            historyManager.add(subtasks.get(id));
            return subtasks.get(id);
        } else {
            return null;
        }
    }

    @Override
    public void addTask(Task task) {
        if (isNotOverlapping(task)) {
            tasks.put(task.getId(), task);
            if (task.getStartTime() != null) {
                prioritizedTasks.add(task);
            }
            nextTaskId++;
        }
    }

    @Override
    public void addEpic(Epic epic) {
        epics.put(epic.getId(), epic);
        nextTaskId++;
    }

    @Override
    public void addSubtask(Subtask subtask) {
        if (isNotOverlapping(subtask)) {
            subtasks.put(subtask.getId(), subtask);
            if (subtask.getStartTime() != null) {
                prioritizedTasks.add(subtask);
            }
            subtask.getEpic().addSubtask(subtask);
            subtask.getEpic().updateEpic();
            nextTaskId++;
        }
    }

    @Override
    public void updateTask(Task task) {
        if (isNotOverlapping(task)) {
            tasks.put(task.getId(), task);
            prioritizedTasks.remove(task);
            if (task.getStartTime() != null) {
                prioritizedTasks.add(task);
            }
        }
    }

    @Override
    public void updateEpic(Epic epic) {
        epics.put(epic.getId(), epic);
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        if (isNotOverlapping(subtask)) {
            subtasks.put(subtask.getId(), subtask);
            subtask.getEpic().updateEpic();
            prioritizedTasks.remove(subtask);
            if (subtask.getStartTime() != null) {
                prioritizedTasks.add(subtask);
            }
        }
    }

    @Override
    public void deleteTaskById(int id) {
        if (tasks.containsKey(id)) {
            tasks.remove(id);
        } else if (epics.containsKey(id)) {
            // remove subtasks for deleted epic
            epics.get(id).getSubtasks().stream()
                    .map(Subtask::getId).forEach(integer -> {
                        subtasks.remove(integer);
                        historyManager.remove(integer);
                    });
            epics.remove(id);
        } else if (subtasks.containsKey(id)) {
            // update fields for epic of deleted subtask
            subtasks.get(id).getEpic().updateEpic();
            subtasks.remove(id);
        }
        historyManager.remove(id);
    }

    @Override
    public ArrayList<Subtask> getEpicSubTasks(Epic epic) {
        return new ArrayList<>(epic.getSubtasks());
    }

    @Override
    public void deleteAllTasks() {
        tasks.clear();
    }

    @Override
    public void deleteAllSubtasks() {
        subtasks.clear();
        epics.values().forEach(Epic::updateEpic);
    }

    @Override
    public void deleteAllEpics() {
        epics.clear();
        subtasks.clear();
    }

    private boolean areTasksOverlapping(Task task1, Task task2) {
        return (task2.getStartTime().isAfter(task1.getStartTime()) && task2.getStartTime().isBefore(task1.getEndTime()))
                || (task1.getStartTime().isAfter(task2.getStartTime()) && task1.getStartTime().isBefore(task2.getEndTime()))
                || (task1.getStartTime().isEqual(task2.getStartTime()));
    }

    private boolean isNotOverlapping(Task newTask) {
        if (newTask.getStartTime() == null) {
            return true;
        } else {
            return prioritizedTasks.stream()
                    .filter(task -> !task.equals(newTask))
                    .noneMatch(task -> areTasksOverlapping(newTask, task));
        }
    }

    @Override
    public TreeSet<Task> getPrioritizedTasks() {
        return prioritizedTasks;
    }
}
