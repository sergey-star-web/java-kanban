import com.yandex.taskmanagerapp.service.FileBackedTaskManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.*;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import static org.junit.jupiter.api.Assertions.*;

public class FileBackedTaskManagerTest {
    private FileBackedTaskManager fileBackedTaskManager;
    private Path tempFile;

    @BeforeEach
    public void createFileTaskManager() {
        try {
            tempFile = Files.createTempFile("tasks", ".csv");
            tempFile.toFile().deleteOnExit();
            String content = "1,TASK,second_task,NEW,non desc,45,2025-08-01T10:00\n" +
                             "2,EPIC,first_epic,IN_PROGRESS,non desc,160,2025-08-04T10:30,2025-08-04T11:30\n" +
                             "3,SUBTASK,first_sub,IN_PROGRESS,non desc,60,2025-08-04T10:30,2\n";
            Files.write(tempFile, content.getBytes());
        } catch (IOException e) {
            System.out.println("Ошибка при создания временного файла: " + e.getMessage());
        }
        fileBackedTaskManager = FileBackedTaskManager.loadFromFile(tempFile.toFile());
    }

    @Test
    public void checkingForTheAbilityToDownloadFromAFile() {
        fileBackedTaskManager = FileBackedTaskManager.loadFromFile(tempFile.toFile());
        assertEquals(3, fileBackedTaskManager.getAllTasks().size());
    }

    @Test
    public void checkingWenUploadingFileInIncorrectFormat() {
        assertDoesNotThrow(() -> {
            Path tempFile1 = Files.createTempFile("temp", ".csv");
            tempFile1.toFile().deleteOnExit();
            String content = "0,TASKKKKK,Задача 1,NEW,non desc,45,2025-08-01T10:00\n" +
                    "1,EPIC,Задача 2,IN_PROGRESS,non desc,160,2025-08-04T10:30,2025-08-04T11:30\n" +
                    "2,SUBTASK,Задача 3,DONE,non desc,60,2025-08-04T10:30,1\n";
            Files.write(tempFile1, content.getBytes());
            fileBackedTaskManager = FileBackedTaskManager.loadFromFile(tempFile1.toFile());
        }, "Произошло исключение");
    }

    @Test
    public void catchingExceptionsWhenWritingToAFanFile() {
        assertThrows(IOException.class, () -> {
            Path fis = Paths.get("nonExistsFile.txt");
            BufferedWriter writer = Files.newBufferedWriter(fis, StandardCharsets.UTF_8, StandardOpenOption.APPEND);
        }, "Произошло исключение");
    }
}
