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
                "," + (task.getDuration() == null ? "" : task.getDuration()) +
                "," + (task.getStartTime() == null ? "" : task.getStartTime()) +
                "," /* no epic */;
    }

    public static String toCSV(Subtask subtask) {
        return subtask.getId() +
                "," + TaskTypes.SUBTASK +
                "," + subtask.getName() +
                "," + subtask.getStatus() +
                "," + subtask.getDescription() +
                "," + (subtask.getDuration() == null ? "" : subtask.getDuration()) +
                "," + (subtask.getStartTime() == null ? "" : subtask.getStartTime()) +
                "," + subtask.getEpic().getId();
    }

    public static String toCSV(Epic epic) {
        return epic.getId() +
                "," + TaskTypes.EPIC +
                "," + epic.getName() +
                "," + epic.getStatus() +
                "," + epic.getDescription() +
                "," /* no duration */ +
                "," /* no start date */ +
                "," /* no epic */;
    }
}
