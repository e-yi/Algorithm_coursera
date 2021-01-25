import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;


/**
 * Write a client program Permutation.java that takes an integer k as a command-line argument;
 * reads in a sequence of strings from standard input using StdIn.readString(); and prints
 * exactly k of them, uniformly at random. Print each item from the sequence at most once.
 */
public class Permutation {

    public static void main(String[] args) {
        RandomizedQueue<String> queue = new RandomizedQueue<>();
        int k = Integer.parseInt(args[0]);
        while (!StdIn.isEmpty()) {
            String s = StdIn.readString();
            queue.enqueue(s);
        }
        for (int i = 0; i < k; i++) {
            StdOut.print(queue.dequeue() + '\n');
        }
    }
}
