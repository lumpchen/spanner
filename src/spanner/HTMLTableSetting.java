/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package spanner;

/**
 *
 * @author lim16
 */
public class HTMLTableSetting {

    private int row;
    private int col;

    public HTMLTableSetting(int row, int col) {
        this.row = row;
        this.col = col;
    }

    public int getRow() {
        return this.row;
    }

    public int getCol() {
        return this.col;
    }
}
