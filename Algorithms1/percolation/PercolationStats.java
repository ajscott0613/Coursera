/* *****************************************************************************
 *  Name:              Alan Turing
 *  Coursera User ID:  123456
 *  Last modified:     1/1/2019
 **************************************************************************** */

import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;


public class PercolationStats {

    // class variable
    // private Percolation perc;
    private static final double CONFIDENCE_95 = 1.96;
    private final double[] allPercT;
    private final int nTrials;

    // perform independent trials on an n-by-n grid
    public PercolationStats(int n, int trials) {

        if (n <= 0) throw new IllegalArgumentException("n = " + n + ", is not > 0");
        if (trials <= 0) throw new IllegalArgumentException("trials = " + trials + ", is not > 0");

        int row;
        int col;
        int nSites = n * n;
        // double percT;
        allPercT = new double[trials];
        nTrials = trials;


        for (int i = 0; i < trials; i++) {

            Percolation perc = new Percolation(n);
            while (!perc.percolates()) {
                row = StdRandom.uniform(1, n + 1);
                col = StdRandom.uniform(1, n + 1);
                while (perc.isOpen(row, col)) {
                    row = StdRandom.uniform(1, n + 1);
                    col = StdRandom.uniform(1, n + 1);
                }
                perc.open(row, col);
            }

            // capture percentage
            double percT = perc.numberOfOpenSites() / (double) nSites;
            allPercT[i] = percT;
        }

    }

    // sample mean of percolation threshold
    public double mean() {
        return StdStats.mean(allPercT);
    }

    // sample standard deviation of percolation threshold
    public double stddev() {
        return StdStats.stddev(allPercT);
    }

    // low endpoint of 95% confidence interval
    public double confidenceLo() {
        return mean() - (stddev() * CONFIDENCE_95) / Math.sqrt(nTrials);
    }

    // high endpoint of 95% confidence interval
    public double confidenceHi() {
        return mean() + (stddev() * CONFIDENCE_95) / Math.sqrt(nTrials);
    }

    // test client (see below)
    public static void main(String[] args) {

        int n = Integer.parseInt(args[0]);
        int trials = Integer.parseInt(args[1]);

        // int n = 5;
        // int trials = 10;

        if (n <= 0) throw new IllegalArgumentException("n = " + n + ", is not > 0");
        if (trials <= 0) throw new IllegalArgumentException("trials = " + trials + ", is not > 0");

        // System.out.println("Running " + trials + " Trials with " + n + " x " + n + " grid.");

        PercolationStats percStats = new PercolationStats(n, trials);

        // print outputs
        System.out.println("mean                    = " + percStats.mean());
        System.out.println("stddev                  = " + percStats.stddev());
        double conHi = percStats.confidenceHi();
        double conLo = percStats.confidenceLo();
        System.out.println("95% confidence interval = [" + conLo + ", " + conHi + "]");

    }
}
