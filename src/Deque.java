import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Throw a java.lang.IllegalArgumentException if the client calls either addFirst() or addLast() with a null argument.
 * Throw a java.util.NoSuchElementException if the client calls either removeFirst() or removeLast when the deque is empty.
 * Throw a java.util.NoSuchElementException if the client calls the next() method in the iterator when there are no more items to return.
 * Throw a java.lang.UnsupportedOperationException if the client calls the remove() method in the iterator.
 *
 * @param <Item>
 */


public class Deque<Item> implements Iterable<Item> {

    private class Node {
        private Item item;
        private Node front;
        private Node back;
    }

    private Node first;
    private Node last;
    private int num;

    public Deque() {
        first = null;
        last = null;
        num = 0;
    }                           // construct an empty deque

    public boolean isEmpty() {
        return num == 0;
    }                 // is the deque empty?

    public int size() {
        return num;
    }                      // return the number of items on the deque

    public void addFirst(Item item) {
        if (item == null) {
            throw new IllegalArgumentException();
        }
        Node node = new Node();
        node.item = item;
        if (num==0){
            first=node;
            last=node;
            num++;
            return;
        }
        first.front = node;
        node.back = first;
        first = node;
        num++;
    }          // add the item to the front

    public void addLast(Item item) {
        if (item == null) {
            throw new IllegalArgumentException();
        }
        Node node = new Node();
        node.item = item;
        if (num==0){
            first=node;
            last=node;
            num++;
            return;
        }
        last.back = node;
        node.front = last;
        last = node;
        num++;
    }           // add the item to the end

    public Item removeFirst() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        Node node = first;
        if (num==1){
            first =null;
            last = null;
            return node.item;
        }
        first = node.back;
        first.front = null;
        num--;
        return node.item;
    }               // remove and return the item from the front

    public Item removeLast() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }

        Node node = last;
        if (num==1){
            first =null;
            last = null;
            return node.item;
        }
        last = node.front;
        last.back = null;
        num--;
        return node.item;
    }                 // remove and return the item from the end

    private class DequeIterator implements Iterator {
        private Node current;

        public DequeIterator(Node first) {
            current = first;
        }

        @Override
        public boolean hasNext() {
            return current != null;
        }

        @Override
        public Object next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            return current.back;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    @Override
    public Iterator<Item> iterator() {
        return new DequeIterator(first);
    }         // return an iterator over items in order from front to end

    public static void main(String[] args) {

    }   // unit testing (optional)
}
