package project20280.list;

import project20280.interfaces.List;

import java.util.Iterator;

public class DoublyLinkedList<E> implements List<E> {

    private static class Node<E> {
        private final E data;
        private Node<E> next;
        private Node<E> prev;

        public Node(E e, Node<E> p, Node<E> n) {
            data = e;
            prev = p;
            next = n;
        }

        public E getData() {
            return data;
        }

        public Node<E> getNext() {
            return next;
        }

        public Node<E> getPrev() {
            return prev;
        }

        public void setNext(Node<E> n) {
            next = n;
        }

        public void setPrev(Node<E> n) {
            prev = n;
        }
    }


    private Node<E> head;
    private Node<E> tail;
    private int size = 0;


    public DoublyLinkedList() {
        head = new Node<E>(null, null, null);
        tail = new Node<E>(null, head, null);
        head.next = tail;
    }


    private void addBetween(E e, Node<E> pred, Node<E> succ) {
        // TODO
        Node<E> newest = new Node<>(e, pred, succ);
        pred.setNext(newest);
        succ.setPrev(newest);
        size++;
    }

    @Override
    public int size () {
        // TODO
        return size;

    }

    @Override
    public boolean isEmpty () {
        // TODO
        return size == 0;

    }

    @Override
    public E get ( int i){
        // TODO
        if (i < 0 || i >= size) {
            throw new IndexOutOfBoundsException();
        }

        if (isEmpty()) {
            return null;
        }

        int index = 0;
        E element;

        if (i < size / 2) {
            Node<E> curr = head.getNext();
            while (index != i) {
                curr = curr.getNext();
                index++;
            }
            element = curr.getData();
        } else {
            index = size - 1;
            Node<E> curr = tail.getPrev();
            while (index != i) {
                curr = curr.getPrev();
                index--;
            }
            element = curr.getData();
        }

        return element;
    }

    @Override
    public void add ( int i, E e){
        // TODO
        if (i < 0 || i >= size) {
            throw new IndexOutOfBoundsException();
        }

        if (i == 0) {
            addFirst(e);
        } else if (i == size - 1) {
            addLast(e);
        } else {
            int index = 0;
            Node<E> curr;

            if (i < size / 2) {
                curr = head.getNext();
                while (index != i) {
                    curr = curr.getNext();
                    index++;
                }
            } else {
                index = size - 1;
                curr = tail.getPrev();
                while (index != i) {
                    curr = curr.getPrev();
                    index--;
                }
            }

            Node<E> newest = new Node<>(e, curr.getPrev(), curr);
            curr.getPrev().setNext(newest);
            curr.setPrev(newest);
            size++;
        }
    }

    @Override
    public E remove ( int i){
        // TODO
        if (i < 0 || i >= size) {
            return null;
        }
        if (isEmpty()) {
            return null;
        }

        if (i == 0) {
            return removeFirst();
        } else if (i == size - 1) {
            return removeLast();
        } else {
            int index = 0;
            Node<E> curr;

            if (i < size / 2) {
                curr = head.getNext();
                while (index != i) {
                    curr = curr.getNext();
                    index++;
                }
            } else {
                index = size - 1;
                curr = tail.getPrev();
                while (index != i) {
                    curr = curr.getPrev();
                    index--;
                }
            }

            curr.getPrev().setNext(curr.getNext());
            curr.getNext().setPrev(curr.getPrev());
            size--;
        }


        return null;
    }

    private class DoublyLinkedListIterator<E> implements Iterator<E> {
        Node<E> curr = (Node<E>) head.next;

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
    public Iterator<E> iterator () {
        return new DoublyLinkedListIterator<E>();
    }

    private E remove (Node < E > n) {
        // TODO
        Node<E> curr = head;
        E element = null;
        if (n == head) {
            return removeFirst();
        } else if (tail == n) {
            return removeLast();
        } else {
            while (curr.getNext() != null) {
                if (curr == n) {
                    element = curr.getData();
                    curr.getPrev().setNext(curr.getNext());
                    curr.getNext().setPrev(curr.getPrev());
                }
                curr = curr.getNext();
            }
        }

        size--;
        return element;
    }

    public E first () {
        if (isEmpty()) {
            return null;
        }
        return head.next.getData();
    }

    public E last () {
        // TODO

        if (isEmpty()) {
            return null;
        }
        return tail.prev.getData();

    }

    @Override
    public E removeFirst () {
        // TODO
        E element;
        if (size == 1) {
            element = head.getNext().getData();
            head.setNext(tail);
            tail.setPrev(head);
        } else {
            element = head.getNext().getData();
            head.setNext(head.getNext().getNext());
            head.getNext().setPrev(head);
        }
        size--;
        return element;
    }

    @Override
    public E removeLast () {
        // TODO
        E element = tail.getPrev().getData();

        tail = tail.getPrev();
        tail.setNext(null);
        size--;
        return element;
    }

    @Override
    public void addLast (E e){
        // TODO
        addBetween(e, tail.getPrev(), tail);
    }

    @Override
    public void addFirst (E e){
        // TODO
        addBetween(e, head, head.getNext());
    }

    public String toString () {
        StringBuilder sb = new StringBuilder("[");
        Node<E> curr = head.next;
        while (curr != tail) {
            sb.append(curr.data);
            curr = curr.next;
            if (curr != tail) {
                sb.append(", ");
            }
        }
        sb.append("]");
        return sb.toString();
    }

    public static void main (String[]args){
        DoublyLinkedList<Integer> ll = new DoublyLinkedList<Integer>();
        ll.addFirst(0);
        ll.addFirst(1);
        ll.addFirst(2);
        ll.addLast(-1);
        System.out.println(ll);

        ll.removeFirst();
        System.out.println(ll);

        ll.removeLast();
        System.out.println(ll);

        for (Integer e : ll) {
            System.out.println("value: " + e);
        }
    }
}