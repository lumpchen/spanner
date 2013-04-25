/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package spanner;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.stage.FileChooser;

/**
 * FXML Controller class
 *
 * @author lim16
 */
public class InsertImageDlgFXMLController implements Initializable {

    private InsertImageDlg primaryStage;
    private String src;
    private String alt;
    private int width;
    private int height;
    @FXML
    private Button okButton;
    @FXML
    private Button cancelButton;
    @FXML
    private TitledPane x1;
    @FXML
    private Button openButton;
    @FXML
    private TextField urlTextField;
    @FXML
    private TextField widthTextField;
    @FXML
    private TextField heightTextField;

    public void setApp(InsertImageDlg stage) {
        this.primaryStage = stage;
    }

    public void initImage() {
        this.src = this.primaryStage.getSrc();
        this.urlTextField.setText(src);
        this.width = this.primaryStage.getImageWidth();
        this.widthTextField.setText(this.width + "");
        this.height = this.primaryStage.getImageHeight();
        this.heightTextField.setText(this.height + "");
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }

    @FXML
    private void onOkButtonAction(ActionEvent event) {
        String text = this.urlTextField.getText();
        if (text == null || text.length() <= 0) {
            FXOptionPane.showConfirmDialog(primaryStage, "No file selected.", FXOptionPane.Title.WARNING);
            this.urlTextField.requestFocus();
            return;
        }

        String w = this.widthTextField.getText();
        if (w == null || w.length() <= 0) {
            FXOptionPane.showConfirmDialog(primaryStage, "Image width is zero.", FXOptionPane.Title.WARNING);
            this.widthTextField.requestFocus();
            return;
        }

        String h = this.heightTextField.getText();
        if (h == null || h.length() <= 0) {
            FXOptionPane.showConfirmDialog(primaryStage, "Image height is zero.", FXOptionPane.Title.WARNING);
            this.heightTextField.requestFocus();
            return;
        }

        this.primaryStage.setSrc(text.trim());
        this.primaryStage.setAlt(this.alt);
        this.primaryStage.setImageWidth(Integer.parseInt(w.trim()));
        this.primaryStage.setImageHeight(Integer.parseInt(h.trim()));

        this.primaryStage.setOk(true);
    }

    @FXML
    private void onCancelButtonAction(ActionEvent event) {
        this.primaryStage.setOk(false);
    }

    @FXML
    private void onOpenButtonAction(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("JPG files (*.jpg)", "*.jpg");
        fileChooser.getExtensionFilters().add(extFilter);
        File openFile = fileChooser.showOpenDialog(this.primaryStage);
        if (openFile != null) {
            try {
                this.src = openFile.toURI().toURL().toExternalForm();
                this.alt = openFile.getName();
                this.urlTextField.setText(this.src);
            } catch (MalformedURLException ex) {
                Logger.getLogger(WKToolBar.class.getName()).log(Level.SEVERE, null, ex);
                FXOptionPane.showMessageDialog(primaryStage, "Cannot open the url.", FXOptionPane.Title.ERROR, ex);
            }
        }
    }
}
