import java.util.Iterator;
import java.util.NoSuchElementException;

public class Board {

    private final int[][] blocks;
    private final int n;
    private int hamming;
    private int manhattan;
    private boolean isGoal;
    private int blankX;
    private int blankY;
    private int dis;

    public Board(int[][] blocks) {
        // construct a board from an n-by-n array of blocks
        // (where blocks[i][j] = block in row i, column j)
        if (blocks == null) {
            throw new IllegalArgumentException();
        }
        this.blocks = clone2d(blocks);
        this.n = blocks.length;

        manhattan = getManhattan();
        hamming = getHamming();
        isGoal = hamming == 0;

        dis = 0;
    }

    private Board(int[][] blocks, int manhattan, int dis) {
        // as this is already a new copy,there is no need to make a clone
        this.blocks = blocks;
        this.n = blocks.length;
        this.manhattan = manhattan;
        hamming = getHamming();
        isGoal = hamming == 0;
        this.dis = dis;
    }

    private int getHamming() {
        int hamming = 0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (blocks[i][j] == 0) {
                    blankX = i;
                    blankY = j;
                    continue;
                }
                if (blocks[i][j] != i * n + j + 1) {
                    hamming++;
                }
            }
        }
        return hamming;
    }

    private int getManhattan() {
        int manhattan = 0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                int num = blocks[i][j] - 1;
                if (num == -1) {
                    continue;
                }
                int row = num / n;
                int col = num % n;
                manhattan += Math.abs(row - i) + Math.abs(col - j);
            }
        }
        return manhattan;
    }

    public int dimension() {
        // board dimension n
        return blocks.length;
    }

    public int hamming() {
        // number of blocks out of place
        return hamming;
    }

    public int manhattan() {
        // sum of Manhattan distances between blocks and goal
        return manhattan;
    }

    public boolean isGoal() {
        // is this board the goal board?
        return isGoal;
    }

    public Board twin() {
        // a board that is obtained by exchanging any pair of blocks
        int[][] newBlocks = clone2d(blocks);
        int row = blankX == 0 ? 1 : 0;
        int temp;
        temp = newBlocks[row][0];
        newBlocks[row][0] = newBlocks[row][1];
        newBlocks[row][1] = temp;
        return new Board(newBlocks);
    }

    @Override
    public boolean equals(Object y) {
        // does this board equal y?
        if (y == null) {
            return false;
        }
        if (getClass() != y.getClass()) {
            return false;
        }
        int[][] blocks = ((Board) y).blocks;
        if (blocks == null || blocks.length != n || blocks[0].length != n) {
            return false;
        }
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (blocks[i][j] != this.blocks[i][j]) {
                    return false;
                }
            }
        }
        return true;
    }

    public Iterable<Board> neighbors() {
        // all neighboring boards
        return () -> new Iterator<Board>() {
            //init {up,down,left,right} as false
            private boolean[] dir = new boolean[4];

            {
                //init
                if (blankX == n - 1) {
                    dir[3] = true;
                } else if (blankX == 0) {
                    dir[2] = true;
                }
                if (blankY == n - 1) {
                    dir[1] = true;
                } else if (blankY == 0) {
                    dir[0] = true;
                }
            }

            @Override
            public boolean hasNext() {
                for (boolean visited : dir) {
                    if (!visited) {
                        return true;
                    }
                }
                return false;
            }

            @Override
            public Board next() {
                for (int i = 0; i < dir.length; i++) {
                    if (!dir[i]) {
                        dir[i] = true;
                        int[][] myBlocks = clone2d(blocks);
                        int targetX;
                        int targetY;
                        switch (i) {
                            case 0:
                                //up
                                targetX = blankX;
                                targetY = blankY - 1;
                                break;
                            case 1:
                                //down
                                targetX = blankX;
                                targetY = blankY + 1;
                                break;
                            case 2:
                                //left
                                targetX = blankX - 1;
                                targetY = blankY;
                                break;
                            case 3:
                                //right
                                targetX = blankX + 1;
                                targetY = blankY;
                                break;
                            default:
                                targetX = -1;
                                targetY = -1;
                        }

                        int target = myBlocks[targetX][targetY];
                        int row = (target - 1) / n;
                        int col = (target - 1) % n;
                        int manhattanBefore = Math.abs(row - targetX) + Math.abs(col - targetY);
                        int manhattanAfter = Math.abs(row - blankX) + Math.abs(col - blankY);
                        int m = manhattan - manhattanBefore + manhattanAfter;

                        myBlocks[blankX][blankY] = target;
                        myBlocks[targetX][targetY] = 0;
                        return new Board(myBlocks, m, dis + 1);
                    }
                }
                throw new NoSuchElementException();
            }
        };
    }

    @Override
    public String toString() {
        // string representation of this board (in the output format specified below)
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(n).append('\n');
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                stringBuilder.append(" ").append(blocks[i][j]).append(" ");
            }
            stringBuilder.append("\n");
        }
        return stringBuilder.toString();
    }

    private int[][] clone2d(int[][] array2D) {
        int len = array2D.length;
        int[][] newArray = new int[len][];
        for (int i = 0; i < len; i++) {
            newArray[i] = array2D[i].clone();
        }
        return newArray;
    }

    public static void main(String[] args) {
        // unit tests (not graded)
    }
}