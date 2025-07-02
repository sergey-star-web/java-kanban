package com.yandex.taskmanagerapp.test;

import com.yandex.taskmanagerapp.model.Epic;
import com.yandex.taskmanagerapp.model.Subtask;
import com.yandex.taskmanagerapp.service.Managers;
import com.yandex.taskmanagerapp.service.TaskManager;
import com.yandex.taskmanagerapp.service.Status;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class ManagersTest {
    @Test
    void returnInstanceInMemoryTaskManager() {
        assertNotNull(Managers.getDefault(), "Класс InstanceInMemoryTaskManager всегда должен возвращаться с метода getDefault()");
    }
    @Test
    void returnInstanceHistoryManager() {
        assertNotNull(Managers.getDefaultHistory(), "Класс HistoryManager всегда должен возвращаться с метода getDefaultHistory()");
    }
}
