/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package spanner;

import com.sun.webpane.webkit.dom.HTMLImageElementImpl;
import java.io.IOException;
import java.net.URL;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 *
 * @author lim16
 */
public class InsertImageDlg extends Stage {

    private String src;
    private String alt;
    private boolean isOk = false;
    private int width;
    private int height;
    private String floating;
    private InsertImageDlgFXMLController controller;

    public InsertImageDlg(Stage primaryStage) throws IOException {
        super();
        super.initModality(Modality.WINDOW_MODAL);
        super.initOwner(primaryStage);

        URL fxml = getClass().getResource("InsertImageDlgFXML.fxml");
        FXMLLoader loader = new FXMLLoader(fxml);
        Parent rootPane = (Parent) loader.load();

        this.controller = loader.getController();
        this.controller.setApp(this);

        Scene scene = new Scene(rootPane);
        super.setScene(scene);
        super.centerOnScreen();
        super.setTitle("Insert Image");
    }

    public void setOk(boolean isOk) {
        this.isOk = isOk;
        this.close();
    }

    public boolean isOk() {
        return this.isOk;
    }

    public String getSrc() {
        return this.src;
    }

    public void setSrc(String src) {
        this.src = src;
    }

    public String getAlt() {
        return this.alt;
    }

    public void setAlt(String alt) {
        this.alt = alt;
    }

    public int getImageWidth() {
        return this.width;
    }

    public void setImageWidth(int w) {
        this.width = w;
    }

    public int getImageHeight() {
        return this.height;
    }

    public void setImageHeight(int h) {
        this.height = h;
    }

    public void setFloat(String floating) {
        this.floating = floating;
    }

    public String getFloat() {
        return this.floating;
    }

    public void setImage(HTMLImageElementImpl img) {
        if (img == null) {
            return;
        }
        this.src = img.getSrc();
        this.width = Integer.parseInt(img.getWidth());
        this.height = Integer.parseInt(img.getHeight());
        this.floating = img.getStyle().getPropertyValue("float");
        this.controller.initImage();
    }
}
