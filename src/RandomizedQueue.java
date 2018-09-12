import edu.princeton.cs.algs4.StdRandom;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class RandomizedQueue<Item> implements Iterable<Item> {

    private Item[] items;
    private int n;
    private int front;

    public RandomizedQueue() {                // construct an empty randomized queue
        items = (Item[]) new Object[2];
        n=0;
        front=0;
    }

    private void resize(int capacity) {
        assert capacity >= n;
        Item[] temp = (Item[]) new Object[capacity];
        int j=0;
        for (Item item:items) {
            if (item==null){
                continue;
            }
            temp[j] = item;
            j++;
        }
        items = temp;
        front = j;
    }

    public boolean isEmpty() {                 // is the randomized queue empty?
        return n==0;
    }

    public int size() {                       // return the number of items on the randomized queue
        return n;
    }

    public void enqueue(Item item) {           // add the item
        if (item==null){
            throw new IllegalArgumentException();
        }
        if (n==items.length){
            resize(n*2);
        }
        if (front==items.length){
            resize(front);
        }
        items[front] = item;
        n++;
        front++;
    }

    public Item dequeue() {                   // remove and return a random item
        if (isEmpty()){
            throw new NoSuchElementException();
        }
        Item item;
        while (true) {
            int i = StdRandom.uniform(front);
            item = items[i];
            if (item!=null){
                items[i]=null;
                break;
            }
        }
        n--;
        if (n<items.length/4){
            resize(items.length/2);
        }
        return item;
    }

    public Item sample() {                    // return a random item (but do not remove it)
        if (isEmpty()){
            throw new NoSuchElementException();
        }
        while (true) {
            int i = StdRandom.uniform(front);
            Item item = items[i];
            if (item!=null){
                return item;
            }
        }
    }

    @Override
    public Iterator<Item> iterator() {        // return an independent iterator over items in random order
        return new RandomizedQueueIterator(items,front);
    }

    private class RandomizedQueueIterator  implements Iterator{

        private Item[] items;
        private final int front;
        private int current;

        public RandomizedQueueIterator(Item[] items,int front) {
            this.front=front;
            this.items=items;
            this.current=0;
        }

        @Override
        public boolean hasNext() {
            return current<front;
        }

        @Override
        public Object next() {
            if (!hasNext()){
                throw new NoSuchElementException();
            }
            return items[current++];
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }

    }

    public static void main(String[] args) {  // unit testing (optional)
    }
}