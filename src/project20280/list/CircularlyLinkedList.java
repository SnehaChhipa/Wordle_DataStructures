package project20280.list;

import project20280.interfaces.List;

import java.util.Iterator;

public class CircularlyLinkedList<E> implements List<E> {

    private class Node<T> {
        private final T data;
        private Node<T> next;

        public Node(T e, Node<T> n) {
            data = e;
            next = n;
        }

        public T getData() {
            return data;
        }

        public void setNext(Node<T> n) {
            next = n;
        }

        public Node<T> getNext() {
            return next;
        }
    }

    private Node<E> tail = null;
    private int size = 0;

    public CircularlyLinkedList() {

    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public E get(int i) {
        // TODO
        int index = 0;
        Node<E> curr = tail;
        while (index != i) {
            curr = curr.getNext();
            index++;
        }
        return curr.getNext().getData();
    }

    /**
     * Inserts the given element at the specified index of the list, shifting all
     * subsequent elements in the list one position further to make room.
     *
     * @param i the index at which the new element should be stored
     * @param e the new element to be stored
     */
    @Override
    public void add(int i, E e) {
        // TODO

        if (i == size) {
            addLast(e);
        }
        else if (i == 0) {
            addFirst(e);
        }
        else if (i < 0 || i > size) {
            throw new IllegalStateException();
        }
        else {
            int index = 0;
            Node<E> curr = tail;
            while (index != i) {
                curr = curr.getNext();
                index++;
            }
            Node<E> newest = new Node<>(e, curr.getNext());
            curr.setNext(newest);
            size++;
        }
    }

    @Override
    public E remove(int i) {
        // TODO
        E element;

        if (i == size-1) {
            return removeLast();
        }
        else if (i == 0) {
            return removeFirst();
        }
        else if (i < 0 || i >= size) {
            throw new IllegalStateException();
        }
        else {
            int index = 0;
            Node<E> curr = tail;
            while (index != i) {
                curr = curr.getNext();
                index++;
            }
            element = curr.getNext().getData();
            curr.setNext(curr.getNext().getNext());
            size--;
        }
        return element;
    }

    public void rotate() {
        // TODO
        if (tail != null) {
            tail = tail.getNext();
        }
    }

    private class CircularlyLinkedListIterator<E> implements Iterator<E> {
        Node<E> curr = (Node<E>) tail;

        @Override
        public boolean hasNext() {
            return curr != tail;
        }

        @Override
        public E next() {
            E res = curr.data;
            curr = curr.next;
            return res;
        }

    }

    @Override
    public Iterator<E> iterator() {
        return new CircularlyLinkedListIterator<E>();
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public E removeFirst() {
        // TODO
        E element;
        if (isEmpty()) {
            return null;
        }
        else if (size == 1) {
            element = tail.getData();
            tail = null;
        }
        else {
            element = tail.getNext().getData();
            tail.setNext(tail.getNext().getNext());

        }
        size--;
        return element;
    }

    @Override
    public E removeLast() {
        // TODO
        E element;
        if (size == 0 || size == 1) {
            return removeFirst();
        } else {
            Node<E> curr = tail;
            while (curr.getNext() != tail) {
                curr = curr.getNext();
            }
            element = curr.getNext().getData();
            tail = curr.getNext().getNext();
            curr.setNext(tail);
        }
        size--;
        return element;
    }

    @Override
    public void addFirst(E e) {
        // TODO
        if (size == 0) {
            tail = new Node<>(e, tail);
            tail.setNext(tail);
        }
        else {
            Node<E> newest = new Node<>(e, tail.getNext());
            tail.setNext(newest);
        }
        size++;
    }

    @Override
    public void addLast(E e) {
        // TODO
        addFirst(e);
        tail = tail.getNext();
    }


    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        Node<E> curr = tail;
        do {
            curr = curr.next;
            sb.append(curr.data);
            if (curr != tail) {
                sb.append(", ");
            }
        } while (curr != tail);
        sb.append("]");
        return sb.toString();
    }


    public static void main(String[] args) {
        CircularlyLinkedList<Integer> ll = new CircularlyLinkedList<Integer>();
        for (int i = 10; i < 20; ++i) {
            ll.addLast(i);
        }

        System.out.println(ll);

        ll.removeFirst();
        System.out.println(ll);

        ll.removeLast();
        System.out.println(ll);

        ll.rotate();
        System.out.println(ll);

        ll.removeFirst();
        ll.rotate();
        System.out.println(ll);

        ll.removeLast();
        ll.rotate();
        System.out.println(ll);

        for (Integer e : ll) {
            System.out.println("value: " + e);
        }

    }
}
