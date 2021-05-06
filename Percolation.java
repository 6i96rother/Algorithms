public class Percolation {

    private static final int CLOSED = -1;

    private final int n;
    private final int size;
    private final int[] sites;
    private int numOpen;

    // creates n-by-n grid, with all sites initially blocked
    public Percolation(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException("Number of elements must be positive!");
        }
        this.n = n;
        size = n*n;
        numOpen = 0;
        sites = new int[size+1];
        for (int i = 1; i <= size; i++) {
            sites[i] = CLOSED;
        }
    }

    private void union(int i1, int i2) {
        int r1 = root(i1);
        int r2 = root(i2);
        int minR = Math.min(r1, r2);
        int maxR = Math.max(r1, r2);
        sites[minR] = maxR;
    }

    private int root(int i) {
        if (sites[i] == CLOSED) {
            return i;
        }
        while (i != sites[i]) {
            sites[i] = sites[sites[i]];
            i = sites[i];
        }
        return i;
    }

    // opens the site (row, col) if it is not open already
    public void open(int row, int col) {
        validateInput(row, col);
        int idx = getSiteIndex(row, col);
        open(idx);
        if (col > 1) {
            // left adjacent
            unionWithAdjacent(idx, idx-1);
        }
        if (col < n) {
            // right adjacent
            unionWithAdjacent(idx, idx+1);
        }
        if (row > 1) {
            // top adjacent
            unionWithAdjacent(idx, idx-n);
        }
        if (row < n) {
            // bottom adjacent
            unionWithAdjacent(idx, idx+n);
        }
    }

    private void validateInput(int row, int col) {
        if (row < 1 || n < row) {
            throw new IllegalArgumentException(String.format("Row must be between 1 and %s!", n));
        }
        if (col < 1 || n < col) {
            throw new IllegalArgumentException(String.format("Column must be between 1 and %s!", n));
        }
    }

    private void open(int idx) {
        if (!isOpen(idx)) {
            sites[idx] = idx;
            numOpen++;
            if (isTopRowSite(idx)) {
                union(idx, 0);
            }
        }
    }

    private boolean isTopRowSite(int i) {
        return 1 <= i && i <= n;
    }

    private void unionWithAdjacent(int idx, int adj) {
        if (isOpen(adj)) {
            union(idx, adj);
        }
    }

    private boolean isOpen(int idx) {
        return sites[idx] != CLOSED;
    }

    private int getSiteIndex(int row, int col) {
        row--;
        return n*row + col;
    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col) {
        validateInput(row, col);
        int idx = getSiteIndex(row, col);
        return isOpen(idx);
    }

    // is the site (row, col) full?
    public boolean isFull(int row, int col) {
        validateInput(row, col);
        int idx = getSiteIndex(row, col);
        // is open and connected to top abstract site
        return isOpen(idx) && root(idx) == root(0);
    }

    // returns the number of open sites
    public int numberOfOpenSites() {
        return numOpen;
    }

    // does the system percolate?
    public boolean percolates() {
        int r = root(0);
        return isBottomRowSite(r);
    }

    private boolean isBottomRowSite(int i) {
        return size-n < i && i <= size;
    }

}
