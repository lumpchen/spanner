/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package spanner;

import java.io.File;
import javafx.geometry.HPos;
import javafx.geometry.Side;
import javafx.geometry.VPos;
import javafx.scene.control.Accordion;
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.stage.Stage;

/**
 *
 * @author lim16
 */
public class WKEditor extends Region {

    private WKToolBar toolBar;
    private WKBrowser browser;
    private NodeTree nodeTree;
    private HTMLTextPane htmlTextPane;
    private SplitPane splitPane;
    private BorderPane borderPane;

    public WKEditor(Stage stage) {
        super();

        this.browser = new WKBrowser(stage);
        this.toolBar = new WKToolBar(stage, this.browser);
        this.nodeTree = new NodeTree();
        this.browser.setNodeTreeView(nodeTree);
        this.htmlTextPane = new HTMLTextPane();
        this.browser.setHTMLTextView(htmlTextPane);

        this.splitPane = new SplitPane();
        TabPane tabPane = new TabPane();
        tabPane.setSide(Side.BOTTOM);
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);

        final Tab tab1 = new Tab();
        tab1.setText("   Editor   ");
        tab1.setContent(this.browser.getView());

        final Tab tab2 = new Tab();
        tab2.setText("   HTML   ");
        tab2.setContent(this.htmlTextPane);
        tabPane.getTabs().addAll(tab1, tab2);


        BorderPane left = new BorderPane();
        left.setTop(this.toolBar);
        left.setCenter(tabPane);

        this.splitPane.getItems().addAll(left, createPane());
        this.splitPane.setDividerPositions(0.7f);

        this.borderPane = new BorderPane();
        this.borderPane.setCenter(this.splitPane);

        HBox hbox = new HBox();
        Label bottomLabel = new Label("status message.");
        hbox.getChildren().add(bottomLabel);
        this.borderPane.setBottom(hbox);

        this.getChildren().add(this.borderPane);
    }

    private Accordion createPane() {
        TitledPane domTreePane = new TitledPane();
        domTreePane.setText("Document tree");
        domTreePane.setContent(this.nodeTree);

        TitledPane stylePane = new TitledPane();
        stylePane.setText("Style");

        final Accordion accordion = new Accordion();
        accordion.getPanes().add(domTreePane);
        accordion.getPanes().add(stylePane);

        return accordion;
    }

    public void openFile(File f) {
        this.browser.openFile(f);
    }

    protected void layoutChildren() {
        double w = getWidth();
        double h = getHeight();
        layoutInArea(this.borderPane, 0, 0, w, h, 0, HPos.CENTER, VPos.CENTER);
    }
}
