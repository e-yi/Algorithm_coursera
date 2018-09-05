import edu.princeton.cs.algs4.Digraph;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;

//Corner cases.  Throw a java.lang.IllegalArgumentException in the following situations:
//        Any argument to the constructor or an instance method is null
//        The input to the constructor does not correspond to a rooted DAG.
//        Any of the noun arguments in distance() or sap() is not a WordNet noun.
//        You may assume that the input files are in the specified format.
//Performance requirements.  Your data type should use space linear in the input size
//        (size of synsets and hypernyms files). The constructor should take time linearithmic
//        (or better) in the input size. The method isNoun() should run in time logarithmic
//        (or better) in the number of nouns. The methods distance() and sap() should run in
//        time linear in the size of the WordNet digraph. For the analysis, assume that the
//        number of nouns per synset is bounded by a constant.


public class WordNet {

    private HashMap<String, LinkedList<Integer>> hashMap;
    private Digraph digraph;
    private final int num;

    public WordNet(String synsets, String hypernyms)
            throws IOException {
        // constructor takes the name of the two input files
        if (synsets == null || hypernyms == null) {
            throw new IllegalArgumentException();
        }

        hashMap = new HashMap<>();

        String line;
        BufferedReader bufferedReader = new BufferedReader(
                new FileReader(new File(synsets)));
        int i = 0;
        while (true) {
            line = bufferedReader.readLine();
            if (line == null) {
                break;
            }
            String[] nones = line.split(",")[1]
                    .split(" ");
            for (String none : nones) {
                if (hashMap.containsKey(none)) {
                    hashMap.get(none).add(i);
                } else {
                    LinkedList<Integer> list = new LinkedList<>();
                    list.add(i);
                    hashMap.put(none, list);
                }
            }

            i++;
        }
        bufferedReader.close();
        num = i+1;
        //init digraph
        digraph = new Digraph(num);

        bufferedReader = new BufferedReader(
                new FileReader(new File(synsets)));
        i =0;
        while (true){
            line = bufferedReader.readLine();
            if (line==null){
                break;
            }
            String[] ids = line.split(",");
            for (int j=1;j<ids.length;j++){
                int hypernym = Integer.parseInt(ids[j]);
                digraph.addEdge(i,hypernym);
            }
            i++;
        }
        bufferedReader.close();
    }

    public Iterable<String> nouns() {
        // returns all WordNet nouns
        return hashMap.keySet();
    }

    public boolean isNoun(String word) {
        // is the word a WordNet noun?
        return hashMap.containsKey(word);
    }

    public int distance(String nounA, String nounB) {
        // distance between nounA and nounB (defined below)
        return 0;
    }

    public String sap(String nounA, String nounB) {
        // a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
        // in a shortest ancestral path (defined below)
        return null;
    }

    public static void main(String[] args) throws IOException {
        // do unit testing of this class
        WordNet wordNet = new WordNet(
                "./testFile/wordnet/synsets15.txt", "");
    }

}