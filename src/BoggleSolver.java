import edu.princeton.cs.algs4.TrieST;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

import java.util.HashSet;
import java.util.Set;


public class BoggleSolver {

    private TrieST<Boolean> trieST;

    // Initializes the data structure using the given array of strings as the dictionary.
    // (You can assume each word in the dictionary contains only the uppercase letters A through Z.)
    public BoggleSolver(String[] dictionary) {
        trieST = new TrieST<>();
        for (String word:dictionary) {
            trieST.put(word, true);
        }
    }

    // Returns the set of all valid words in the given Boggle board, as an Iterable.
    public Iterable<String> getAllValidWords(BoggleBoard board) {
        if (board == null) {
            throw new IllegalArgumentException();
        }

        int row = board.rows();
        int col = board.cols();

        Set<String> validWords = new HashSet<>();

        boolean[] path = new boolean[row*col];

        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                dfsBoard(i,j,"",path,validWords,board);
            }
        }

        return validWords;
    }

    /**
     *
     * @param i
     * @param j
     * @param prefix
     * @param set
     * @param board
     */
    private void dfsBoard(int i,int j,String prefix,boolean[] path,
                          Set<String> set,BoggleBoard board){
        // 合法性检查
        if (i<0||j<0||i>=board.rows()||j>=board.cols()){
            return;
        }

        // 检查是否已经使用过
        if(path[i*board.cols()+j]){
            return;
        }

        String tail = String.valueOf(board.getLetter(i,j));
        if (tail.equals("Q")){
            tail = tail.concat("U");
        }

        String s = prefix.concat(tail);

        // 检查是否有希望
        if(!trieST.keysWithPrefix(s).iterator().hasNext()){
            return;
        }

        // 检查是否成功
        if (trieST.get(s)!=null && s.length()>2){
            set.add(s);
        }

        boolean[] myPath = path.clone();
        myPath[i*board.cols()+j] = true;

        for (int k = -1; k < 2; k++) {
            for (int l = -1; l < 2; l++) {
                dfsBoard(i+k,j+l,s,myPath,set,board);
            }
        }
    }


    // Returns the score of the given word if it is in the dictionary, zero otherwise.
    // (You can assume the word contains only the uppercase letters A through Z.)
    public int scoreOf(String word) {
        if (!trieST.contains(word)){
            return 0;
        }

        int score;
        int len = word.length();

        if (len>=8){
            score = 11;
        }else if (len>=7){
            score = 5;
        }else if (len>=6){
            score = 3;
        }else if (len>=5){
            score = 2;
        }else if (len>=3){
            score = 1;
        }else {
            score = 0;
        }

        return score;
    }

    public static void main(String[] args) {
        In in = new In(args[0]);
        String[] dictionary = in.readAllStrings();
        BoggleSolver solver = new BoggleSolver(dictionary);
        BoggleBoard board = new BoggleBoard(args[1]);
        int score = 0;
        for (String word : solver.getAllValidWords(board)) {
            StdOut.println(word);
            score += solver.scoreOf(word);
        }
        StdOut.println("Score = " + score);
    }


}
