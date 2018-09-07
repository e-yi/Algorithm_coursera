import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.DirectedCycle;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdIn;

import java.util.ArrayList;
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

    private HashMap<String, LinkedList<Integer>> noun2id = new HashMap<>();
    private ArrayList<String> id2noun = new ArrayList<>();
    private SAP sap;
    private Digraph digraph;
    private final int num;

    public WordNet(String synsets, String hypernyms) {
        // constructor takes the name of the two input files
        if (synsets == null || hypernyms == null) {
            throw new IllegalArgumentException();
        }

        int i=0;
        String line;
        In in = new In(synsets);
        while (true) {
            line = in.readLine();
            if (line == null) {
                break;
            }
            String nouns = line.split(",")[1];
            id2noun.add(nouns);
            String[] nounList = nouns.split(" ");
            for (String noun : nounList) {
                if (noun2id.containsKey(noun)) {
                    noun2id.get(noun).add(i);
                } else {
                    LinkedList<Integer> list = new LinkedList<>();
                    list.add(i);
                    noun2id.put(noun, list);
                }
            }

            i++;
        }
        in.close();
        num = i;
        //init digraph
        digraph = new Digraph(num);

        in = new In(hypernyms);
        while (true){
            line = in.readLine();
            if (line==null){
                break;
            }
            i =Integer.parseInt(line.split(",")[0]);
            String[] ids = line.split(",");
            for (int j=1;j<ids.length;j++){
                int hypernym = Integer.parseInt(ids[j]);
                digraph.addEdge(i,hypernym);
            }
            i++;
        }
        in.close();

        //if is not a DAG throw exception
        boolean isDAG = false;
        for (int j = 0; j < num; j++) {
            if (digraph.outdegree(j)==0){
                if (!isDAG){
                    //handle multiple roots situations
                    isDAG=true;
                }else {
                    throw new IllegalArgumentException();
                }
            }
        }
        if (isDAG){
            DirectedCycle cycle = new DirectedCycle(digraph);
            if (cycle.hasCycle()){
                isDAG = false;
            }
        }
        if (!isDAG){
            throw new IllegalArgumentException();
        }

        sap = new SAP(digraph);
    }

    public Iterable<String> nouns() {
        // returns all WordNet nouns
        return noun2id.keySet();
    }

    public boolean isNoun(String word) {
        // is the word a WordNet noun?
        if (word==null){
            throw new IllegalArgumentException();
        }
        return noun2id.containsKey(word);
    }

    public int distance(String nounA, String nounB) {
        if (nounA==null||nounB==null){
            throw new IllegalArgumentException();
        }
        // distance between nounA and nounB (defined below)
        Iterable<Integer> a = noun2id.get(nounA);
        Iterable<Integer> b = noun2id.get(nounB);
        if (a==null||b==null){
            throw new IllegalArgumentException();
        }
        return sap.length(a,b);
    }

    public String sap(String nounA, String nounB) {
        // a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
        // in a shortest ancestral path (defined below)
        if (nounA==null||nounB==null){
            throw new IllegalArgumentException();
        }
        Iterable<Integer> a = noun2id.get(nounA);
        Iterable<Integer> b = noun2id.get(nounB);
        if (a==null||b==null) {
            throw new IllegalArgumentException();
        }
        int idx = sap.ancestor(a,b);
        return id2noun.get(idx);
    }

    public static void main(String[] args){
        // do unit testing of this class
        WordNet wordNet = new WordNet(
                "./testFile/wordnet/synsets15.txt", "./testFile/wordnet/hypernyms15Tree.txt");
    }

}