package com.yandex.taskmanagerapp.model;

import com.yandex.taskmanagerapp.service.Managers;
import com.yandex.taskmanagerapp.service.TaskManager;

import java.util.*;

public class LinkedHashMapAnalogue<T> implements List<T> {
    private Node<T> head;
    private Node<T> tail;
    private int size = 0;

    public void addFirst(T element) {
        final Node<T> oldHead = head;
        final Node<T> newNode = new Node<>(null, element, oldHead);
        head = newNode;
        if (oldHead == null)
            tail = newNode;
        else
            oldHead.prev = newNode;
        this.size++;
    }

    public T getFirst() {
        final Node<T> curHead = head;
        if (curHead == null)
            throw new NoSuchElementException();
        return head.data;
    }

    public void addLast(T element) {
        final Node<T> oldTail = tail;
        final Node<T> newNode = new Node<>(tail, element, null);
        tail = newNode;
        this.size++;
        if (this.tail != null) {
            oldTail.next = newNode;
        } else {
            this.head = newNode;
        }
    }

    public T getLast() {
        final Node<T> curTail = tail;
        if (curTail == null)
            throw new NoSuchElementException();
        return tail.data;
    }

    public int size() {
        int resultSize = this.size;
        //if (this.head != null) resultSize++;
        //if (this.tail != null) resultSize++;
        return resultSize;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public boolean contains(Object o) {
        return false;
    }

    @Override
    public Iterator<T> iterator() {
        return null;
    }

    @Override
    public Object[] toArray() {
        return new Object[0];
    }

    @Override
    public <T1> T1[] toArray(T1[] a) {
        return null;
    }

    @Override
    public boolean add(T t) {
        return false;
    }

    @Override
    public boolean remove(Object o) {
        return false;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return false;
    }

    @Override
    public boolean addAll(Collection<? extends T> c) {
        return false;
    }

    @Override
    public boolean addAll(int index, Collection<? extends T> c) {
        return false;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return false;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return false;
    }

    @Override
    public void clear() {

    }

    @Override
    public T get(int index) {
        return null;
    }

    @Override
    public T set(int index, T element) {
        return null;
    }

    @Override
    public void add(int index, T element) {

    }

    @Override
    public T remove(int index) {
        return unlink(node(index));
    }

    @Override
    public int indexOf(Object o) {
        return 0;
    }

    @Override
    public int lastIndexOf(Object o) {
        return 0;
    }

    @Override
    public ListIterator<T> listIterator() {
        return null;
    }

    @Override
    public ListIterator<T> listIterator(int index) {
        return null;
    }

    @Override
    public List<T> subList(int fromIndex, int toIndex) {
        return List.of();
    }

    private T unlinkLast(Node<T> l) {
        final T element = l.data;
        final Node<T> prev = l.prev;
        l.data = null;
        l.prev = null;
        this.tail = prev;
        if (prev == null)
            this.head = null;
        else
            prev.next = null;
        this.size--;
        return element;
    }

    public T removeLast() {
        final Node<T> l = this.tail;
        if (l == null) {
            throw new NoSuchElementException();
        }
        return unlinkLast(l);
    }

    private T unlink(Node<T> x) {
        final T element = x.data;
        final Node<T> next = x.next;
        final Node<T> prev = x.prev;

        if (prev == null) {
            this.head = next;
        } else {
            prev.next = next;
            x.prev = null;
        }
        if (next == null) {
            this.tail = prev;
        } else {
            next.prev = prev;
            x.next = null;
        }
        x.data = null;
        size--;
        return element;
    }

    private Node<T> node(int index) {
        if (index < (size >> 1)) {
            Node<T> x = this.head;
            for (int i = 0; i < index; i++)
                x = x.next;
            return x;
        } else {
            Node<T> x = this.tail;
            for (int i = size - 1; i > index; i--)
                x = x.prev;
            return x;
        }
    }

    public T getValue(int index) {
        return node(index).data;
    }
}
