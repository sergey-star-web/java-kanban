import java.util.ArrayList;
import java.util.HashMap;

public class TaskManager {
    private Integer counterId = 0;
    public static HashMap<Integer, Task> tasks = new HashMap<Integer, Task>();
    public static HashMap<Integer, Subtask> subTasks = new HashMap<Integer, Subtask>();
    public static HashMap<Integer, Epic> epics = new HashMap<Integer, Epic>();

    public TaskManager() {
    }

    public void getTasks() {
        if(!tasks.isEmpty()) {
            System.out.println("Список задач: ");
            int calcCounter = 0;
            for (Integer id : tasks.keySet()) {
                calcCounter++;
                System.out.println((calcCounter) + ") " + tasks.get(id).toString());
            }
            System.out.println();
        } else {
            System.out.println("Список задач пуст.");
            System.out.println();
        }
    }

    public void getSubtasks() {
        if(!subTasks.isEmpty()) {
            System.out.println("Список подзадач: ");
            int calcCounter = 0;
            for (Integer id : subTasks.keySet()) {
                calcCounter++;
                System.out.println((calcCounter) + ") " + subTasks.get(id).toString());
            }
            System.out.println();
        } else {
            System.out.println("Список подзадач пуст.");
            System.out.println();
        }
    }

    public void getEpics() {
        if(!epics.isEmpty()) {
            System.out.println("Список эпиков: ");
            int calcCounter = 0;
            for (Integer id : epics.keySet()) {
                calcCounter++;
                System.out.println((calcCounter) + ") " + epics.get(id).toString());
            }
            System.out.println();
        } else {
            System.out.println("Список эпиков пуст.");
            System.out.println();
        }
    }

    public void getTask(Integer Id) {
        Task task = tasks.get(Id);

        if (task != null) {
            System.out.println(task);
            System.out.println();
        } else {
            System.out.println("Задача не найдена.");
            System.out.println();
        }
    }

    public void getSubtask(Integer Id) {
        Subtask subTask = subTasks.get(Id);

        if (subTask != null) {
            System.out.println(subTask);
            System.out.println();
        } else {
            System.out.println("Подзадача не найдена.");
            System.out.println();
        }
    }

    public void getEpic(Integer Id) {
        Epic epic = epics.get(Id);

        if (epic != null) {
            System.out.println(epic);
            System.out.println();
        } else {
            System.out.println("Эпик не найден.");
            System.out.println();
        }
    }

    public void getSubtasksInEpic(Integer Id) {
        Epic epic = epics.get(Id);
        ArrayList<Subtask> subtasksInEpic = new ArrayList<Subtask>();

        if (epic != null) {
            if (epic.getSubtasks() != null) {
                subtasksInEpic = epic.getSubtasks();
            }
            if (!subtasksInEpic.isEmpty()) {
                System.out.println("Список подзадач эпика " + epic.getName()+ ":");
                for (int i = 0; i < subtasksInEpic.size(); i++) {
                    System.out.println(i + 1 + ") " + subtasksInEpic.get(i).toString());
                }
                System.out.println();
            } else {
                System.out.println("Список подзадач эпика пуст");
                System.out.println();
            }
        } else {
            System.out.println("Эпик не найден.");
            System.out.println();
        }
    }

    public void deleteTasks() {
        if(!tasks.isEmpty()) {
            tasks = new HashMap<Integer, Task>();
        }
    }

    public void deleteSubtasks() {
        if(!subTasks.isEmpty()) {
            subTasks = new HashMap<Integer, Subtask>();
        }
    }

    public void deleteEpics() {
        if(!epics.isEmpty()) {
            subTasks = new HashMap<Integer, Subtask>();
            epics = new HashMap<Integer, Epic>();
        }
    }

    public void deleteTask(Integer Id) {
        Task task = tasks.get(Id);

        if(task != null) {
            tasks.remove(Id);
            System.out.println("Задача:" + task.getName() + " удалена");
            System.out.println();
        } else {
            System.out.println("Задача не найдена.");
            System.out.println();
        }
    }

    public void deleteSubtask(Integer Id) {
        Subtask subTask = subTasks.get(Id);

        if(subTask != null) {
            updateSubtasksInEpic((byte) 2, subTask.getIdEpic(), subTask.getId());
            subTasks.remove(Id);
            updateStatusInEpic(subTask.getIdEpic());
            System.out.println("Подзадача:" + subTask.getName() + " удалена");
            System.out.println();
        } else {
            System.out.println("Подзадача не найдена.");
            System.out.println();
        }
    }

    public void deleteEpic(Integer Id) {
        Epic epic = epics.get(Id);

        if(epic != null) {
            for (Integer id : subTasks.keySet()) {
                if (subTasks.get(id).getIdEpic().equals(Id)) {
                    subTasks.remove(id);
                }
            }
            epics.remove(Id);
            System.out.println("Эпик:" + epic.getName() + " удален");
            System.out.println();
        } else {
            System.out.println("Эпик не найден.");
            System.out.println();
        }
    }

    public void createTask(Task task) {
        this.counterId++;
        task.setId(this.counterId);
        tasks.put(task.getId(), task);
    }

    public void createSubtask(Subtask subTask, Integer idEpic) {
        this.counterId++;
        subTask.setId(this.counterId);
        subTask.setIdEpic(idEpic);
        subTasks.put(subTask.getId(), subTask);
        updateSubtasksInEpic((byte) 1, idEpic, subTask.getId());
        updateStatusInEpic(subTask.getIdEpic());
    }

    public void createEpic(Epic epic) { //нужно скорректировать создание эпика
        this.counterId++;
        epic.setId(this.counterId);
        epics.put(epic.getId(), epic);
    }

    private void updateSubtasksInEpic(byte createSubtask, Integer idEpic, Integer idSubtask) {
        /*
        createSubtask = 1: create Subtask
        createSubtask = 2: delete Subtask
        createSubtask = 3: update Subtask
        */
        Epic epic = epics.get(idEpic);
        Subtask subTask = subTasks.get(idSubtask);

        if (epic != null && subTask != null) {
            ArrayList<Subtask> subtasksInEpic = new ArrayList<Subtask>();
            if (epic.getSubtasks() != null) {
                subtasksInEpic = epic.getSubtasks();
            }
            if (createSubtask == 1) {
                subtasksInEpic.add(subTask);
            } else if (createSubtask == 2) {
                int indexSub = -1;
                for(int i = 0; i < subtasksInEpic.size(); i++) {
                   if (subtasksInEpic.get(i).getId().equals(subTask.getId())) {
                       indexSub = i;
                       break;
                   }
                }
                subtasksInEpic.remove(indexSub);
            } else if (createSubtask == 3) {
                int indexSub = -1;
                for (int i = 0; i < subtasksInEpic.size(); i++) {
                    if (subtasksInEpic.get(i).getId().equals(idSubtask)) {
                        indexSub = i;
                    }
                }
                if (indexSub != -1) {
                    subtasksInEpic.set(indexSub, subTask);
                }
            }
            epic.setSubtasks(subtasksInEpic);
        }
    }

    private void updateStatusInEpic(Integer IdEpic) {
        Epic epic = epics.get(IdEpic);
        ArrayList<Subtask> subtasksInEpic = new ArrayList<Subtask>();
        boolean existsNew = false;
        boolean existsDone = false;
        boolean existsIN_PROGRESS = false;

        if (epic != null) {
            if (epic.getSubtasks() != null) {
                subtasksInEpic = epic.getSubtasks();
            }
            for (Subtask subtask : subtasksInEpic) {
                if (subtask.getStatus() == Status.NEW) {
                    existsNew = true;
                }
                if (subtask.getStatus() == Status.DONE) {
                    existsDone  = true;
                }
                if (subtask.getStatus() == Status.IN_PROGRESS) {
                    existsIN_PROGRESS = true;
                }
            }
            if (existsNew && !existsDone && !existsIN_PROGRESS) {
                if (epic.getStatus() != Status.NEW) {
                    epic.setStatus(Status.NEW);
                }
            } else if (existsDone && !existsNew && !existsIN_PROGRESS) {
                if (epic.getStatus() != Status.DONE) {
                    epic.setStatus(Status.DONE);
                }
            } else if (!subtasksInEpic.isEmpty()) {
                if (epic.getStatus() != Status.IN_PROGRESS) {
                    epic.setStatus(Status.IN_PROGRESS);
                }
            } else {
                if (epic.getStatus() != Status.NEW) {
                    epic.setStatus(Status.NEW);
                }
            }
        }
    }

    public void updateTask(Task task) {
        tasks.put(task.getId(), task);
    }

    public void updateSubtask(Subtask subtask) {
        subTasks.put(subtask.getId(), subtask);
        updateSubtasksInEpic((byte) 3, subtask.getIdEpic(), subtask.getId());
        updateStatusInEpic(subtask.getIdEpic());
    }

    public void updateEpic(Epic epic) {
        epics.put(epic.getId(), epic);
    }

}
