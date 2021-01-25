import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

import java.util.LinkedList;

public class MoveToFront {

    private static class Bytes {
        private static final int N = 256;

        static LinkedList<Character> order = null;

        static char encode(char i) {
            if (order == null) {
                order = new LinkedList<>();
                for (char j = 0; j < N; j++) {
                    order.addLast(j);
                }
            }

//            Character i = (char) java.lang.Byte.toUnsignedInt(b);
            int idx = order.indexOf(i);
            assert idx < 256 && idx >= 0;
            order.remove(idx);
            order.addFirst(i);
            return (char) idx;
        }

        static char decode(char idx) {
            if (order == null) {
                order = new LinkedList<>();
                for (char j = 0; j < N; j++) {
                    order.addLast(j);
                }
            }

            Character c = order.get(idx);
            order.remove(idx);
            order.addFirst(c);

            return c;
        }

        static void reset() {
            order = null;
        }

    }

    // apply move-to-front encoding, reading from standard input and writing to standard output
    public static void encode() {
        while (!BinaryStdIn.isEmpty()) {
            char b = BinaryStdIn.readChar();
            char c = Bytes.encode(b);
            BinaryStdOut.write(c);
        }
        BinaryStdOut.close();
        Bytes.reset();
    }

    // apply move-to-front decoding, reading from standard input and writing to standard output
    public static void decode() {
        while (!BinaryStdIn.isEmpty()) {
            char c = BinaryStdIn.readChar();
            char b = Bytes.decode(c);
            BinaryStdOut.write(b);
        }
        BinaryStdOut.close();
        Bytes.reset();
    }

    // if args[0] is "-", apply move-to-front encoding
    // if args[0] is "+", apply move-to-front decoding
    public static void main(String[] args) {
        if (args[0].equals("-")) {
            encode();
        } else if (args[0].equals("+")) {
            decode();
        } else {
            throw new IllegalArgumentException();
        }
    }

}