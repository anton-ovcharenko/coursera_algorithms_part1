import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private static final byte OPEN_MARK = 1;
    private static final byte CONNECTED_TO_BOTTOM_MARK = 2;
    private static final int BLOCKED_MARK = 0;
    private int n;
    private int topIndex;
    private WeightedQuickUnionUF uf;
    private byte[] sites; // 0 - closed site, 1 - open site, 2 - connected to bottom
    private int openSiteAmount;

    public Percolation(int n) {
        if (n < 1) {
            throw new IllegalArgumentException();
        }
        this.n = n;
        uf = new WeightedQuickUnionUF(this.n * this.n + 1);
        sites = new byte[this.n * this.n + 1]; // default value 0
        topIndex = this.n * this.n;
    }

    public void open(int i, int j) {
        validate(i, j);
        if (isOpen(i, j)) {
            return;
        }
        int currentSiteIndex = convertCoord(i, j);
        this.sites[currentSiteIndex] = OPEN_MARK;
        openSiteAmount++;

        // if bottom line
        if (i == n) {
            this.sites[currentSiteIndex] = CONNECTED_TO_BOTTOM_MARK;
        }

        // join with topIndex cell
        if (i == 1) {
            uf.union(currentSiteIndex, topIndex);
            if (n == 1) {
                this.sites[topIndex] = CONNECTED_TO_BOTTOM_MARK;
            }
        }

        // join with bottom cell
        if (i < n) {
            joinWith(currentSiteIndex, i + 1, j);
        }

        // join with upper cell
        if (i > 1) {
            joinWith(currentSiteIndex, i - 1, j);
        }

        // join with left cell
        if (j > 1) {
            joinWith(currentSiteIndex, i, j - 1);
        }

        // join with left cell
        if (j < n) {
            joinWith(currentSiteIndex, i, j + 1);
        }
    }

    public boolean isOpen(int i, int j) {
        validate(i, j);
        return sites[convertCoord(i, j)] != BLOCKED_MARK;
    }

    public boolean isFull(int i, int j) {
        validate(i, j);
        return uf.connected(convertCoord(i, j), topIndex);
    }

    public boolean percolates() {
        int topRoot = uf.find(topIndex);
        return sites[topRoot] == CONNECTED_TO_BOTTOM_MARK;
    }

    public int numberOfOpenSites() {
        return openSiteAmount;
    }

    private int convertCoord(int i, int j) {
        return n * (i - 1) + j - 1;
    }

    private void joinWith(int currentSiteIndex, int i, int j) {
        if (isOpen(i, j)) {
            final int convertCoord = convertCoord(i, j);
            final int currentRoot = uf.find(currentSiteIndex);
            final int toRoot = uf.find(convertCoord);
            uf.union(currentSiteIndex, convertCoord);
            if (sites[currentRoot] == CONNECTED_TO_BOTTOM_MARK || sites[toRoot] == CONNECTED_TO_BOTTOM_MARK) {
                final int afterUnionRoot = uf.find(currentSiteIndex);
                sites[afterUnionRoot] = CONNECTED_TO_BOTTOM_MARK;
            }
        }
    }

    private void validate(int i, int j) {
        if (i < 1 || i > n || j < 1 || j > n) {
            throw new IllegalArgumentException();
        }
    }

    public static void main(String[] args) {
        In in = new In(args[0]);      // input file
        int n = in.readInt();         // n-by-n percolation system
        Percolation percolation = new Percolation(n);

        while (!in.isEmpty()) {
            int i = in.readInt();
            int j = in.readInt();
            percolation.open(i, j);
        }

        StdOut.println("percolates: " + percolation.percolates());
    }
}