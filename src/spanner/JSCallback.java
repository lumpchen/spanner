/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package spanner;

import com.sun.webpane.webkit.dom.HTMLImageElementImpl;
import com.sun.webpane.webkit.dom.HTMLTableElementImpl;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 *
 * @author lim16
 */
public class JSCallback {

    private HTMLTableElementImpl selTable;
    private HTMLImageElementImpl selImg;
    private String opacity;
    private WKBrowser browser;

    public JSCallback(WKBrowser browser) {
        this.browser = browser;
    }

    public void addCallbackForImage(Element img) {
        img.setAttribute("draggable", "false");
        img.setAttribute("onclick", "app.onclickImage()");
        img.setAttribute("onmouseover", "app.onmouseoverImage()");
        img.setAttribute("onmouseout", "app.onmouseoutImage()");
    }

    public void addCallbackForTable(Element table) {
        table.setAttribute("draggable", "false");
        table.setAttribute("onclick", "app.onclickTable()");
        table.setAttribute("onmouseover", "app.onmouseoverTable()");
        table.setAttribute("onmouseout", "app.onmouseoutTable()");
    }

    private HTMLImageElementImpl getSelectedImage() {
        Object srcEle = this.browser.getSelectObj();
        if (srcEle != null) {
            if (srcEle instanceof HTMLImageElementImpl) {
                return (HTMLImageElementImpl) srcEle;
            }
        }
        return null;
    }

    private HTMLTableElementImpl getSelectedTable() {
        Object srcEle = this.browser.getSelectObj();
        if (srcEle != null) {
            while (srcEle != null && !(srcEle instanceof HTMLTableElementImpl)) {
                srcEle = ((Node) srcEle).getParentNode();
            }

            if (srcEle != null && srcEle instanceof HTMLTableElementImpl) {
                return (HTMLTableElementImpl) srcEle;
            }
        }
        return null;
    }

    public void onclickImage() {
        HTMLImageElementImpl img = getSelectedImage();
        if (img == null) {
            return;
        }
        this.browser.paintOverlap(img);
    }

    public void onmouseoverImage() {
        selImg = getSelectedImage();
        if (selImg == null) {
            return;
        }
        opacity = selImg.getStyle().getPropertyValue("opacity");
        if (opacity == null) {
            selImg.getStyle().setProperty("opacity", "0.5", "");
        } else {
            Integer op = Integer.parseInt(opacity);
            if (op <= 0.5) {
                selImg.getStyle().setProperty("opacity", op * 2 + "", "");
            } else {
                selImg.getStyle().setProperty("opacity", op / 2 + "", "");
            }
        }
    }

    public void onmouseoutImage() {
        if (selImg == null) {
            return;
        }
        if (opacity != null) {
            selImg.getStyle().setProperty("opacity", opacity, "");
        } else {
            selImg.getStyle().removeProperty("opacity");
        }
    }

    public void onclickTable() {
        HTMLTableElementImpl table = getSelectedTable();
        if (table == null) {
            return;
        }
        this.browser.paintOverlap(table);
    }

    public void onmouseoverTable() {
        selTable = getSelectedTable();
        if (selTable == null) {
            return;
        }
        opacity = selTable.getStyle().getPropertyValue("opacity");
        if (opacity == null) {
            selTable.getStyle().setProperty("opacity", "0.5", "");
        } else {
            Integer op = Integer.parseInt(opacity);
            if (op <= 0.5) {
                selTable.getStyle().setProperty("opacity", op * 2 + "", "");
            } else {
                selTable.getStyle().setProperty("opacity", op / 2 + "", "");
            }
        }
    }

    public void onmouseoutTable() {
        if (selTable == null) {
            return;
        }
        if (opacity != null) {
            selTable.getStyle().setProperty("opacity", opacity, "");
        } else {
            selTable.getStyle().removeProperty("opacity");
        }
    }
}
