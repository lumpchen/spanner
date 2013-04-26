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
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.web.WebView;
import javafx.stage.FileChooser;

/**
 * FXML Controller class
 *
 * @author lim16
 */
public class InsertImageDlgFXMLController implements Initializable {

    private String template = "<!DOCTYPE html>\n"
            + "<html>\n"
            + "<body>\n"
            + "\n"
            + "<h4>Let the image float to the "
            + "<span style=\"color:blue\">img_floating</span>:</h4>\n"
            + "<p>This is some text. "
            + "<img src=\"img_src\" alt=\"img_alt\" width=\"img_w\" height=\"img_h\" style=\"float:img_floating\">"
            + "This is some text. This is some text. This is some text.</p>\n"
            + "\n"
            + "</body>\n"
            + "</html>";
    private final String[] FLOATING = {"left", "right", "none", "inherit"};
    private final String defaultFloat = FLOATING[2];
    private final String PLACEHOLDER = "placeholder";
    private final String placeholder_src = getClass().getResource("imageplaceholder.png").toExternalForm();
    private InsertImageDlg primaryStage;
    private String src;
    private String alt;
    private int width;
    private int height;
    private String floating;
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
    @FXML
    private Button placeholderButton;
    @FXML
    private ComboBox<String> floatComboBox;
    @FXML
    private WebView webView;

    public void setApp(InsertImageDlg stage) {
        this.primaryStage = stage;
    }

    public void initImage() {
        this.src = this.primaryStage.getSrc();
        if (this.placeholder_src.equalsIgnoreCase(this.src)) {
            this.urlTextField.setText(this.PLACEHOLDER);
        } else {
            this.urlTextField.setText(src);
        }
        this.width = this.primaryStage.getImageWidth();
        this.widthTextField.setText(this.width + "");
        this.height = this.primaryStage.getImageHeight();
        this.heightTextField.setText(this.height + "");
        this.floating = this.primaryStage.getFloat();
        if (this.floating != null) {
            this.floatComboBox.setValue(this.floating);
        }
    }

    /**
     * initialises the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.floatComboBox.getItems().addAll(FLOATING);
        this.floatComboBox.setValue(defaultFloat);
        this.floatComboBox.valueProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue ov, String t, String t1) {
                floating = t1;
                showSample();
            }
        });

        this.urlTextField.setPromptText("Select an image.");

        this.widthTextField.setPromptText("72");
        this.widthTextField.textProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue ov, Object t, Object t1) {
                if (t1 != null && t1 instanceof String) {
                    String iw = t1.toString().trim();
                    if (iw.length() > 0) {
                        try {
                            width = Integer.parseInt(iw);
                            showSample();
                        } catch (Exception e) {
                            FXOptionPane.showMessageDialog(primaryStage, "Invalid width.", FXOptionPane.Title.WARNING);
                            widthTextField.setText(t.toString());
                        }
                    }
                }
            }
        });

        this.heightTextField.setPromptText("72");
        this.heightTextField.textProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue ov, Object t, Object t1) {
                if (t1 != null && t1 instanceof String) {
                    String ih = t1.toString().trim();
                    if (ih.length() > 0) {
                        try {
                            height = Integer.parseInt(ih);
                            showSample();
                        } catch (Exception e) {
                            FXOptionPane.showMessageDialog(primaryStage, "Invalid height.", FXOptionPane.Title.WARNING);
                            heightTextField.setText(t.toString());
                        }
                    }
                }
            }
        });
    }

    private void showSample() {
        String html = template;
        if (this.src != null) {
            html = html.replace("img_src", this.src);
        } else {
            html = html.replace("img_src", this.placeholder_src);
        }

        if (this.width > 0) {
            html = html.replace("img_w", this.width + "");
        } else {
            html = html.replace("img_w", 72 + "");
        }

        if (this.height > 0) {
            html = html.replace("img_h", this.height + "");
        } else {
            html = html.replace("img_h", 72 + "");
        }

        if (this.floating != null) {
            html = html.replace("img_floating", this.floating);
        } else {
            html = html.replace("img_floating", defaultFloat);
        }

        this.webView.getEngine().loadContent(html);
    }

    @FXML
    private void onOkButtonAction(ActionEvent event) {
        String text = this.urlTextField.getText();
        if (text == null || text.length() <= 0) {
            FXOptionPane.showMessageDialog(primaryStage, "No file selected.", FXOptionPane.Title.WARNING);
            this.urlTextField.requestFocus();
            return;
        }

        String w = this.widthTextField.getText();
        if (w == null || w.length() <= 0) {
            FXOptionPane.showMessageDialog(primaryStage, "Image width is zero.", FXOptionPane.Title.WARNING);
            this.widthTextField.requestFocus();
            return;
        }

        String h = this.heightTextField.getText();
        if (h == null || h.length() <= 0) {
            FXOptionPane.showMessageDialog(primaryStage, "Image height is zero.", FXOptionPane.Title.WARNING);
            this.heightTextField.requestFocus();
            return;
        }

        if (this.PLACEHOLDER.equalsIgnoreCase(text.trim())) {
            this.primaryStage.setSrc(this.src);
        } else {
            this.primaryStage.setSrc(text.trim());
        }

        this.primaryStage.setAlt(this.alt);
        this.primaryStage.setImageWidth(Integer.parseInt(w.trim()));
        this.primaryStage.setImageHeight(Integer.parseInt(h.trim()));
        this.primaryStage.setFloat(floating);
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
                showSample();
            } catch (MalformedURLException ex) {
                Logger.getLogger(WKToolBar.class
                        .getName()).log(Level.SEVERE, null, ex);
                FXOptionPane.showMessageDialog(primaryStage,
                        "Cannot open the url.", FXOptionPane.Title.ERROR, ex);
            }
        }
    }

    @FXML
    private void placeholderButtonAction(ActionEvent event) {
        this.src = this.placeholder_src;
        this.alt = "placeholder";
        this.urlTextField.setText(PLACEHOLDER);
        showSample();
    }
}
