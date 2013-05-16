/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package spanner;

import com.sun.webpane.webkit.dom.HTMLTableCellElementImpl;
import com.sun.webpane.webkit.dom.HTMLTableElementImpl;
import com.sun.webpane.webkit.dom.HTMLTableRowElementImpl;
import java.awt.Point;
import java.awt.Rectangle;
import javafx.scene.input.MouseEvent;
import org.w3c.dom.html.HTMLCollection;
import org.w3c.dom.html.HTMLElement;
import org.w3c.dom.html.HTMLTableSectionElement;

/**
 *
 * @author lim16
 */
public class TableOverlay extends Overlay {

    private Rectangle rect;
    private HTMLTableElementImpl table;
    private boolean dragged;
    private Point.Double pressed;
    private CellRect[][] cellArray;

    public TableOverlay(WKBrowser browser) {
        super(browser);
    }

    @Override
    void draw(HTMLElement ele) {
        if (ele instanceof HTMLTableElementImpl) {
            drawForTable((HTMLTableElementImpl) ele);
        }
    }

    private void drawForTable(HTMLTableElementImpl ele) {
        this.table = ele;
        int left = table.getClientLeft() + table.getOffsetLeft();
        int top = table.getClientTop() + table.getOffsetTop();
        int width = table.getClientWidth();
        int height = table.getClientHeight();

        this.rect = new Rectangle(left, top, width, height);

        HTMLTableSectionElement head = table.getTHead();
        HTMLTableSectionElement foot = table.getTFoot();

        HTMLCollection rows = this.table.getRows();
        for (int r = 0; r < rows.getLength(); r++) {
            HTMLTableRowElementImpl row = (HTMLTableRowElementImpl) rows.item(r);
            HTMLCollection cols = row.getCells();
            for (int c = 0; c < cols.getLength(); c++) {
                HTMLTableCellElementImpl cell = (HTMLTableCellElementImpl) cols.item(c);
                this.drawCell(cell);
            }
        }
    }

    private void drawCell(HTMLTableCellElementImpl cell) {
        int left = table.getOffsetLeft() + cell.getClientLeft() + cell.getOffsetLeft();
        int top = table.getOffsetTop() + cell.getClientTop() + cell.getOffsetTop();
        int width = cell.getClientWidth();
        int height = cell.getClientHeight();

        Rectangle rect = new Rectangle(left, top, width, height);
        CellRect cellRect = new CellRect(rect, cell);
        this.drawRect(rect);
    }

    class CellRect {

        Rectangle rect;
        HTMLTableCellElementImpl cell;

        CellRect(Rectangle rect, HTMLTableCellElementImpl cell) {
            this.rect = rect;
            this.cell = cell;
        }
    }

    @Override
    void onMouseClicked(MouseEvent me) {
        if (this.dragged) {
            this.dragged = false;
            return;
        }
        if (rect != null) {
            double x = me.getX();
            double y = me.getY();
            if (rect.contains(x, y)) {
                if (me.getClickCount() == 2) {
//                    this.browser.onDoubleClickImage();
                } else {
                    return;
                }
            }
        }
        this.browser.retireOverlap();
    }

    @Override
    void onMouseDragged(MouseEvent me) {
    }

    @Override
    void onMouseReleased(MouseEvent me) {
    }

    @Override
    void onMousePressed(MouseEvent me) {
    }

    @Override
    void onMouseMoved(MouseEvent me) {
    }
}
