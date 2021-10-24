/* *****************************************************************************
 *  Name:              Alan Turing
 *  Coursera User ID:  123456
 *  Last modified:     1/1/2019
 **************************************************************************** */

import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {

    // private int[][] grid;
    private boolean[] arr;
    private final int nLen;
    private final WeightedQuickUnionUF wquf;

    // creates n-by-n grid, with all sites initially blocked
    public Percolation(int n) {

        if (n <= 0) throw new IllegalArgumentException("n = " + n + ", is not > 0");

        arr = new boolean[n * n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                // this.grid[i][j] = 0;
                this.arr[i * n + j] = false;
            }
        }
        nLen = n;
        wquf = new WeightedQuickUnionUF(n * n);

        // create virtual top and bottom site
        for (int i = 1; i < nLen; i++) {
            wquf.union(0, i);
            wquf.union((n - 1) * n, (n - 1) * n + i);
        }

        // this.grid = grid;
    }

    // private boolean connected(int p, int q) {
    //     return (wquf.find(p) == wquf.find(q));
    // }

    // private void printGrid() {
    //
    //     System.out.println("-------------------------------------------");
    //     for (int i = 0; i < nLen * nLen; i++) {
    //         if (i > 0 && (i + 1) % nLen == 0) {
    //             if (arr[i]) System.out.println(arr[i] + "  ");
    //             else System.out.println(arr[i] + " ");
    //         }
    //         else {
    //             if (arr[i]) System.out.print(arr[i] + "  ");
    //             else System.out.print(arr[i] + " ");
    //         }
    //     }
    //     System.out.println("-------------------------------------------");
    //
    // }

    private int convertDim(int row, int col) {
        return row * nLen + col;
    }

    // opens the site (row, col) if it is not open already
    public void open(int row, int col) {

        if (row < 1 || row > nLen) throw new IllegalArgumentException("invalid row value");
        if (col < 1 || col > nLen) throw new IllegalArgumentException("invalid column value");

        if (isOpen(row, col)) return;

        row -= 1;
        col -= 1;
        int index = convertDim(row, col);
        arr[index] = true;

        // System.out.println("------------------------------------------------");
        // System.out.println("    running for row = " + row + ", col = " + col);
        // System.out.println("    index = " + index);
        // printGrid();


        int[] lat = { -1, 1 };
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 2; j++) {
                int nRow = row;
                int nCol = col;
                if (i == 0) nRow = row + lat[j];
                else nCol = col + lat[j];
                if (nRow >= 0 && nRow < nLen && nCol >= 0 && nCol < nLen) {
                    int nIndex = convertDim(nRow, nCol);
                    if (isOpen(nRow + 1, nCol + 1) && !(wquf.find(index) == wquf.find(nIndex))) {
                        wquf.union(index, nIndex);
                        // open(nRow + 1, nCol + 1);
                    }
                }
            }
        }

    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col) {

        if (row < 1 || row > nLen) throw new IllegalArgumentException("invalid row value");
        if (col < 1 || col > nLen) throw new IllegalArgumentException("invalid column value");

        row -= 1;
        col -= 1;
        // return (arr[row * nLen + col] > 0);
        return (arr[convertDim(row, col)]);
    }

    // is the site (row, col) full?
    public boolean isFull(int row, int col) {

        // System.out.println(row);
        // System.out.println(nLen);

        if (row < 1 || row > nLen) throw new IllegalArgumentException("invalid row value");
        if (col < 1 || col > nLen) throw new IllegalArgumentException("invalid column value");

        if (!isOpen(row, col)) return false;
        if (row == 1 && isOpen(row, col)) return true;
        row -= 1;
        col -= 1;

        int[] lat = { -1, 1 };
        boolean topOpen = false;
        for (int i = 0; i < nLen; i++) {
            if (isOpen(1, i + 1)) topOpen = true;
        }
        if (!topOpen) return false;

        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 2; j++) {
                int nRow = row;
                int nCol = col;
                if (i == 0) nRow = row + lat[j];
                else nCol = col + lat[j];
                if (nRow >= 0 && nRow < nLen && nCol >= 0 && nCol < nLen) {
                    int nIndex = convertDim(nRow, nCol);
                    if ((wquf.find(0) == wquf.find(nIndex))) return true;
                }
            }
        }


        // int[] neighbors = { -1, 1, nLen };
        // for (int i = 0; i < nLen; i++) {
        //     if (!arr[i]) continue;
        //     for (int j = 0; j < neighbors.length; j++) {
        //         int curIndex = index + neighbors[j];
        //         if (curIndex < 0 || curIndex >= (nLen * nLen)) continue;
        //         System.out.println("** (" + (row + 1) + ", " + (col + 1) + ") **");
        //         System.out.println("    index " + i + ": " + wquf.find(i));
        //         System.out.println("    curindex " + curIndex + ": " + wquf.find(curIndex));
        //         if ((wquf.find(i) == wquf.find(curIndex))) return true;
        //     }
        // }
        return false;
    }

    // returns the number of open sites
    public int numberOfOpenSites() {
        int opensites = 0;
        for (int i = 0; i < nLen; i++) {
            for (int j = 0; j < nLen; j++) {
                if (isOpen(i + 1, j + 1)) opensites++;
            }
        }
        return opensites;
    }

    // does the system percolate?
    public boolean percolates() {
        boolean bottomOpen = false;
        for (int i = 1; i < nLen + 1; i++) {
            if (isOpen(nLen, i)) bottomOpen = true;
        }
        if (!bottomOpen) return false;
        if (isFull(nLen, nLen)) return true;
        return false;
    }

    // test client (optional)
    public static void main(String[] args) {
        // int n = 5;

        // Percolation perc = new Percolation(6);
        //
        // // print grid
        // perc.printGrid();
        //
        // // test isOpen
        // boolean OpenBool = perc.isOpen(2, 3);
        // System.out.print("row 2, column 3 open?:    ");
        // System.out.println(OpenBool);
        //
        // // test isFull
        // boolean FullBool = perc.isFull(2, 3);
        // System.out.print("row 2, column 3 full?:    ");
        // System.out.println(FullBool);
        //
        // // test numberOfOpenSites
        // int opensites = perc.numberOfOpenSites();
        // System.out.println("There are " + opensites + " open sites.");
        //
        // test OpenSite
        // perc.open(1, 6);
        // perc.open(2, 6);
        // perc.open(3, 6);
        // perc.open(4, 6);
        // perc.open(5, 6);
        // perc.open(5, 5);
        // perc.open(4, 4);
        // perc.open(3, 4);
        // perc.open(2, 4);
        // perc.open(2, 3);
        // perc.open(2, 2);
        // perc.open(2, 1);
        //
        //
        // // print grid
        // perc.printGrid();
        //
        // // test isOpen
        // System.out.print("isOpen(2, 1):    ");
        // System.out.println(perc.isOpen(2, 1));
        //
        // // test isFull
        // System.out.print("isFull(2, 1):    ");
        // System.out.println(perc.isFull(2, 1));
        //
        // // test numberOfOpenSites
        // System.out.println("There are " + perc.numberOfOpenSites() + " open sites.");
        //
        // // test percolates
        // System.out.println("System Percolates:  " + perc.percolates());


    }

}
