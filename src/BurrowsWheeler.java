import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

public class BurrowsWheeler {
    private static final int N = 256;

    // apply Burrows-Wheeler transform,
    // reading from standard input and writing to standard output
    public static void transform() {
        String s = BinaryStdIn.readString();
        CircularSuffixArray csa = new CircularSuffixArray(s);
        int n = csa.length();
        for (int i = 0; i < n; i++) {
            if (csa.index(i) == 0) {
                BinaryStdOut.write(i);
                break;
            }
        }
        for (int i = 0; i < n; i++) {
            BinaryStdOut.write(s.charAt((csa.index(i) + n - 1) % n));
        }
        BinaryStdOut.close();
    }

    // apply Burrows-Wheeler inverse transform,
    // reading from standard input and writing to standard output
    public static void inverseTransform() {
        int first = BinaryStdIn.readInt();
        char[] s = BinaryStdIn.readString().toCharArray();
        int n = s.length;

        int[] count = new int[N + 1];
        for (char c : s) {
            count[c + 1]++;
        }
        for (int i = 0; i < N; i++) {
            count[i + 1] += count[i];
        }

        int[] next = new int[n];
        char[] h = new char[n];
        for (int i = 0; i < n; i++) {
            int p = count[s[i]]++;
            h[p] = s[i];
            next[p] = i;
        }

        int cp = first;
        for (int i = 0; i < n; i++) {
            BinaryStdOut.write(h[cp]);
            cp = next[cp];
        }
        BinaryStdOut.close();
    }

    // if args[0] is "-", apply Burrows-Wheeler transform
    // if args[0] is "+", apply Burrows-Wheeler inverse transform
    public static void main(String[] args) {
        if (args[0].equals("-")) {
            transform();
        } else if (args[0].equals("+")) {
            inverseTransform();
        } else {
            throw new IllegalArgumentException();
        }
    }

}