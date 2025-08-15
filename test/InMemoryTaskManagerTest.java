import com.yandex.taskmanagerapp.model.Epic;
import com.yandex.taskmanagerapp.model.Subtask;
import com.yandex.taskmanagerapp.model.Task;
import com.yandex.taskmanagerapp.service.Managers;
import com.yandex.taskmanagerapp.service.TaskManager;
import com.yandex.taskmanagerapp.enums.Status;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class InMemoryTaskManagerTest {
    private final TaskManager tm = Managers.getMemoryTaskManager();

    @Test
    void createTasksDifferentTypes() {
        Task task = new Task("first_task", "non desc", Status.NEW, 30, "2025-08-01 10:00:00");
        Subtask subtask = new Subtask("first_sub", "non desc", Status.NEW, 50, "2025-08-01 15:00:00");
        Epic epic = new Epic("first_epic", "non desc");
        boolean tasksDifferentTypes = false;

        tm.addTask(task);
        tm.addSubtask(subtask);
        tm.addEpic(epic);
        if (tm.getTasks().contains(task) && tm.getSubtasks().contains(subtask) && tm.getEpics().contains(epic)) {
            tasksDifferentTypes = true;
        }
        assertTrue(tasksDifferentTypes, "Должны добавляться задачи разного типа");
    }

    @Test
    void checkFindGetTaskId() {
        Task task = new Task("first_task", "non desc", Status.NEW, 30, "2025-08-01 10:00:00");

        tm.addTask(task);
        assertNotNull(tm.getTask(task.getId()), "Метод getTask должен вернуть задачу");
    }

    @Test
    void checkFindGetSubtaskId() {
        Subtask subtask = new Subtask("first_sub", "non desc", Status.NEW, 30, "2025-08-01 10:00:00");

        tm.addSubtask(subtask);
        assertNotNull(tm.getTask(subtask.getId()), "Метод getSubtask должен вернуть подзадачу");
    }

    @Test
    void checkFindGetEpicId() {
        Epic epic = new Epic("first_epic", "non desc");

        tm.addEpic(epic);
        assertNotNull(tm.getTask(epic.getId()), "Метод getEpic должен вернуть эпик");
    }
}
