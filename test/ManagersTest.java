package com.test;

import com.yandex.taskmanagerapp.model.Epic;
import com.yandex.taskmanagerapp.model.Subtask;
import com.yandex.taskmanagerapp.model.Task;
import com.yandex.taskmanagerapp.service.Managers;
import com.yandex.taskmanagerapp.service.TaskManager;
import com.yandex.taskmanagerapp.enums.Status;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ManagersTest {
    private final TaskManager tm = Managers.getMemoryTaskManager();

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
        Task task = new Task("first_task", "non desc", Status.NEW, 30, "2025-08-01 10:00:00");

        tm.addTask(task);
        tm.getTask(task.getId());
        Task taskPrev = tm.getHistory().getFirst();
        taskPrev = new Task(taskPrev.getName(), taskPrev.getDescription(), taskPrev.getStatus());

        task.setDescription("test task update description");
        tm.getTask(task.getId());
        Task taskCurr = tm.getHistory().getFirst();
        taskCurr = new Task(taskCurr.getName(), taskCurr.getDescription(), taskCurr.getStatus());

        assertFalse(taskPrev.getDescription().equals(taskCurr.getDescription()), "Описание тасков не должно быть одинаковым");
    }

    @Test
    void checkSubtasksVersionsWhenAddingHistoryManager() {
        Subtask subtask = new Subtask("first_sub", "non desc", Status.NEW, 30, "2025-08-01 10:00:00");

        subtask.setIdEpic(1);
        tm.addSubtask(subtask);
        tm.getTask(subtask.getId());

        Subtask subtaskPrev = (Subtask) tm.getHistory().getFirst();
        subtaskPrev = new Subtask(subtaskPrev.getName(), subtaskPrev.getDescription(), subtaskPrev.getStatus(),
                Integer.parseInt(String.valueOf(subtaskPrev.getDuration().toMinutes())),
                subtaskPrev.getStartTimeString());
        subtaskPrev.setIdEpic(subtask.getIdEpic());

        subtask.setDescription("test subtask update description");
        subtask.setIdEpic(999);
        tm.getTask(subtask.getId());

        Subtask subtaskCurr = (Subtask) tm.getHistory().getFirst();
        subtaskCurr = new Subtask(subtaskCurr.getName(), subtaskCurr.getDescription(), subtaskCurr.getStatus(),
                Integer.parseInt(String.valueOf(subtaskCurr.getDuration().toMinutes())),
                subtaskCurr.getStartTimeString());
        subtaskCurr.setIdEpic(subtask.getIdEpic());

        assertFalse(subtaskPrev.getDescription().equals(subtaskCurr.getDescription()), "Описание сабтасков не должно быть одинаковым");
        assertFalse(subtaskPrev.getIdEpic() == subtaskCurr.getIdEpic(), "Код эпика в сабтасках не должно быть одинаковым");
    }

    @Test
    void checkEpicsVersionsWhenAddingHistoryManager() {
        Epic epic = new Epic("first_epic", "non desc");
        Subtask subtask = new Subtask("first_sub", "non desc", Status.NEW, 30, "2025-08-01 10:00:00");

        tm.addEpic(epic);
        subtask.setIdEpic(epic.getId());
        tm.addSubtask(subtask);
        tm.getTask(epic.getId());
        Epic epicPrev = (Epic) tm.getHistory().getFirst();
        epicPrev = new Epic(epicPrev.getName(), epicPrev.getDescription());
        epicPrev.setSubtasks(epic.getSubtasks());

        epic.setDescription("test subtask update description");
        epic.setSubtasks(null);
        tm.getTask(epic.getId());
        Epic epicCurr = (Epic) tm.getHistory().getFirst();
        epicCurr = new Epic(epicCurr.getName(), epicCurr.getDescription());
        epicCurr.setSubtasks(epic.getSubtasks());

        assertFalse(epicPrev.getDescription().equals(epicCurr.getDescription()), "Описание эпиков не должно быть одинаковым");
        assertFalse(epicPrev.getSubtasks().equals(epicCurr.getSubtasks()), "Список сабтасков в эпиках не должен быть одинаковым");
    }
}
