/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package spanner;

import com.sun.webpane.webkit.dom.HTMLImageElementImpl;
import java.awt.Point;
import java.awt.Rectangle;
import javafx.application.Platform;
import javafx.scene.Cursor;
import javafx.scene.input.MouseEvent;
import org.w3c.dom.html.HTMLElement;

/**
 *
 * @author lim16
 */
public class ImageOverlay extends Overlay {

    private Rectangle rect;
    private Rectangle[] cornerRects = new Rectangle[4];
    private HTMLImageElementImpl selImg;
    private boolean dragged;
    private Point.Double pressed;

    public ImageOverlay(WKBrowser browser) {
        super(browser);
    }

    @Override
    void draw(HTMLElement ele) {
        if (ele instanceof HTMLImageElementImpl) {
            drawRectForImage((HTMLImageElementImpl) ele);
        }
    }

    private void drawRectForImage(HTMLImageElementImpl selImg) {
        this.selImg = selImg;
        int top = selImg.getY();
        int left = selImg.getX();
        int right = selImg.getClientWidth();
        int bottom = selImg.getClientHeight();

        this.rect = new Rectangle(left, top, right, bottom);

        this.drawRect();
    }

    private void drawRect() {
        if (rect == null) {
            return;
        }

        this.drawRect(rect);

        int left = rect.x;
        int top = rect.y;
        int right = rect.width;
        int bottom = rect.height;

        int w = 16;
        int dw = 8;
        int rx = left - dw;
        int ry = top - dw;
        this.cornerRects[0] = new Rectangle(rx, ry, w, w);

        rx = left + right - dw;
        ry = top - dw;
        this.cornerRects[1] = new Rectangle(rx, ry, w, w);

        rx = left - dw;
        ry = top + bottom - dw;
        this.cornerRects[2] = new Rectangle(rx, ry, w, w);

        rx = left + right - dw;
        ry = top + bottom - dw;
        this.cornerRects[3] = new Rectangle(rx, ry, w, w);

        for (int i = 0; i < 4; i++) {
            this.drawRect(this.cornerRects[i]);
        }
    }

    void onMouseClicked(MouseEvent me) {
        if (this.dragged) {
            this.dragged = false;
            return;
        }
        if (rect != null) {
            double x = me.getX();
            double y = me.getY();
            if (rect.contains(x, y)
                    || this.cornerRects[0].contains(x, y)
                    || this.cornerRects[1].contains(x, y)
                    || this.cornerRects[2].contains(x, y)
                    || this.cornerRects[3].contains(x, y)) {
                if (me.getClickCount() == 2) {
                    this.browser.onDoubleClickImage(selImg);
                } else {
                    return;
                }
            }
        }
        this.browser.retireOverlap();
    }

    void onMouseDragged(final MouseEvent me) {
        if (this.pressed == null || this.rect == null) {
            return;
        }

        this.dragged = true;
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                clearGC();

                int x = (int) (me.getX() + 0.5);
                int y = (int) (me.getY() + 0.5);

                rect.width = x - rect.x;
                rect.height = y - rect.y;
                drawRect();
                browser.onResizeImage(selImg, rect);
            }
        });
    }

    void onMouseReleased(final MouseEvent me) {
        if (this.pressed == null || this.rect == null) {
            return;
        }
        if (!this.dragged) {
            return;
        }

        clearGC();

        int x = (int) (me.getX() + 0.5);
        int y = (int) (me.getY() + 0.5);

        rect.width = x - rect.x;
        rect.height = y - rect.y;
        drawRect();
        browser.onResizeImage(selImg, rect);
    }

    void onMousePressed(MouseEvent me) {
        if (rect == null) {
            return;
        }

        double x = me.getX();
        double y = me.getY();

        if (this.cornerRects[0].contains(x, y)
                || this.cornerRects[1].contains(x, y)
                || this.cornerRects[2].contains(x, y)
                || this.cornerRects[3].contains(x, y)) {
            this.pressed = new Point.Double(x, y);
        } else {
            this.pressed = null;
        }
    }

    void onMouseMoved(MouseEvent me) {
        if (rect == null) {
            return;
        }

        double x = me.getX();
        double y = me.getY();

        if (this.cornerRects[0].contains(x, y)) {
            this.setCursor(Cursor.SE_RESIZE);
        } else if (this.cornerRects[1].contains(x, y)) {
            this.setCursor(Cursor.SW_RESIZE);
        } else if (this.cornerRects[2].contains(x, y)) {
            this.setCursor(Cursor.SW_RESIZE);
        } else if (this.cornerRects[3].contains(x, y)) {
            this.setCursor(Cursor.SE_RESIZE);
        } else {
            this.setCursor(Cursor.DEFAULT);
        }
    }
}
