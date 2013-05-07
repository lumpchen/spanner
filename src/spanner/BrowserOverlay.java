/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package spanner;

import com.sun.webpane.webkit.dom.HTMLImageElementImpl;
import java.awt.Point;
import java.awt.Rectangle;
import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

/**
 *
 * @author lim16
 */
public class BrowserOverlay extends Canvas {

    private GraphicsContext gc;
    private Rectangle rect;
    private WKBrowser browser;
    private Rectangle[] cornerRects = new Rectangle[4];
    private HTMLImageElementImpl selImg;

    public BrowserOverlay(WKBrowser browser) {
        super();

        this.browser = browser;
        this.gc = this.getGraphicsContext2D();
        this.gc.setStroke(Color.RED);

        this.setOnMouseMoved(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent me) {
                onMouseMoved(me);
            }
        });

        this.setOnMousePressed(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent me) {
                onMousePressed(me);
            }
        });

        this.setOnMouseReleased(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent me) {
                onMouseReleased(me);
            }
        });

        this.setOnMouseDragged(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent me) {
                onMouseDragged(me);
            }
        });

        this.setOnMouseClicked(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent me) {
                onMouseClicked(me);
            }
        });
    }

    public void clearGC() {
        Paint p = this.gc.getStroke();
        this.gc.setStroke(Color.WHITE);
        this.drawRect();
        this.gc.setStroke(p);
    }

    public void setSize(double width, double height) {
        this.setWidth(width);
        this.setHeight(width);
    }

    public void drawRectForImage(HTMLImageElementImpl selImg) {
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
        this.gc.rect(rx, ry, w, w);

        rx = left + right - dw;
        ry = top - dw;
        this.cornerRects[1] = new Rectangle(rx, ry, w, w);
        this.gc.rect(rx, ry, w, w);

        rx = left - dw;
        ry = top + bottom - dw;
        this.cornerRects[2] = new Rectangle(rx, ry, w, w);
        this.gc.rect(rx, ry, w, w);

        rx = left + right - dw;
        ry = top + bottom - dw;
        this.cornerRects[3] = new Rectangle(rx, ry, w, w);
        this.gc.rect(rx, ry, w, w);

        for (int i = 0; i < 4; i++) {
            this.drawRect(this.cornerRects[i]);
        }

        this.gc.stroke();
    }

    private void drawRect(Rectangle rect) {
        this.gc.rect(rect.x, rect.y, rect.width, rect.height);
    }

    private void onMouseClicked(MouseEvent me) {
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

    private void onMouseDragged(MouseEvent me) {
        if (this.pressed == null || this.rect == null) {
            return;
        }

        int x = (int) (me.getX() + 0.5);
        int y = (int) (me.getY() + 0.5);

//        rect.width = x - rect.x;
//        rect.height = y - rect.y;
//        this.clearGC();
//        this.drawRect();
    }

    private void onMouseReleased(MouseEvent me) {
        if (this.pressed == null || this.rect == null) {
            return;
        }
        this.clearGC();

        int x = (int) (me.getX() + 0.5);
        int y = (int) (me.getY() + 0.5);

        rect.width = x - rect.x;
        rect.height = y - rect.y;
        this.drawRect();
        this.browser.onResizeImage(selImg, rect);

    }
    private Point.Double pressed;

    private void onMousePressed(MouseEvent me) {
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

    private void onMouseMoved(MouseEvent me) {
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
