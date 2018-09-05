import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {

    private final int trials;
    private final int n;

    private double mean;
    private double stddey;
    private double confidenceLo;
    private double confidenceHi;

    public PercolationStats(int n, int trials) {
        if (n<=0||trials<=0){
            throw new IllegalArgumentException();
        }
        this.n = n;
        this.trials = trials;
        run();
    }    // perform trials independent experiments on an n-by-n grid

    private void run() {
        double[] x = new double[trials];
        for (int i = 0; i < trials; i++) {
            Percolation percolation = new Percolation(n);
            int tempCount = 0;
            while (true) {
                int row = StdRandom.uniform(n) + 1;
                int col = StdRandom.uniform(n) + 1;
                if (percolation.isOpen(row, col)) {
                    continue;
                }
                percolation.open(row, col);
                tempCount++;
                if (percolation.percolates()) {
                    x[i] = tempCount / Math.pow(n, 2);
                    break;
                }
            }
        }
        doMath(x);
    }

    private void doMath(double[] x) {
        assert x.length == trials;
        this.mean = StdStats.mean(x);
        this.stddey = StdStats.stddev(x);
        this.confidenceLo = this.mean - 1.96 * this.stddey / Math.sqrt(trials);
        this.confidenceHi = this.mean + 1.96 * this.stddey / Math.sqrt(trials);
    }

    public double mean() {
        return mean;
    }                         // sample mean of percolation threshold

    public double stddev() {
        return stddey;
    }                        // sample standard deviation of percolation threshold

    public double confidenceLo() {
        return confidenceLo;
    }                  // low  endpoint of 95% confidence interval

    public double confidenceHi() {
        return confidenceHi;
    }                  // high endpoint of 95% confidence interval


    public static void main(String[] args) {
        int n = Integer.parseInt(args[0]);
        int trials = Integer.parseInt(args[1]);
        PercolationStats percolationStats = new PercolationStats(n, trials);
        System.out.format("mean                    = %f%n", percolationStats.mean());
        System.out.format("stddev                  = %f%n", percolationStats.stddev());
        System.out.format("95%% confidence interval = [%f, %f]%n",
                percolationStats.confidenceLo(), percolationStats.confidenceHi());
    }       // test client (described below)
}
