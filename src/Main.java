import java.util.ArrayList;
import java.util.Objects;
import java.util.HashMap;

public class Main {
    public static void main(String[] args) {
        TaskManager tm = new TaskManager();
        Epic epic1 = new Epic("first_ep", "non desc", Status.NEW);
        Subtask sub1 = new Subtask("first_sub", "non desc", Status.NEW);
        Task task1 = new Task("first_tas", "non desc", Status.NEW);
        Task task2 = new Task("2_tas", "non desc", Status.NEW);
        Subtask sub2 = new Subtask("2", "non desc", Status.NEW);
        Task task3 = new Task("3_tas", "non desc", Status.NEW);

        tm.createTask(task1);
        tm.createTask(task2);
        tm.createTask(task3);
        tm.createSubtask(sub1);
        tm.createSubtask(sub2);
        tm.createEpic(epic1);

        System.out.println(epic1.getId());
        System.out.println(sub1.getId());
        System.out.println(task1.getId());
        System.out.println(task2.getId());
        System.out.println(task3.getId());

        tm.getTasks();
        tm.getSubtasks();
        tm.getEpics();

        tm.deleteSubtasks();

        tm.getSubtasks();

        tm.getTask(4);
        tm.getSubtask(2);

        tm.deleteTask(4);
        tm.deleteSubtask(2);
        tm.deleteEpic(1);

        tm.getTasks();
        tm.getSubtasks();
        tm.getEpics();
    }
}
