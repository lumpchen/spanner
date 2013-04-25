/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package spanner;

import com.sun.webpane.webkit.dom.TextImpl;
import javafx.scene.web.WebEngine;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 *
 * @author lim16
 */
public class WKNodeBuilder {

    public static void buildImageNode(WebEngine webEngine, String src, String alt,
            int width, int height, String style) throws WKException {
        Object selection = webEngine.executeScript("window.getSelection().focusNode");
        if (selection != null) {
            System.out.println(selection.getClass().getName());
            TextImpl n = (TextImpl) selection;
            Document doc = webEngine.getDocument();
            Element img = doc.createElement("img");
            img.setAttribute("src", src);
            img.setAttribute("alt", alt);
            img.setAttribute("height", height + "");
            img.setAttribute("width", width + "");

            img.setAttribute("ondblclick", "app.ondblclickImage()");
            img.setAttribute("onmouseover", "app.onmouseoverImage()");
            img.setAttribute("onmouseout", "app.onmouseoutImage()");

            n.getParentNode().insertBefore(img, n);
            System.err.println("name:  " + n.toString());
            System.err.println("value:  " + n.getNodeValue());
        } else {
            throw new WKException(WKNodeBuilder.class, "", null);
        }
    }
}
