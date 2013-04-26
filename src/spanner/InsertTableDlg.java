/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package spanner;

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
public class InsertTableDlg extends Stage {

    private boolean isOk = false;
    private InsertTableDlgFXMLController controller;

    public InsertTableDlg(Stage primaryStage) throws IOException {
        super();
        super.initModality(Modality.WINDOW_MODAL);
        super.initOwner(primaryStage);

        URL fxml = getClass().getResource("InsertTableDlgFXML.fxml");
        FXMLLoader loader = new FXMLLoader(fxml);
        Parent rootPane = (Parent) loader.load();

        this.controller = loader.getController();
        this.controller.setApp(this);

        Scene scene = new Scene(rootPane);
        super.setScene(scene);
        super.centerOnScreen();
        super.setTitle("Insert Table");
    }

    public void setOk(boolean isOk) {
        this.isOk = isOk;
        this.close();
    }

    public boolean isOk() {
        return this.isOk;
    }

    public HTMLTableSetting getHTMLTableSetting() {
        return null;
    }
}
