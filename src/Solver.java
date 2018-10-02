import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;

public class Solver {

    private int moves;
    private boolean isSolvable;
    private MinPQ<Node> minPQ;
    private MinPQ<Node> twinMinPQ;
    private Stack<Board> solution;

    public Solver(Board initial) {
        // find a solution to the initial board (using the A* algorithm)
        minPQ = new MinPQ<>();
        Node init = new Node(null, initial, 0);
        minPQ.insert(init);
        twinMinPQ = new MinPQ<>();
        Node twinInit = new Node(null, init.board.twin(), 0);
        twinMinPQ.insert(twinInit);

        while (true) {
            Node goal = step(minPQ);
            if (goal != null) {
                isSolvable = true;
                getSolution(goal);
                break;
            }
            Node twinGoal = step(twinMinPQ);
            if (twinGoal != null) {
                isSolvable = false;
                moves = -1;
                solution = null;
                break;
            }
        }
    }

    private Node step(MinPQ<Node> queue) {
        if (queue.isEmpty()) {
            //keep return null and wait for another queue to find solution
            return null;
            //or should I throw an exception?
        }
        Node node = queue.delMin();
        for (Board neighbor : node.board.neighbors()) {
            Node neighborNode = new Node(node, neighbor, node.dis + 1);
            if (neighbor == node.pre.board) {
                continue;
            }
            if (neighbor.isGoal()) {
                return neighborNode;
            }
            queue.insert(neighborNode);
        }
        return null;
    }

    /**
     * if find goal, this will fill `solution` and `moves`
     *
     * @param goal
     */
    private void getSolution(Node goal) {
        Node node = goal;
        //number of boards
        int num = 0;
        while (node != null) {
            solution.push(node.board);
            node = node.pre;
            num++;
        }
        this.moves = num - 1;
    }

    public boolean isSolvable() {
        // is the initial board solvable?
        return isSolvable;
    }

    public int moves() {
        // min number of moves to solve initial board; -1 if unsolvable
        return moves;
    }

    public Iterable<Board> solution() {
        // sequence of boards in a shortest solution; null if unsolvable
        return solution;
    }


    private class Node implements Comparable<Node> {
        Node pre;
        Board board;
        int dis;

        Node(Node pre, Board board, int dis) {
            this.pre = pre;
            this.board = board;
            this.dis = dis;
        }

        @Override
        public int compareTo(Node o) {
            return board.manhattan() + dis - o.board.manhattan() - o.dis;
        }
    }

    public static void main(String[] args) {

        // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] blocks = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                blocks[i][j] = in.readInt();
            }
        }
        Board initial = new Board(blocks);

        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable()) {
            StdOut.println("No solution possible");
        } else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution()) {
                StdOut.println(board);
            }
        }
    }
}
