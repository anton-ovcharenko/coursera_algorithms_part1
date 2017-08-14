import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {
    private double[] xValues;
    private double vMean;
    private double vStdDev;

    public PercolationStats(int N, int T) {
        if (N <= 0 || T <= 0)
            throw new java.lang.IllegalArgumentException();

        xValues = new double[T];

        for (int i = 0; i < T; ++i) {
            int count = 0;
            Percolation p = new Percolation(N);
            boolean bPercolate = false;

            while (!bPercolate) {
                int pI = StdRandom.uniform(1, N + 1);
                int pJ = StdRandom.uniform(1, N + 1);
                if (!p.isOpen(pI, pJ)) {
                    p.open(pI, pJ);
                    ++count;
                    bPercolate = p.percolates();
                }
            }
            xValues[i] = (double) count / (double) (N * N);
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
        int N = Integer.parseInt(args[0]);
        int T = Integer.parseInt(args[1]);

        PercolationStats ps = new PercolationStats(N, T);
        double mean = ps.mean();
        double stddev = ps.stddev();
        double confLow = ps.confidenceLo();
        double confHigh = ps.confidenceHi();

        StdOut.println("mean                    = " + Double.toString(mean));
        StdOut.println("stddev                  = " + Double.toString(stddev));
        StdOut.println("95% confidence interval = " + Double.toString(confLow) + ", " + Double.toString(confHigh));
    }
}

