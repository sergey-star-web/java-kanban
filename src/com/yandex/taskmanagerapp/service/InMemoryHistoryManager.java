package com.yandex.taskmanagerapp.service;

import com.yandex.taskmanagerapp.model.Node;
import com.yandex.taskmanagerapp.model.Task;

import java.util.ArrayList;
import java.util.HashMap;

public class InMemoryHistoryManager implements HistoryManager {
    private HashMap<Integer, Node<Task>> placeInspectionTask = new HashMap<>();
    private Node tail;
    private Node head;

    @Override
    public void add(Task task) {
        if (placeInspectionTask.containsKey(task.getId())) {
            removeNode(placeInspectionTask.get(task.getId()));
        }
        linkLast(task);
    }

    @Override
    public void remove(int id) {
        if (placeInspectionTask.containsKey(id)) {
            removeNode(placeInspectionTask.get(id));
        }
    }

    @Override
    public ArrayList<Task> getHistory() {
        return getTasks();
    }

    private void removeNode(Node<Task> node) {
        if (node == null) return;
        if (node.getPrev() != null) {
            node.getPrev().setNext(node.getNext());
        } else {
            this.head = node.getNext();
        }
        if (node.getNext() != null) {
            node.getNext().setPrev(node.getPrev());
        } else {
            this.tail = node.getPrev();
        }
        placeInspectionTask.remove(node.getData().getId());
    }

    public void linkLast(Task task) {
        Node<Task> newNode = new Node<>(this.tail, task,null);

        if (this.tail == null) {
            this.head = this.tail = newNode;
        } else {
            this.tail.setNext(newNode);
            this.tail = newNode;
        }
        placeInspectionTask.put(task.getId(), newNode);
    }

    public Task getTask(int id) {
        if (placeInspectionTask.containsKey(id)) {
            return placeInspectionTask.get(id).getData();
        }
        return null;
    }

    public ArrayList<Task> getTasks() {
        ArrayList<Task> tasks = new ArrayList<>();
        Node<Task> current = this.tail;

        while (current != null) {
            tasks.add(current.getData());
            current = current.getPrev();
        }
        return tasks;
    }

    public int size() {
        return placeInspectionTask.size();
    }
}