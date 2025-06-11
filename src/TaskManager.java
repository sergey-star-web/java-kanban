import java.util.ArrayList;
import java.util.Objects;
import java.util.HashMap;

public class TaskManager {
    public static Integer counterId = 0;
    public static HashMap<Integer, Object> things = new HashMap<Integer, Object>(); // нужно создать 3 хэша
    // для каждого типа задачи

    public TaskManager() {
    }

    public static void put(Object object) {
        things.put(TaskManager.counterId, object);
    }

    public ArrayList<Object> setTaskForType(TypeTask typeTask) {
        ArrayList<Object> resultObj = new ArrayList<>();
        switch (typeTask) {
            case EPIC:
                for (Object thing : things.values()) {
                    if (thing.getClass() == Epic.class) resultObj.add(thing);
                }
                return resultObj;
            case SUBTASK:
                for (Object thing : things.values()) {
                    if (thing.getClass() == Subtask.class) resultObj.add(thing);
                }
                return resultObj;
            case TASK:
                for (Object thing : things.values()) {
                    if (thing.getClass() == Task.class) resultObj.add(thing);
                }
                return resultObj;
        }
        return resultObj;
    }

}
