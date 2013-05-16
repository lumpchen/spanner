/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package spanner;

import com.sun.webpane.webkit.dom.HTMLTableElementImpl;
import java.awt.Point;
import java.awt.Rectangle;
import javafx.scene.input.MouseEvent;
import org.w3c.dom.html.HTMLElement;

/**
 *
 * @author lim16
 */
public class TableOverlay extends Overlay {

    private Rectangle rect;
    private HTMLTableElementImpl table;
    private boolean dragged;
    private Point.Double pressed;

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

        this.drawRect();
    }

    private void drawRect() {
        if (rect == null) {
            return;
        }

        this.drawRect(rect);
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
