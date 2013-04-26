/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package spanner;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TitledPane;
import javafx.scene.web.WebView;

/**
 * FXML Controller class
 *
 * @author lim16
 */
public class InsertTableDlgFXMLController implements Initializable {

    private InsertTableDlg primaryStage;
    @FXML
    private WebView webView;
    @FXML
    private Button okButton;
    @FXML
    private Button cancelButton;
    @FXML
    private TitledPane x1;

    public void setApp(InsertTableDlg stage) {
        this.primaryStage = stage;
    }

    /**
     * initialises the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    @FXML
    private void okButtonAction(ActionEvent event) {
        this.primaryStage.setOk(true);
    }

    @FXML
    private void cancelButtonAction(ActionEvent event) {
        this.primaryStage.setOk(false);
    }
}
