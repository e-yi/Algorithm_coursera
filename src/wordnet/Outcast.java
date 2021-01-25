import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class Outcast {
    private WordNet net;

    public Outcast(WordNet wordnet) {
        // constructor takes a WordNet object
        net = wordnet;
    }

    public String outcast(String[] nouns) {
        // given an array of WordNet nouns, return an outcast
        int[] dis = new int[nouns.length];
        for (int i = 0; i < nouns.length; i++) {
            for (int j = 0; j < nouns.length; j++) {
                dis[i] += net.distance(nouns[i], nouns[j]);
            }
        }
        int out = -1;
        int maxDis = Integer.MIN_VALUE;
        for (int i = 0; i < nouns.length; i++) {
            if (dis[i] > maxDis) {
                maxDis = dis[i];
                out = i;
            }
        }
        return nouns[out];
    }

    public static void main(String[] args) {
        WordNet wordnet = new WordNet(args[0], args[1]);
        Outcast outcast = new Outcast(wordnet);
        for (int t = 2; t < args.length; t++) {
            In in = new In(args[t]);
            String[] nouns = in.readAllStrings();
            StdOut.println(args[t] + ": " + outcast.outcast(nouns));
        }
    }
}