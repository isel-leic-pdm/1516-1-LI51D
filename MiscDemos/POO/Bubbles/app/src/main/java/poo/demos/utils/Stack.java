package poo.demos.utils;

import java.util.Iterator;

/**
 * Implementation of a container with LIFO discipline supported by a
 * single-linked list.
 *
 * @param <T> The type of elements stored in the stack.
 */
public class Stack<T> implements Iterable<T> {

    private static class Node<T> {

        public Node<T> next;
        public final T elem;
        public Node(T elem) {
            this.elem = elem;
        }
    }

    /** The stack's top, or {@value null} if the stack is empty. */
    private Node<T> top;
    /** The number of elements in the stack. */
    private int size;

    /**
     * Adds the given element to the top of the stack.
     * @param elem The element to be pushed.
     * @throws IllegalArgumentException if a elem is null
     */
    public void push(T elem) {

        if(elem == null)
            throw new IllegalArgumentException();

        final Node<T> newNode = new Node<T>(elem);
        newNode.next = top;
        top = newNode;
        size += 1;
    }

    /**
     * Removes the element at the top of the stack, if one exists.
     * @return The removed element.
     * @throws IllegalStateException if the stack is empty.
     */
    public T pop() {

        if(size() == 0)
            throw new IllegalStateException();

        final T result = top.elem;
        top = top.next;
        size -= 1;
        return result;
    }

    /**
     * Gets the stack size.
     * @return The number of elements in the stack.
     */
    public int size() {
        return size;
    }

    /**
     * Gets an iterator for the current stack instance. The produced iterator
     * does not support removal.
     * @return The iterator instance.
     */
    @Override
    public Iterator<T> iterator() {

        return new Iterator<T>() {

            private Node<T> current = top;

            @Override
            public boolean hasNext() {
                return current != null;
            }

            @Override
            public T next() {
                final T result = current.elem;
                current = current.next;
                return result;
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }
}
