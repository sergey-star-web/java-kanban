import com.yandex.taskmanagerapp.enums.Status;
import com.yandex.taskmanagerapp.model.Epic;
import com.yandex.taskmanagerapp.model.Subtask;
import com.yandex.taskmanagerapp.model.Task;
import com.yandex.taskmanagerapp.service.HistoryManager;
import com.yandex.taskmanagerapp.service.InMemoryHistoryManager;
import com.yandex.taskmanagerapp.service.Managers;
import com.yandex.taskmanagerapp.service.TaskManager;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class InMemoryHistoryManagerTest {
    private final HistoryManager historyManager = Managers.getDefaultHistory();
    private final TaskManager tm = Managers.getMemoryTaskManager();

    @Test
    public void correctForEmptyHistory() {
        Task task = new Task(1,"Task", "Task description", Status.NEW);
        historyManager.add(task);
        historyManager.remove(task.getId());
        assertNull(historyManager.getTask(task.getId()));
    }

    @Test
    public void checkForDuplicateTasksInTheHistory() {
        Task task = new Task("Task", "Task description", Status.NEW);
        tm.addTask(task);
        tm.getTask(task.getId());
        tm.getTask(task.getId());
        tm.getTask(task.getId());
        assertEquals(1, tm.getHistory().size());
    }

    @Test
    public void checkDeleteDiffTasksInTheHistory() {
        Task task = new Task("first_task", "non desc", Status.NEW, 30, "2025-08-01 10:00:00");
        Epic epic = new Epic("first_epic", "non desc");
        Subtask subtask = new Subtask("first_sub", "non desc", Status.NEW, 30, "2025-08-02 10:00:00");
        tm.addTask(task);
        tm.addEpic(epic);
        subtask.setIdEpic(epic.getId());
        tm.addSubtask(subtask);

        tm.getTask(task.getId());
        tm.getTask(epic.getId());
        tm.getTask(subtask.getId());

        InMemoryHistoryManager hm = (InMemoryHistoryManager) tm.getInMemoryHistory();

        hm.remove(task.getId());
        hm.remove(epic.getId());
        hm.remove(subtask.getId());

        assertEquals(0, hm.size());
    }
}
