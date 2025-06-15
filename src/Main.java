public class Main {
    public static void main(String[] args) {
        TaskManager tm = new TaskManager();

        Task task1 = new Task("first_task", "non desc", Status.NEW);
        Task task2 = new Task("second_task", "non desc", Status.NEW);
        Epic epic1 = new Epic("first_epic", "non desc");
        Subtask sub1 = new Subtask("first_sub", "non desc", Status.NEW );
        Subtask sub2 = new Subtask("second_sub", "non desc", Status.NEW );
        Epic epic2 = new Epic("second_epic", "non desc");
        Subtask sub3 = new Subtask("thirst_sub", "non desc", Status.NEW );

        tm.createTask(task1);
        tm.createTask(task2);
        tm.createEpic(epic1);
        tm.createSubtask(sub1, epic1.getId());
        tm.createSubtask(sub2, epic1.getId());
        tm.createEpic(epic2);
        tm.createSubtask(sub3, epic2.getId());

        tm.getEpics();
        tm.getTasks();
        tm.getSubtasks();

        sub3.setStatus(Status.DONE);
        tm.updateSubtask(sub3);

        sub1.setStatus(Status.IN_PROGRESS);
        tm.updateSubtask(sub1);

        task1.setStatus(Status.DONE);
        tm.updateTask(task1);

        tm.getSubtask(sub3.getId());
        tm.getSubtask(sub1.getId());
        tm.getTask(task1.getId());
        tm.getEpic(epic1.getId()); // проверяем состояние эпиков
        tm.getEpic(epic2.getId()); // проверяем состояние эпиков

        tm.deleteTask(1);
        tm.deleteEpic(6);

        tm.getEpics();
        tm.getTasks();
        tm.getSubtasks();
    }
}
