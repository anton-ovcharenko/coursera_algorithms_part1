import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private int n;
    private int topIndex;
    private int bottomIndex;
    private WeightedQuickUnionUF uf;
    private byte[] sites; // 0 - closed site, 1 - open site
    private int openSites;

    public Percolation(int N) {
        n = N;
        uf = new WeightedQuickUnionUF(n * n + 2);
        sites = new byte[n * n]; // default value 0
        topIndex = n * n;
        bottomIndex = n * n + 1;
    }

    public void open(int i, int j) {
        validate(i, j);
        if (isOpen(i, j)) {
            return;
        }
        int currentSiteIndex = convertCoord(i, j);
        this.sites[currentSiteIndex] = 1; //open site
        openSites++;

        // join with topIndex cell
        if (i == 1 && !uf.connected(currentSiteIndex, topIndex)) {
            uf.union(currentSiteIndex, topIndex);
        }

        // join with bottomIndex cell
        if (i == n) {
            uf.union(currentSiteIndex, bottomIndex);
        }

        // join with bottom cell
        if (i < n) {
            if (isOpen(i + 1, j)) {
                uf.union(currentSiteIndex, convertCoord(i + 1, j));
            }
        }

        // join with upper cell
        if (i > 1) {
            if (isOpen(i - 1, j)) {
                uf.union(currentSiteIndex, convertCoord(i - 1, j));
            }
        }

        // join with left cell
        if (j > 1) {
            if (isOpen(i, j - 1)) {
                uf.union(currentSiteIndex, convertCoord(i, j - 1));
            }
        }

        // join with left cell
        if (j < n) {
            if (isOpen(i, j + 1)) {
                uf.union(currentSiteIndex, convertCoord(i, j + 1));
            }
        }
    }

    private void validate(int i, int j) {
        if (i < 1 || i > n || j < 1 || j > n) {
            throw new IndexOutOfBoundsException();
        }
    }

    public boolean isOpen(int i, int j) {
        validate(i, j);
        return sites[convertCoord(i, j)] == 1;
    }

    public boolean isFull(int i, int j) {
        validate(i, j);
        return isOpen(i, j) && uf.connected(topIndex, convertCoord(i, j));
    }

    private int convertCoord(int i, int j) {
        return n * (i - 1) + j - 1;
    }

    public boolean percolates() {
        return uf.connected(topIndex, bottomIndex);
    }

    public int numberOfOpenSites(){
        return openSites;
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