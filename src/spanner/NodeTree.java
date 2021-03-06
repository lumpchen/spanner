/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package spanner;

import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.html.HTMLImageElement;
import org.w3c.dom.html.HTMLTableElement;

/**
 *
 * @author lim16
 */
public class NodeTree extends TreeView {

    private JSCallback jsCallback;
    private TreeItem<String> root;

    public NodeTree() {
        super();
        this.root = new TreeItem<>("DOM Explorer");
        this.setShowRoot(true);
        this.setRoot(this.root);
        this.root.setExpanded(true);
    }

    public void setJSCallback(JSCallback jsCallback) {
        this.jsCallback = jsCallback;
    }

    public void showDom(Document doc) {
        removeAllItems(this.root);
        Element docEle = doc.getDocumentElement();
        this.showNode(docEle, this.root);
    }

    private void removeAllItems(TreeItem treeNode) {
        int n = treeNode.getChildren().size();
        for (int i = n - 1; i >= 0; i--) {
            TreeItem item = (TreeItem) treeNode.getChildren().get(i);
            if (item.getChildren().size() > 0) {
                removeAllItems(item);
            }
            treeNode.getChildren().remove(item);
        }
    }

    private void showNode(Node node, TreeItem<String> parent) {
        NodeList nodeList = node.getChildNodes();
        for (int i = 0, n = nodeList.getLength(); i < n; i++) {
            Node child = nodeList.item(i);
            if (child.getNodeType() == Node.ELEMENT_NODE) {
                if (child instanceof HTMLImageElement) {
                    this.jsCallback.addCallbackForImage((Element) child);
                } else if (child instanceof HTMLTableElement) {
                    this.jsCallback.addCallbackForTable((Element) child);
                }

                TreeItem<String> item = new TreeItem<>(child.getNodeName());
                parent.getChildren().add(item);
                showNode(child, item);
            }
        }
    }
}
