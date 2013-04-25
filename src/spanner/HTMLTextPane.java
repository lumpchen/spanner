/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package spanner;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TextArea;

/**
 *
 * @author lim16
 */
public class HTMLTextPane extends TextArea {

    public HTMLTextPane() {
        super();
        this.setEditable(false);
        this.setWrapText(true);
        this.setFocusTraversable(true);
        this.focusedProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue ov, Object t, Object t1) {
            }
        });
    }
}
