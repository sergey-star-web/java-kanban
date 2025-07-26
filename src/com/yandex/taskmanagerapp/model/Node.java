package com.yandex.taskmanagerapp.model;

public class Node<T> {
    private T data;
    private Node next;
    private Node prev;

    public Node(Node prev, T data, Node next) {
        this.data = data;
        this.next = next;
        this.prev = prev;
    }

    public void setNext(Node next) {
        this.next = next;
    }

    public void setPrev(Node prev) {
        this.prev = prev;
    }

    public void setData(T data) {
        this.data = data;
    }

    public Node getPrev() {
        return this.prev;
    }

    public Node getNext() {
        return this.next;
    }

    public T getData() {
        return this.data;
    }

}