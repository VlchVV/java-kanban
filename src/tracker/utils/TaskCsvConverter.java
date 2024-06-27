package tracker.utils;

import tracker.TaskTypes;
import tracker.model.Epic;
import tracker.model.Subtask;
import tracker.model.Task;

public class TaskCsvConverter {
    public static String toCSV(Task task) {
        return task.getId() +
                "," + TaskTypes.TASK +
                "," + task.getName() +
                "," + task.getStatus() +
                "," + task.getDescription() +
                ",";
    }

    public static String toCSV(Subtask subtask) {
        return subtask.getId() +
                "," + TaskTypes.SUBTASK +
                "," + subtask.getName() +
                "," + subtask.getStatus() +
                "," + subtask.getDescription() +
                "," + subtask.getEpic().getId();
    }

    public static String toCSV(Epic epic) {
        return epic.getId() +
                "," + TaskTypes.EPIC +
                "," + epic.getName() +
                "," + epic.getStatus() +
                "," + epic.getDescription() +
                ",";
    }
}
