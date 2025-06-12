import java.util.ArrayList;
import java.util.Objects;
import java.util.HashMap;

public class TaskManager {
    private Integer counterId = 0;
    public static HashMap<Integer, Task> tasks = new HashMap<Integer, Task>();
    public static HashMap<Integer, Subtask> subTasks = new HashMap<Integer, Subtask>();
    public static HashMap<Integer, Epic> epics = new HashMap<Integer, Epic>();

    public TaskManager() {
    }

    public void setTasks(Object obj) {
        if (obj.getClass() == Subtask.class) {
            subTasks.put(this.counterId, (Subtask) obj);
        } else if (obj.getClass() == Epic.class) {
            epics.put(this.counterId, (Epic) obj);
        } else {
            tasks.put(this.counterId, (Task) obj);
        }
    }

    public void getTasks() {
        if(!tasks.isEmpty()) {
            System.out.println(tasks.values());
        } else {
            System.out.println("Список задач пуст.");
        }
    }

    public void getSubtasks() {
        if(!subTasks.isEmpty()) {
            System.out.println(subTasks.values());
        } else {
            System.out.println("Список подзадач пуст.");
        }
    }

    public void getEpics() {
        if(!epics.isEmpty()) {
            System.out.println(epics.values());
        } else {
            System.out.println("Список эпиков пуст.");
        }
    }

    public void getTask(Integer Id) {
        Task task = tasks.get(Id);
        if (task != null) {
            System.out.println(task);
        } else {
            System.out.println("Задача не найдена.");
        }
    }

    public void getSubtask(Integer Id) {
        Subtask subTask = subTasks.get(Id);
        if (subTask != null) {
            System.out.println(subTask);
        } else {
            System.out.println("Подзадача не найдена.");
        }
    }

    public void getEpic(Integer Id) {
        Epic epic = epics.get(Id);
        if (epic != null) {
            System.out.println(epic);
        } else {
            System.out.println("Эпик не найден.");
        }
    }

    public void deleteTasks() {
        if(!tasks.isEmpty()) {
            tasks = new HashMap<Integer, Task>();
        }
    }

    public void deleteSubtasks() {
        if(!subTasks.isEmpty()) {
            subTasks = new HashMap<Integer, Subtask>();
        }
    }

    public void deleteEpics() {
        if(!epics.isEmpty()) {
            epics = new HashMap<Integer, Epic>();
        }
    }

    public void deleteTask(Integer Id) {
        Task task = tasks.get(Id);
        if(task != null) {
            tasks.remove(Id);
            System.out.println("Задача:" + task.getName() + " удалена");
        } else {
            System.out.println("Задача не найдена.");
        }
    }

    public void deleteSubtask(Integer Id) {
        Subtask subTask = subTasks.get(Id);
        if(subTask != null) {
            tasks.remove(Id);
            System.out.println("Подзадача:" + subTask.getName() + " удалена");
        } else {
            System.out.println("Подзадача не найдена.");
        }
    }

    public void deleteEpic(Integer Id) {
        Epic epic = epics.get(Id);
        if(epic != null) {
            epics.remove(Id);
            System.out.println("Эпик:" + epic.getName() + " удален");
        } else {
            System.out.println("Эпик не найден.");
        }
    }

    public void createTask(Task task) {
        this.counterId++;
        task.setId(this.counterId);
        tasks.put(task.getId(), task);
    }

    public void createSubtask(Subtask subtask) { //нужно скорректировать создание сабтаска
        this.counterId++;
        subtask.setId(this.counterId);
        subTasks.put(subtask.getId(), subtask);
    }

    public void createEpic(Epic epic) { //нужно скорректировать создание эпика
        this.counterId++;
        epic.setId(this.counterId);
        epics.put(epic.getId(), epic);
    }

    public void updateTask(Task task) {
        tasks.put(task.getId(), task);
    }

    public void updateSubtask(Subtask subtask) {
        subTasks.put(subtask.getId(), subtask);
    }

    public void updateEpic(Epic epic) {
        epics.put(epic.getId(), epic);
    }

}
