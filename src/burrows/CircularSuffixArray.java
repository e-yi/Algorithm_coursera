import java.util.Arrays;

public class CircularSuffixArray {

    private int n;
    private Integer[] pointers;

    // circular suffix array of s
    public CircularSuffixArray(String s) {
        if (s == null) {
            throw new IllegalArgumentException();
        }

        n = s.length();

//        StringBuffer ss = new StringBuffer(s);

        pointers = new Integer[n];
        for (int i = 0; i < n; i++) {
            pointers[i] = i;
        }

        Arrays.sort(pointers, (a, b) -> {
            for (int i = 0; i < n; i++) {
                char aa = s.charAt((a + i) % n);
                char bb = s.charAt((b + i) % n);
                if (aa == bb) {
                    continue;
                }
                return aa > bb ? 1 : -1;
            }
            return 0;
        });

    }

    // length of s
    public int length() {
        return n;
    }

    // returns index of ith sorted suffix
    public int index(int i) {
        if (i < 0 || i >= n) {
            throw new IllegalArgumentException();
        }

        return pointers[i];
    }

    // unit testing (required)
    public static void main(String[] args) {
        CircularSuffixArray a = new CircularSuffixArray("ABRACADABRA!");
        System.out.println(a.length());
        for (int i = 0; i < "ABRACADABRA!".length(); i++) {
            System.out.println(a.index(i));
        }

    }

}