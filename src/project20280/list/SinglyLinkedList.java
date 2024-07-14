package project20280.list;

import project20280.interfaces.List;

import java.util.Iterator;

public class SinglyLinkedList<E> implements List<E> {

    private static class Node<E> {

        private final E element;            // reference to the element stored at this node

        /**
         * A reference to the subsequent node in the list
         */
        private Node<E> next;         // reference to the subsequent node in the list

        /**
         * Creates a node with the given element and next node.
         *
         * @param e the element to be stored
         * @param n reference to a node that should follow the new node
         */
        public Node(E e, Node<E> n) {
            element = e;
            next = n;
        }

        // Accessor methods

        /**
         * Returns the element stored at the node.
         *
         * @return the element stored at the node
         */
        public E getElement() {
            return element;
        }

        /**
         * Returns the node that follows this one (or null if no such node).
         *
         * @return the following node
         */
        public Node<E> getNext() {
            return next;
        }

        // Modifier methods

        /**
         * Sets the node's next reference to point to Node n.
         *
         * @param n the node that should follow this one
         */
        public void setNext(Node<E> n) {
            next = n;
        }
    } //----------- end of nested Node class -----------

    /**
     * The head node of the list
     */
    private Node<E> head = null;               // head node of the list (or null if empty)


    /**
     * Number of nodes in the list
     */
    private int size = 0;                      // number of nodes in the list

    public SinglyLinkedList() {
    }              // constructs an initially empty list

    //@Override
    public int size() {
        // TODO
        return size;
    }

    //@Override
    public boolean isEmpty() {
        // TODO
        return size == 0;
    }

    @Override
    public E get(int position) {
        // TODO<
        if (position < 0 || position >= size) {
            System.out.println(position);
            throw new IllegalArgumentException("Position Invalid");
        }

        if (isEmpty()) {
            return null;
        }

        Node<E> curr = head;
        E element = null;

        for (int i = 0; i < size; i++) {
            if (i == position) {
                element = curr.getElement();
            }
            curr = curr.getNext();
        }

        return element;
    }

    @Override
    public void add(int position, E e) {
        // TODO
        if (position < 0 || position >= size) {
            throw new IllegalArgumentException("Position Invalid");
        }

        int index = 1;

        if (position == 0) {
            addFirst(e);
        }
        else if (position == size - 1) {
            addLast(e);
        }
        else {
            Node<E> curr = head.getNext();
            Node<E> prev = head;

            while (curr.getNext() != null) {
                if (position == index) {
                    Node<E> newNode = new Node<>(e, curr);
                    prev.setNext(newNode);
                    break;
                }
                prev = curr;
                curr = curr.getNext();
                index++;
            }
        }

        size++;
    }


    @Override
    public void addFirst(E e) {
        // TODO
        head = new Node<E>(e, head);
        size++;
    }

    @Override
    public void addLast(E e) {
        // TODO
        Node<E> newest = new Node<E>(e, null);
        Node<E> last = head;
        if (last == null) {
            head = newest;
        }
        else {
            while (last.getNext() != null) {
                last = last.getNext();
            }
            last.setNext(newest);
        }
        size++;
    }

    @Override
    public E remove(int position) {
        // TODO
        if (position < 0 || position >= size) {
            throw new IllegalArgumentException("Position Invalid");
        }
        if (isEmpty()) {
            return null;
        }

        int index = 1;

        if (position == 0) {
            return removeFirst();
        }

        if (position == size-1) {
            return removeLast();
        }

        Node<E> curr = head.getNext();
        Node<E> prev = head;
        E element = null;


        while (curr.getNext() != null) {
            if (position == index) {
                element = curr.getElement();
                prev.setNext(curr.getNext());
                break;
            }
            prev = curr;
            curr = curr.getNext();
            index++;
        }
        size--;
        return element;
    }

    @Override
    public E removeFirst() {
        // TODO
        if (isEmpty()) {
            return null;
        }

        E element = head.getElement();
        head = head.getNext();
        size--;

        return element;
    }

    @Override
    public E removeLast() {
        // TODO
        if (isEmpty()) {
            return null;
        }

        E element;
        Node<E> curr = head;

        if (curr.getNext() == null) {
            element = head.getElement();
            head = null;
        }
        else {
            Node<E> prev = null;
            while(curr.getNext() != null) {
                prev = curr;
                curr = curr.getNext();
            }

            element = curr.getElement();
            prev.setNext(null);
        }

        size--;
        return element;
    }

    //@Override
    public Iterator<E> iterator() {
        return new SinglyLinkedListIterator<E>();
    }

    private class SinglyLinkedListIterator<E> implements Iterator<E> {
        Node<E> curr = (Node<E>) head;

        @Override
        public boolean hasNext() {
            return curr != null;
        }

        @Override
        public E next() {
            E res = curr.getElement();
            curr = curr.next;
            return res;
        }
    }

    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        Node<E> curr = head;
        while (curr != null) {
            sb.append(curr.getElement());
            if (curr.getNext() != null)
                sb.append(", ");
            curr = curr.getNext();
        }
        sb.append("]");
        return sb.toString();
    }

    public static void main(String[] args) {
        SinglyLinkedList<Integer> ll = new SinglyLinkedList<Integer>();
        System.out.println("ll " + ll + " isEmpty: " + ll.isEmpty());
        //LinkedList<Integer> ll = new LinkedList<Integer>();

        ll.addFirst(0);
        ll.addFirst(1);
        ll.addFirst(2);
        ll.addFirst(3);
        ll.addFirst(4);
        ll.addLast(-1);
        //ll.removeLast();
        //ll.removeFirst();
        //System.out.println("I accept your apology");
        //ll.add(3, 2);
        System.out.println(ll);
        ll.remove(5);
        System.out.println(ll);

    }
}
