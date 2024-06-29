package tracker.service;

import tracker.Status;
import tracker.TaskTypes;
import tracker.model.Epic;
import tracker.model.Subtask;
import tracker.model.Task;
import tracker.utils.TaskCsvConverter;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileBackedTaskManager extends InMemoryTaskManager {
    private final File file;

    private FileBackedTaskManager(File file) {
        this.file = file;
    }

    public static void main(String[] args) {
        FileBackedTaskManager taskManager =
                loadFromFile("resources/manager.txt");
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
        Subtask subtask3 = new Subtask(taskManager.getNextTaskId(), "Подзадача 3 эпик 2", "", epic2);
        taskManager.addSubtask(subtask3);
    }

    public static FileBackedTaskManager loadFromFile(String filename) {
        FileBackedTaskManager fileBackedTaskManager = new FileBackedTaskManager(new File(filename));
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(filename))) {
            int nextTaskId = 1;
            if (bufferedReader.ready()) {
                nextTaskId = Integer.parseInt(bufferedReader.readLine());
            }
            while (bufferedReader.ready()) {
                String line = bufferedReader.readLine();
                if (!line.isBlank()) {
                    Task task = fileBackedTaskManager.fromString(line);
                    if (task instanceof Epic) {
                        fileBackedTaskManager.addEpic((Epic) task);
                    } else if (task instanceof Subtask) {
                        fileBackedTaskManager.addSubtask((Subtask) task);
                    } else {
                        fileBackedTaskManager.addTask(task);
                    }
                }
            }
            fileBackedTaskManager.nextTaskId = nextTaskId;
        } catch (IOException e) {
            throw new ManagerSaveException("Произошла ошибка при сохранении файла");
        }
        return fileBackedTaskManager;
    }

    private void save() {
        try (FileWriter fileWriter = new FileWriter(file)) {
            fileWriter.write(getNextTaskId() + "\n");
            for (Task task : getAllTasks()) {
                fileWriter.write(TaskCsvConverter.toCSV(task));
                fileWriter.write("\n");
            }
            for (Epic epic : getAllEpics()) {
                fileWriter.write(TaskCsvConverter.toCSV(epic));
                fileWriter.write("\n");
            }
            for (Subtask subtask : getAllSubtasks()) {
                fileWriter.write(TaskCsvConverter.toCSV(subtask));
                fileWriter.write("\n");
            }
        } catch (IOException e) {
            throw new ManagerSaveException("Произошла ошибка при сохранении файла");
        }
    }

    private Task fromString(String value) {
        String[] taskParams = value.split(",", 6);
        TaskTypes type = TaskTypes.valueOf(taskParams[1]);
        Task task = null;
        switch (type) {
            case EPIC:
                task = new Epic(Integer.parseInt(taskParams[0]), taskParams[2], taskParams[4]);
                break;
            case TASK:
                task = new Task(Integer.parseInt(taskParams[0]), taskParams[2], taskParams[4]);
                break;
            case SUBTASK:
                task = new Subtask(Integer.parseInt(taskParams[0]), taskParams[2], taskParams[4],
                        getEpic(Integer.parseInt(taskParams[5])));
                break;
        }
        task.setStatus(Status.valueOf(taskParams[3]));
        return task;
    }

    @Override
    public void addSubtask(Subtask subtask) {
        super.addSubtask(subtask);
        save();
    }

    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
        save();
    }

    @Override
    public void updateEpic(Epic epic) {
        super.updateEpic(epic);
        save();
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        super.updateSubtask(subtask);
        save();
    }

    @Override
    public void deleteTaskById(int id) {
        super.deleteTaskById(id);
        save();
    }

    @Override
    public ArrayList<Subtask> getEpicSubTasks(Epic epic) {
        return super.getEpicSubTasks(epic);
    }

    @Override
    public void deleteAllTasks() {
        super.deleteAllTasks();
        save();
    }

    @Override
    public void deleteAllSubtasks() {
        super.deleteAllSubtasks();
        save();
    }

    @Override
    public void deleteAllEpics() {
        super.deleteAllEpics();
        save();
    }

    @Override
    public int getNextTaskId() {
        return super.getNextTaskId();
    }

    @Override
    public List<Task> getAllTasks() {
        return super.getAllTasks();
    }

    @Override
    public List<Epic> getAllEpics() {
        return super.getAllEpics();
    }

    @Override
    public List<Subtask> getAllSubtasks() {
        return super.getAllSubtasks();
    }

    @Override
    public List<Task> getHistory() {
        return super.getHistory();
    }

    @Override
    public Task getTask(int id) {
        return super.getTask(id);
    }

    @Override
    public Epic getEpic(int id) {
        return super.getEpic(id);
    }

    @Override
    public Subtask getSubTask(int id) {
        return super.getSubTask(id);
    }

    @Override
    public void addTask(Task task) {
        super.addTask(task);
        save();
    }

    @Override
    public void addEpic(Epic epic) {
        super.addEpic(epic);
        save();
    }
}
