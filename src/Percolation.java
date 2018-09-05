
import edu.princeton.cs.algs4.WeightedQuickUnionUF;

/**
 * Corner cases.  By convention, the row and column indices are integers between 1 and n,
 * where (1, 1) is the upper-left site: Throw a java.lang.IllegalArgumentException if any
 * argument to open(), isOpen(), or isFull() is outside its prescribed range. The constructor
 * should throw a java.lang.IllegalArgumentException if n ≤ 0.
 * <p>
 * Performance requirements.  The constructor should take time proportional to n2; all methods
 * should take constant time plus a constant number of calls to the union–find methods union(),
 * find(), connected(), and count().
 */
public class Percolation {

    private boolean[][] grids;
    private int countOpenSite;
    private final int n;
    private final WeightedQuickUnionUF uf;

    /**
     * create n-by-n grid, with all sites blocked
     *
     * @param n
     */
    public Percolation(int n) {
        if (n<=0){
            throw new IllegalArgumentException();
        }
        this.n = n;
        //grids : 0 1-n n+1
        grids = new boolean[n + 2][n + 2];
        //uf: 0 1-n*n n+1
        uf = new WeightedQuickUnionUF(n * n + 2);
    }

    public void open(int row, int col) {
        // open site (row, col) if it is not open already
        checkBound(row, col);

        if (grids[row][col]) {
            //if is opened already
            return;
        }

        if (isOpenP(row - 1, col)) {
            uf.union((row - 2) * n + col, (row - 1) * n + col);
        }
        if (isOpenP(row + 1, col)) {
            uf.union(row * n + col, (row - 1) * n + col);
        }
        if (isOpenP(row, col - 1)) {
            uf.union((col - 1) + (row - 1) * n, (row - 1) * n + col);
        }
        if (isOpenP(row, col + 1)) {
            uf.union((col + 1) + (row - 1) * n, (row - 1) * n + col);
        }


        if (row == 1) {
            uf.union((row - 1) * n + col, 0);
        }
        if (row == n) {
            uf.union((row - 1) * n + col, n * n + 1);
        }

        grids[row][col] = true;
        countOpenSite++;
    }

    private boolean isOpenP(int row, int col) {
        if (row < 1 || row > n || col < 1 || col > n) {
            return false;
        }
        return grids[row][col];
    }

    public boolean isOpen(int row, int col) {
        // is site (row, col) open?
        checkBound(row, col);
        return grids[row][col];
    }

    public boolean isFull(int row, int col) {
        // is site (row, col) full?
        checkBound(row, col);
        return uf.connected(0, (row - 1) * n + col);
    }

    public int numberOfOpenSites() {
        // number of open sites
        return countOpenSite;
    }

    public boolean percolates() {
        return uf.connected(0, n * n + 1);
    }

    private void checkBound(int row, int col) {
        if (row < 1 || row > n || col < 1 || col > n) {
            throw new IllegalArgumentException();
        }
    }

    public static void main(String[] args) {
        // test client (optional)
        int n = 50;
        Percolation p = new Percolation(n);
        System.out.println(p.isFull(1, 2));
        System.out.println(p.isFull(2, 2));
        p.open(1, 2);
        p.open(2, 2);
        System.out.println(p.isFull(1, 2));
        System.out.println(p.isFull(2, 2));
    }
}
