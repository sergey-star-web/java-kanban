import com.google.gson.Gson;
import com.yandex.taskmanagerapp.api.HttpTaskServer;
import com.yandex.taskmanagerapp.constants.Constant;
import com.yandex.taskmanagerapp.enums.Status;
import com.yandex.taskmanagerapp.service.InMemoryTaskManager;
import com.yandex.taskmanagerapp.service.TaskManager;
import com.yandex.taskmanagerapp.model.Epic;
import com.yandex.taskmanagerapp.model.Subtask;
import com.yandex.taskmanagerapp.model.Task;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class HttpTaskManagerTasksTest {

    // создаём экземпляр InMemoryTaskManager
    TaskManager manager = new InMemoryTaskManager();
    // передаём его в качестве аргумента в конструктор HttpTaskServer
    HttpTaskServer taskServer = new HttpTaskServer(manager);
    Gson gson = Constant.getGsonObject();

    public HttpTaskManagerTasksTest() throws IOException {
    }

    @BeforeEach
    public void setUp() {
        manager.deleteTasks();
        taskServer.start();
    }

    @AfterEach
    public void shutDown() {
        taskServer.stop();
    }

    @Test
    public void testAddTask() throws IOException, InterruptedException {
        // создаём задачу
        Task task = new Task(1,"Test 2", "Testing task 2", Status.NEW,
                Duration.ofMinutes(5), LocalDateTime.now());
        // конвертируем её в JSON
        String taskJson = gson.toJson(task);

        // создаём HTTP-клиент и запрос
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks");
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(HttpRequest.BodyPublishers.ofString(taskJson)).build();
        taskServer.handleRequest(request);

        // вызываем рест, отвечающий за создание задач
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        // проверяем код ответа
        assertEquals(201, response.statusCode());

        // проверяем, что создалась одна задача с корректным именем
        List<Task> tasksFromManager = manager.getTasks();

        assertNotNull(tasksFromManager, "Задачи не возвращаются");
        assertEquals(1, tasksFromManager.size(), "Некорректное количество задач");
        assertEquals("Test 2", tasksFromManager.get(0).getName(), "Некорректное имя задачи");
    }

    @Test
    public void testAddSubtask() throws IOException, InterruptedException {
        // создаём задачу
        Subtask sub = new Subtask(1, "first_sub", "non desc", Status.NEW, 1,
                "2025-08-04 10:30:00", 60);
        // конвертируем её в JSON
        String taskJson = gson.toJson(sub);

        // создаём HTTP-клиент и запрос
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/subtasks");
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(HttpRequest.BodyPublishers.ofString(taskJson)).build();
        taskServer.handleRequest(request);

        // вызываем рест, отвечающий за создание задач
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        // проверяем код ответа
        assertEquals(201, response.statusCode());

        // проверяем, что создалась одна задача с корректным именем
        List<Subtask> subtasksFromManager = manager.getSubtasks();

        assertNotNull(subtasksFromManager, "Задачи не возвращаются");
        assertEquals(1, subtasksFromManager.size(), "Некорректное количество задач");
        assertEquals("first_sub", subtasksFromManager.get(0).getName(), "Некорректное имя задачи");
    }

    @Test
    public void testAddEpic() throws IOException, InterruptedException {
        // создаём задачу
        Epic epic = new Epic(1, "first_epic", "non desc", Status.NEW);
        // конвертируем её в JSON
        String taskJson = gson.toJson(epic);

        // создаём HTTP-клиент и запрос
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/epics");
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(HttpRequest.BodyPublishers.ofString(taskJson)).build();
        taskServer.handleRequest(request);

        // вызываем рест, отвечающий за создание задач
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        // проверяем код ответа
        assertEquals(201, response.statusCode());

        // проверяем, что создалась одна задача с корректным именем
        List<Epic> epicsFromManager = manager.getEpics();

        assertNotNull(epicsFromManager, "Задачи не возвращаются");
        assertEquals(1, epicsFromManager.size(), "Некорректное количество задач");
        assertEquals("first_epic", epicsFromManager.get(0).getName(), "Некорректное имя задачи");
    }

    @Test
    public void testGetHistory() throws IOException, InterruptedException {
        Task task1 = new Task("first_task", "non desc", Status.NEW, 35, "2025-08-01 14:15:30");
        Epic epic1 = new Epic("first_epic", "non desc");
        Subtask sub1 = new Subtask("first_sub", "non desc", Status.NEW, 60, "2025-08-04 10:30:00");
        Epic epic2 = new Epic("second_epic", "non desc");
        Subtask sub3 = new Subtask("thirst_sub", "non desc", Status.NEW, 50, "2025-08-03 15:35:00");

        manager.addTask(task1);
        manager.addEpic(epic1);
        manager.addEpic(epic2);
        sub1.setIdEpic(epic1.getId());
        sub3.setIdEpic(epic2.getId());
        manager.addSubtask(sub1);
        manager.addSubtask(sub3);
        manager.getTask(sub3.getId());
        manager.getTask(sub1.getId());
        manager.getTask(task1.getId());
        manager.getTask(sub1.getId());

        // создаём HTTP-клиент и запрос
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/history");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        taskServer.handleRequest(request);

        // вызываем рест, отвечающий за создание задач
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        // проверяем код ответа
        assertEquals(200, response.statusCode());
        // проверяем, что создалась одна задача с корректным именем
        List<Task> hystoryTasks = manager.getHistory();
        assertNotNull(hystoryTasks, "Задачи не возвращаются");
        assertEquals(3, hystoryTasks.size(), "Некорректное количество задач");
    }

    @Test
    public void testGetPrioritized() throws IOException, InterruptedException {
        Task task1 = new Task("first_task", "non desc", Status.NEW, 35, "2025-08-01 14:15:30");
        Epic epic1 = new Epic("first_epic", "non desc");
        Subtask sub1 = new Subtask("first_sub", "non desc", Status.NEW, 60, "2025-08-04 10:30:00");
        Epic epic2 = new Epic("second_epic", "non desc");
        Subtask sub2 = new Subtask("thirst_sub", "non desc", Status.NEW, 50, null);
        Subtask sub3 = new Subtask("thirst_sub", "non desc", Status.NEW, 50, "2025-08-03 15:35:00");

        manager.addTask(task1);
        manager.addEpic(epic1);
        manager.addEpic(epic2);
        sub1.setIdEpic(epic1.getId());
        sub2.setIdEpic(epic1.getId());
        sub3.setIdEpic(epic2.getId());
        manager.addSubtask(sub1);
        manager.addSubtask(sub2);
        manager.addSubtask(sub3);

        // создаём HTTP-клиент и запрос
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/prioritized");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        taskServer.handleRequest(request);
        // вызываем рест, отвечающий за создание задач
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        // проверяем код ответа
        assertEquals(200, response.statusCode());
        // проверяем, что создалась одна задача с корректным именем
        List<Task> prioritizedTasks = manager.getPrioritizedTasks();
        assertNotNull(prioritizedTasks, "Задачи не возвращаются");
        assertEquals(3, prioritizedTasks.size(), "Некорректное количество задач");
    }
}
