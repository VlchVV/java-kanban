import tracker.model.Epic;
import tracker.model.Subtask;
import tracker.model.Task;
import tracker.service.Managers;
import tracker.service.TaskManager;

public class Main {

    public static void main(String[] args) {
        System.out.println("Поехали!");
        TaskManager taskManager = Managers.getDefault();
        Task task = new Task(taskManager.getNextTaskId(), "Задача 1", "");
        taskManager.addTask(task);
        Task task2 = new Task(taskManager.getNextTaskId(), "Задача 2", "222");
        taskManager.addTask(task2);
        Epic epic = new Epic(taskManager.getNextTaskId(), "Эпик 1", "");
        taskManager.addEpic(epic);
        Epic epic2 = new Epic(taskManager.getNextTaskId(), "Эпик 2", "");
        taskManager.addEpic(epic2);
        Subtask subtask = new Subtask(taskManager.getNextTaskId(), "Подзадача 1 эпик 1", "", epic);
        taskManager.addSubtask(subtask);
        Subtask subtask2 = new Subtask(taskManager.getNextTaskId(), "Подзадача 2 эпик 1", "", epic);
        taskManager.addSubtask(subtask2);
        Subtask subtask3 = new Subtask(taskManager.getNextTaskId(), "Подзадача 3 эпик 1", "", epic);
        taskManager.addSubtask(subtask3);
        taskManager.getTask(1);
        taskManager.getTask(2);
        taskManager.getEpic(3);
        taskManager.getEpic(4);
        taskManager.getSubTask(5);
        taskManager.getSubTask(6);

        System.out.println("HISTORY 1:");
        System.out.println(taskManager.getHistory());

        taskManager.getTask(1);
        taskManager.getEpic(3);

        System.out.println("HISTORY 2 check duplication:");
        System.out.println(taskManager.getHistory());

        taskManager.deleteTaskById(1);

        System.out.println("HISTORY 3 del task:");
        System.out.println(taskManager.getHistory());

        taskManager.deleteTaskById(3);

        System.out.println("HISTORY 4 del epic with subtasks:");
        System.out.println(taskManager.getHistory());

        System.out.println("OTHER INFO:");
        System.out.println(taskManager.getAllTasks());
        System.out.println(taskManager.getAllEpics());
        System.out.println(taskManager.getAllSubtasks());
        System.out.println("HISTORY:");
        System.out.println(taskManager.getHistory());
        printAllTasks(taskManager);
    }

    private static void printAllTasks(TaskManager manager) {
        System.out.println("Задачи:");
        for (Task task : manager.getAllTasks()) {
            System.out.println(task);
        }
        System.out.println("Эпики:");
        for (Epic epic : manager.getAllEpics()) {
            System.out.println(epic);

            for (Task task : manager.getEpicSubTasks(epic)) {
                System.out.println("--> " + task);
            }
        }
        System.out.println("Подзадачи:");
        for (Task subtask : manager.getAllSubtasks()) {
            System.out.println(subtask);
        }

        System.out.println("История:");
        for (Task task : manager.getHistory()) {
            System.out.println(task);
        }
    }
}
