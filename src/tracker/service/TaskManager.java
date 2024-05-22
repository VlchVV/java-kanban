package tracker.service;

import tracker.model.Epic;
import tracker.model.Subtask;
import tracker.model.Task;

import java.util.ArrayList;

public interface TaskManager {
    int getNextTaskId();

    ArrayList<Task> getAllTasks();

    ArrayList<Epic> getAllEpics();

    ArrayList<Subtask> getAllSubtasks();

    ArrayList<Task> getHistory();

    Task getTask(int id);

    Epic getEpic(int id);

    Subtask getSubTask(int id);

    void addTask(Task task);

    void addEpic(Epic epic);

    void addSubtask(Subtask subtask);

    void updateTask(Task task);

    void updateEpic(Epic epic);

    void updateSubtask(Subtask subtask);

    void deleteTaskById(int id);

    ArrayList<Subtask> getEpicSubTasks(Epic epic);

    void deleteAllTasks();

    void deleteAllSubtasks();

    void deleteAllEpics();
}
