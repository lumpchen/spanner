/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package spanner;

import java.awt.Rectangle;
import javafx.event.EventHandler;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.BlendMode;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import org.w3c.dom.html.HTMLElement;

/**
 *
 * @author lim16
 */
public abstract class Overlay extends Canvas {

    protected GraphicsContext gc;
    protected WKBrowser browser;

    Overlay(WKBrowser browser) {
        super();
        this.setBlendMode(BlendMode.MULTIPLY);

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

    protected void clearGC() {
        gc.setFill(Color.rgb(255, 255, 255, 1));
        this.gc.fillRect(0, 0, this.getWidth(), this.getHeight());
    }

    public void setSize(double width, double height) {
        this.setWidth(width);
        this.setHeight(width);
    }

    void drawRect(Rectangle rect) {
        this.gc.strokeRect(rect.x, rect.y, rect.width, rect.height);
    }

    abstract void draw(HTMLElement ele);

    abstract void onMouseClicked(final MouseEvent me);

    abstract void onMouseDragged(final MouseEvent me);

    abstract void onMouseReleased(final MouseEvent me);

    abstract void onMousePressed(final MouseEvent me);

    abstract void onMouseMoved(final MouseEvent me);
}
