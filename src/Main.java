import java.util.ArrayList;
import java.util.Objects;
import java.util.HashMap;

public class Main {
    public static void main(String[] args) {
        TaskManager tm = new TaskManager();
        //Epic epic1 = new Epic("first_ep", "non desc", Status.NEW);
        Subtask sub1 = new Subtask("first_sub", "non desc", Status.NEW);
        Task task1 = new Task("first_tas", "non desc", Status.NEW);
        Task task2 = new Task("2_tas", "non desc", Status.NEW);
        Task task3 = new Task("3_tas", "non desc", Status.NEW);
       // System.out.println(epic1.getId());
        System.out.println(sub1.getId());
        System.out.println(task1.getId());
        System.out.println(task2.getId());
        System.out.println(task3.getId());
        System.out.println(TaskManager.things);

        ArrayList<Object> objts = tm.setTaskForType(TypeTask.TASK);
        for (Object obj : objts) {
            System.out.println((Task) obj);
        }

        ArrayList<Object> objst = tm.setTaskForType(TypeTask.SUBTASK);
        for (Object obj : objst) {
            System.out.println((Subtask) obj);
        }

        ArrayList<Object> objep = tm.setTaskForType(TypeTask.EPIC);
        for (Object obj : objep) {
            System.out.println((Epic) obj);
        }
    }
}
