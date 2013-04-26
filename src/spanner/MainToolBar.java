/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package spanner;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.control.Button;
import javafx.scene.control.Separator;
import javafx.scene.control.ToolBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import spanner.fo.FopAgent;

/**
 *
 * @author lim16
 */
public class MainToolBar extends GridPane {

    private Stage primaryStage;
    private ToolBar bar;
    private WKEditor edtor;

    public MainToolBar(Stage stage, WKEditor edtor) {
        super();

        this.primaryStage = stage;
        this.edtor = edtor;

        ColumnConstraints localColumnConstraints = new ColumnConstraints();
        localColumnConstraints.setHgrow(Priority.ALWAYS);
        this.getColumnConstraints().add(localColumnConstraints);

        this.getStyleClass().add("html-editor");
        this.bar = new ToolBar();
        this.bar.getStyleClass().add("top-toolbar");
        this.add(this.bar, 0, 0);

        Button openButton = new Button();
        openButton.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("spanner_open.png"))));
        openButton.setOnAction(new EventHandler() {
            @Override
            public void handle(Event t) {
                actionOpenButton(t);
            }
        });

        this.bar.getItems().add(new Separator());

        Button previewButton = new Button();
        previewButton.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("preview.png"))));
        previewButton.setOnAction(new EventHandler() {
            @Override
            public void handle(Event t) {
                actionPreviewButton(t);
            }
        });

        this.bar.getItems().add(openButton);
        this.bar.getItems().add(previewButton);
        this.bar.setPrefHeight(60);
    }

    private void actionOpenButton(Event t) {
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("HTML files (*.html)", "*.html");
        fileChooser.getExtensionFilters().add(extFilter);
        extFilter = new FileChooser.ExtensionFilter("All files", "*.*");
        fileChooser.getExtensionFilters().add(extFilter);
        File openFile = fileChooser.showOpenDialog(primaryStage);
        if (openFile != null) {
            primaryStage.setTitle(openFile.getAbsolutePath());
            edtor.openFile(openFile);
        }
    }

    private void actionPreviewButton(Event t) {
        String html = this.edtor.getHtml();
        try {
            FopAgent.preview(html, "c:/000.pdf");
        } catch (WKException ex) {
            Logger.getLogger(MainToolBar.class.getName()).log(Level.SEVERE, null, ex);
            FXOptionPane.showMessageDialog(primaryStage, "Failed to preview.", FXOptionPane.Title.WARNING);
        }
    }

    protected void layoutChildren() {
        double w = getWidth();
        double h = getHeight();
        layoutInArea(this.bar, 0, 0, w, h, 0, HPos.CENTER, VPos.CENTER);
    }
}
