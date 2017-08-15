import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {
    private double[] xValues;
    private double vMean;
    private double vStdDev;

    public PercolationStats(int n, int t) {
        if (n <= 0 || t <= 0)
            throw new java.lang.IllegalArgumentException();

        xValues = new double[t];

        for (int i = 0; i < t; ++i) {
            int count = 0;
            Percolation p = new Percolation(n);
            boolean bPercolate = false;

            while (!bPercolate) {
                int pI = StdRandom.uniform(1, n + 1);
                int pJ = StdRandom.uniform(1, n + 1);
                if (!p.isOpen(pI, pJ)) {
                    p.open(pI, pJ);
                    ++count;
                    bPercolate = p.percolates();
                }
            }
            xValues[i] = (double) count / (double) (n * n);
        }

        vMean = StdStats.mean(xValues);
        vStdDev = StdStats.stddev(xValues);
    }

    public double mean() {
        return vMean;
    }

    public double stddev() {
        return vStdDev;
    }

    public double confidenceLo() {
        return mean() - (1.96 * stddev() / Math.sqrt((double) xValues.length));
    }

    public double confidenceHi() {
        return mean() + (1.96 * stddev() / Math.sqrt((double) xValues.length));
    }

    public static void main(String[] args) {
        if (args.length < 2) {
            throw new java.lang.IllegalArgumentException();
        }
        int n = Integer.parseInt(args[0]);
        int t = Integer.parseInt(args[1]);

        PercolationStats ps = new PercolationStats(n, t);
        double mean = ps.mean();
        double stddev = ps.stddev();
        double confLow = ps.confidenceLo();
        double confHigh = ps.confidenceHi();

        StdOut.println("mean                    = " + Double.toString(mean));
        StdOut.println("stddev                  = " + Double.toString(stddev));
        StdOut.println("95% confidence interval = " + Double.toString(confLow) + ", " + Double.toString(confHigh));
    }
}

