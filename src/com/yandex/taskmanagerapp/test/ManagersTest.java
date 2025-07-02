package com.yandex.taskmanagerapp.test;

import com.yandex.taskmanagerapp.model.Epic;
import com.yandex.taskmanagerapp.model.Subtask;
import com.yandex.taskmanagerapp.model.Task;
import com.yandex.taskmanagerapp.service.HistoryManager;
import com.yandex.taskmanagerapp.service.Managers;
import com.yandex.taskmanagerapp.service.TaskManager;
import com.yandex.taskmanagerapp.service.Status;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ManagersTest {
    private final HistoryManager historyManager = Managers.getDefaultHistory();
    private final TaskManager tm = Managers.getDefault();

    @Test
    void returnInstanceInMemoryTaskManager() {
        assertNotNull(Managers.getDefault(), "Класс InstanceInMemoryTaskManager всегда должен возвращаться с метода getDefault()");
    }

    @Test
    void returnInstanceHistoryManager() {
        assertNotNull(Managers.getDefaultHistory(), "Класс HistoryManager всегда должен возвращаться с метода getDefaultHistory()");
    }

    @Test
    void checkTasksVersionsWhenAddingHistoryManager() {
        Task task = new Task("first_task", "non desc", Status.NEW);

        tm.createTask(task);
        tm.getTask(task.getId());
        Task taskPrev = tm.getHistory().getLast();
        task.setDescription("test task update description");
        tm.getTask(task.getId());
        Task taskCurr = tm.getHistory().getLast();
        assertFalse(taskPrev.getDescription().equals(taskCurr.getDescription()), "Описание тасков не должно быть одинаковым");
    }

    @Test
    void checkSubtasksVersionsWhenAddingHistoryManager() {
        Subtask subtask = new Subtask("first_sub", "non desc", Status.NEW );

        subtask.setIdEpic(1);
        tm.createSubtask(subtask);
        tm.getSubtask(subtask.getId());
        Subtask subtaskPrev = (Subtask) tm.getHistory().getLast();
        subtask.setDescription("test subtask update description");
        subtask.setIdEpic(999);
        tm.getSubtask(subtask.getId());
        Subtask subtaskCurr = (Subtask) tm.getHistory().getLast();
        assertFalse(subtaskPrev.getDescription().equals(subtaskCurr.getDescription()), "Описание сабтасков не должно быть одинаковым");
        assertFalse(subtaskPrev.getIdEpic() == subtaskCurr.getIdEpic(), "Код эпика в сабтасках не должно быть одинаковым");
    }

    @Test
    void checkEpicsVersionsWhenAddingHistoryManager() {
        Epic epic = new Epic("first_epic", "non desc");
        Subtask subtask = new Subtask("first_sub", "non desc", Status.NEW);

        tm.createEpic(epic);
        subtask.setIdEpic(epic.getId());
        tm.createSubtask(subtask);
        tm.getEpic(epic.getId());
        Epic epicPrev = (Epic) tm.getHistory().getLast();
        epic.setDescription("test subtask update description");
        epic.setSubtasks(null);
        tm.getEpic(epic.getId());
        Epic epicCurr = (Epic) tm.getHistory().getLast();
        assertFalse(epicPrev.getDescription().equals(epicCurr.getDescription()), "Описание эпиков не должно быть одинаковым");
        assertFalse(epicPrev.getSubtasks().equals(epicCurr.getSubtasks()), "Список сабтасков в эпиках не должен быть одинаковым");
    }
}
