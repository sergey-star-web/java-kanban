package com.yandex.taskmanagerapp.service;

import com.yandex.taskmanagerapp.model.Node;
import com.yandex.taskmanagerapp.model.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;

public class InMemoryHistoryManager implements HistoryManager {
    private HashMap<Integer, Node<Task>> placeInspectionTask = new HashMap<>();
    private Node tail;
    private Node head;

    @Override
    public void add(Task task) {
        Node<Task> node = placeInspectionTask.remove(task.getId());
        removeNode(node);
        placeInspectionTask.put(task.getId(), linkLast(task));
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
    }

    @Override
    public void remove(int id) {
        Node<Task> node = placeInspectionTask.remove(id);
        if (node != null) {
            removeNode(node);
        }
    }

    @Override
    public ArrayList<Task> getHistory() {
        return getTasks();
    }

    public Node<Task> linkLast(Task task) {
        Node<Task> newNode = new Node<>(this.tail, task,null);

        if (this.tail == null) {
            this.tail = newNode;
            this.head = this.tail;
        } else {
            this.tail.setNext(newNode);
            this.tail = newNode;
        }
        return newNode;
    }

    public Task getTask(int id) {
        Node<Task> node = placeInspectionTask.get(id);
        Optional<Task> taskOptional = Optional.ofNullable(node).map(Node::getData);
        return taskOptional.orElse(null);
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