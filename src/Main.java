import tracker.Status;
import tracker.model.Epic;
import tracker.model.Subtask;
import tracker.model.Task;
import tracker.service.TaskManager;

public class Main {

    public static void main(String[] args) {
        System.out.println("Поехали!");
        TaskManager taskManager = new TaskManager();

        Task task = new Task(taskManager.getNextTaskId(), "Задача 1", "");
        taskManager.addTask(task);
        Task task2 = new Task(taskManager.getNextTaskId(), "Задача 2", "222");
        taskManager.addTask(task2);
        Epic epic = new Epic(taskManager.getNextTaskId(), "Эпик 1", "");
        taskManager.addTask(epic);
        Epic epic2 = new Epic(taskManager.getNextTaskId(), "Эпик 2", "");
        taskManager.addTask(epic2);
        Subtask subtask = new Subtask(taskManager.getNextTaskId(), "Подзадача 1 эпик 1", "", epic);
        taskManager.addTask(subtask);
        Subtask subtask2 = new Subtask(taskManager.getNextTaskId(), "Подзадача 2 эпик 1", "", epic);
        taskManager.addTask(subtask2);
        Subtask subtask3 = new Subtask(taskManager.getNextTaskId(), "Подзадача 3 эпик 2", "", epic2);
        taskManager.addTask(subtask3);
        System.out.println(taskManager.getAllTasks());
        System.out.println(taskManager.getAllEpics());
        System.out.println(taskManager.getAllSubtasks());
        subtask3.setStatus(Status.DONE);
        subtask2.setStatus(Status.IN_PROGRESS);
        taskManager.updateTask(subtask3);
        taskManager.updateTask(subtask2);
        System.out.println(taskManager.getAllTasks());
        System.out.println(taskManager.getAllEpics());
        System.out.println(taskManager.getAllSubtasks());
        System.out.println(taskManager.getTaskById(3));
        taskManager.deleteTaskById(3);
        taskManager.deleteTaskById(6);
        System.out.println(taskManager.getAllTasks());
        System.out.println(taskManager.getAllEpics());
        System.out.println(taskManager.getAllSubtasks());
    }
}
