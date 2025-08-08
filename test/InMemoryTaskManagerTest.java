import com.yandex.taskmanagerapp.model.Epic;
import com.yandex.taskmanagerapp.model.Subtask;
import com.yandex.taskmanagerapp.model.Task;
import com.yandex.taskmanagerapp.service.Managers;
import com.yandex.taskmanagerapp.service.TaskManager;
import com.yandex.taskmanagerapp.enums.Status;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class InMemoryTaskManagerTest {
    private final TaskManager tm = Managers.getDefault();

    @Test
    void createTasksDifferentTypes() {
        Task task = new Task("first_task", "non desc", Status.NEW);
        Subtask subtask = new Subtask("first_sub", "non desc", Status.NEW);
        Epic epic = new Epic("first_epic", "non desc");
        boolean tasksDifferentTypes = false;

        tm.createTask(task);
        tm.createSubtask(subtask);
        tm.createEpic(epic);
        if (tm.getTasks().contains(task) && tm.getSubtasks().contains(subtask) && tm.getEpics().contains(epic)) {
            tasksDifferentTypes = true;
        }
        assertTrue(tasksDifferentTypes, "Должны добавляться задачи разного типа");
    }

    @Test
    void checkFindGetTaskId() {
        Task task = new Task("first_task", "non desc", Status.NEW);

        tm.createTask(task);
        assertNotNull(tm.getTask(task.getId()), "Метод getTask должен вернуть задачу");
    }

    @Test
    void checkFindGetSubtaskId() {
        Subtask subtask = new Subtask("first_sub", "non desc", Status.NEW);

        tm.createSubtask(subtask);
        assertNotNull(tm.getTask(subtask.getId()), "Метод getSubtask должен вернуть подзадачу");
    }

    @Test
    void checkFindGetEpicId() {
        Epic epic = new Epic("first_epic", "non desc");

        tm.createEpic(epic);
        assertNotNull(tm.getTask(epic.getId()), "Метод getEpic должен вернуть эпик");
    }
}
