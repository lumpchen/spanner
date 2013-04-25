/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package spanner;

import com.sun.webpane.platform.WebPage;
import com.sun.webpane.platform.event.WCFocusEvent;
import com.sun.webpane.sg.Accessor;
import com.sun.webpane.webkit.dom.HTMLImageElementImpl;
import com.sun.webpane.webkit.dom.TextImpl;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Worker;
import javafx.event.EventHandler;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import netscape.javascript.JSObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.html.HTMLElement;

/**
 *
 * @author lim16
 */
public class WKBrowser {

    private Stage stage;
    private WebView webView;
    private WebEngine webEngine;
    private WebPage webPage;
    private NodeTree nodeTree;
    private HTMLTextPane htmlTextPane;
    public final String INIT_HTML = "<html><body><head></head><p>Hello</p><body></html>";

    public WKBrowser(Stage stage) {
        this.stage = stage;
        this.webView = new WebView();
        this.webEngine = this.webView.getEngine();
        this.webPage = Accessor.getPageFor(this.webEngine);

        this.webEngine.getLoadWorker().stateProperty().addListener(new ChangeListener<Worker.State>() {
            @Override
            public void changed(ObservableValue<? extends Worker.State> ov, Worker.State t, Worker.State t1) {
                if (t1 == Worker.State.SUCCEEDED) {
                    loadSuccess();
                }
            }
        });

        this.webEngine.loadContent(INIT_HTML);
    }

    public void openFile(File f) {
        this.webEngine.load("file:/" + f.getAbsolutePath());
    }

    private void loadSuccess() {
        Document doc = webEngine.getDocument();
        HTMLElement body = (HTMLElement) doc.getElementsByTagName("body").item(0);
        this.contentEditable(body);

        JSObject window = (JSObject) webEngine.executeScript("window");
        window.setMember("app", new JavaApp());

        if (this.nodeTree != null) {
            this.nodeTree.showDom(doc);
        }

        if (this.htmlTextPane != null) {
            this.htmlTextPane.setText(this.webPage.getHtml(this.webPage.getMainFrame()));
        }
    }

    private void contentEditable(HTMLElement node) {
        node.setAttribute("contenteditable", "true");
    }

    public WebView getView() {
        return this.webView;
    }

    public void setNodeTreeView(NodeTree nodeTree) {
        this.nodeTree = nodeTree;
    }

    public void setHTMLTextView(HTMLTextPane htmlTextPane) {
        this.htmlTextPane = htmlTextPane;
    }

    public void updateView() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                nodeTree.showDom(webEngine.getDocument());
                htmlTextPane.setText(webPage.getHtml(webPage.getMainFrame()));
            }
        });
    }

    public boolean isCommandEnabled(String paramString) {
        return this.webPage.queryCommandEnabled(paramString);
    }

    public boolean getCommandState(String command) {
        return this.webPage.queryCommandState(command);
    }

    public String getCommandValue(String command) {
        return this.webPage.queryCommandValue(command);
    }

    public boolean executeCommand(String command, String value) {
        return this.webPage.executeCommand(command, value);
    }

    public String getClientSelectedText() {
        return this.webPage.getClientSelectedText();
    }

    public int getClientCommittedTextLength() {
        return this.webPage.getClientCommittedTextLength();
    }

    public void dispatchFocusEvent(WCFocusEvent event) {
        this.webPage.dispatchFocusEvent(event);
    }

    public void addKeyPressedEventHandler(EventHandler handler) {
        this.webView.addEventHandler(KeyEvent.KEY_PRESSED, handler);
    }

    public void addKeyReleasedEventHandler(EventHandler handler) {
        this.webView.addEventHandler(KeyEvent.KEY_RELEASED, handler);
    }

    public void addFocusChangedListener(ChangeListener listener) {
        this.webView.focusedProperty().addListener(listener);
    }

    public void addMouseEventHandler(EventHandler handler) {
        this.webView.addEventHandler(MouseEvent.ANY, handler);
    }

    public void requestFocus() {
        this.webView.requestFocus();
    }

    public boolean isFocused() {
        return this.webView.isFocused();
    }

    public void insertTableNode(HTMLTable table) throws WKException {

        Object selection = webEngine.executeScript("window.getSelection().focusNode");
        if (selection != null) {
            int row = table.getRow();
            int col = table.getCol();

        } else {
            throw new WKException(WKNodeBuilder.class, "Not found insertion point.", null);
        }
    }

    private void insertTableNode(Node focusNode, Element table) {
        if (focusNode instanceof TextImpl) {
            TextImpl textNode = (TextImpl) focusNode;
            Node parent = textNode.getParentNode();
            Integer offset = (Integer) webEngine.executeScript("window.getSelection().focusOffset");
        } else {
            Integer offset = (Integer) webEngine.executeScript("window.getSelection().focusOffset");
            NodeList children = focusNode.getChildNodes();
            focusNode.insertBefore(table, children.item(offset));
        }
    }

    public void insertImageNode(String src, String alt, int width, int height,
            String style) throws WKException {
        Object selection = webEngine.executeScript("window.getSelection().focusNode");
        if (selection != null) {
            try {
                System.out.println(selection.getClass().getName());
                Document doc = webEngine.getDocument();
                Element img = doc.createElement("img");
                img.setAttribute("src", src);
                img.setAttribute("alt", alt);
                img.setAttribute("height", height + "");
                img.setAttribute("width", width + "");

                img.setAttribute("ondblclick", "app.ondblclickImage()");
                img.setAttribute("onmouseover", "app.onmouseoverImage()");
                img.setAttribute("onmouseout", "app.onmouseoutImage()");

                this.insertImageNode((Node) selection, img);
            } catch (Exception e) {
                throw new WKException(WKNodeBuilder.class, "Failed to insert image.", null);
            }
        } else {
            throw new WKException(WKNodeBuilder.class, "Not found insertion point.", null);
        }
    }

    private void insertImageNode(Node focusNode, Element img) {
        if (focusNode instanceof TextImpl) {
            TextImpl textNode = (TextImpl) focusNode;
            Node parent = textNode.getParentNode();
            Integer offset = (Integer) webEngine.executeScript("window.getSelection().focusOffset");

            String text = textNode.getTextContent();
            if (offset <= 0) {
                parent.insertBefore(img, textNode);
            } else if (offset == text.length()) {
                parent.appendChild(img);
            } else {
                String s1 = text.substring(0, offset);
                String s2 = text.substring(offset);

                Node before = textNode.getOwnerDocument().createTextNode(s1);
                Node after = textNode.getOwnerDocument().createTextNode(s2);

                Node next = textNode.getNextSibling();
                parent.removeChild(textNode);
                if (next != null) {
                    parent.insertBefore(after, next);
                    parent.insertBefore(img, after);
                    parent.insertBefore(before, img);
                } else {
                    parent.appendChild(before);
                    parent.appendChild(img);
                    parent.appendChild(after);
                }
            }
        } else {
            Integer offset = (Integer) webEngine.executeScript("window.getSelection().focusOffset");
            NodeList children = focusNode.getChildNodes();
            focusNode.insertBefore(img, children.item(offset));
        }
    }

    private HTMLImageElementImpl getSelectedImage() {
        Object e = this.webView.getEngine().executeScript("window.event.type");
        if (e == null) {
            return null;
        }

        Object srcEle = this.webView.getEngine().executeScript("window.event.srcElement");
        if (srcEle != null) {
            if (srcEle instanceof HTMLImageElementImpl) {
                return (HTMLImageElementImpl) srcEle;
            }
        }
        return null;
    }

    public class JavaApp {

        private String oldBorder;
        private String mouseOverBorder = "border:1px solid blue";

        public void ondblclickImage() {
            HTMLImageElementImpl selImg = getSelectedImage();
            if (selImg == null) {
                return;
            }
            InsertImageDlg dlg;
            try {
                dlg = new InsertImageDlg(stage);
                dlg.setImage(selImg);
                dlg.showAndWait();
                if (dlg.isOk()) {
                    String src = dlg.getSrc();
                    int w = dlg.getImageWidth();
                    int h = dlg.getImageHeight();

                    selImg.setSrc(src);
                    selImg.setWidth(w + "");
                    selImg.setHeight(h + "");
                    selImg.setAlign("right");
                }
            } catch (IOException ex) {
                Logger.getLogger(WKBrowser.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        public void onmouseoverImage() {
            HTMLImageElementImpl selImg = getSelectedImage();
            if (selImg == null) {
                return;
            }
            this.oldBorder = selImg.getBorder();
            selImg.setAttribute("style", mouseOverBorder);
        }

        public void onmouseoutImage() {
            HTMLImageElementImpl selImg = getSelectedImage();
            if (selImg == null) {
                return;
            }
            if (this.oldBorder != null) {
                selImg.setAttribute("style", this.oldBorder);
            } else {
                selImg.removeAttribute("style");
            }
        }
    }
}
