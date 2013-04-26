/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package spanner;

import com.sun.javafx.PlatformUtil;
import com.sun.javafx.scene.control.skin.FXVK;
import com.sun.webpane.platform.event.WCFocusEvent;
import java.io.IOException;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Separator;
import javafx.scene.control.TextInputControl;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.ToolBar;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

/**
 *
 * @author lim16
 */
public class WKToolBar extends GridPane {

    private Stage stage;
    private WKBrowser browser;
    private ToolBar bar1;
    private ToolBar bar2;
    private ResourceBundle resources;
    private Button cutButton;
    private Button copyButton;
    private Button pasteButton;
    private Button insertHorizontalRuleButton;
    private ToggleGroup alignmentToggleGroup;
    private ToggleButton alignLeftButton;
    private ToggleButton alignCenterButton;
    private ToggleButton alignRightButton;
    private ToggleButton alignJustifyButton;
    private ToggleButton bulletsButton;
    private ToggleButton numbersButton;
    private Button indentButton;
    private Button outdentButton;
    private ComboBox formatComboBox;
    private Map<String, String> formatStyleMap;
    private Map<String, String> styleFormatMap;
    private ComboBox fontFamilyComboBox;
    private ComboBox fontSizeComboBox;
    private Map<String, String> fontSizeMap;
    private Map<String, String> sizeFontMap;
    private ToggleButton boldButton;
    private ToggleButton italicButton;
    private ToggleButton underlineButton;
    private ToggleButton strikethroughButton;
    private ColorPicker fgColorPicker;
    private ColorPicker bgColorPicker;
    private static final Color DEFAULT_BG_COLOR = Color.WHITE;
    private static final Color DEFAULT_FG_COLOR = Color.BLACK;
    private static final String[][] DEFAULT_FORMAT_MAPPINGS = {
        {"<p>", "", "3"},
        {"<h1>", "bold", "6"},
        {"<h2>", "bold", "5"},
        {"<h3>", "bold", "4"},
        {"<h4>", "bold", "3"},
        {"<h5>", "bold", "2"},
        {"<h6>", "bold", "1"}};
    private static final String[] DEFAULT_WINDOWS_7_MAPPINGS = {"Windows 7", "Segoe UI", "12px", "", "120"};
    private static final String[][] DEFAULT_OS_MAPPINGS = {
        {"Windows XP", "Tahoma", "12px", "", "96"},
        {"Windows Vista", "Segoe UI", "12px", "", "96"},
        DEFAULT_WINDOWS_7_MAPPINGS,
        {"Mac OS X", "Lucida Grande", "12px", "", "72"},
        {"Linux", "Lucida Sans", "12px", "", "96"}};
    private static final String DEFAULT_OS_FONT = getOSMappings()[1];
    private boolean resetToolbarState = false;
    public static final String CUT_COMMAND = "cut";
    public static final String COPY_COMMAND = "copy";
    public static final String PASTE_COMMAND = "paste";
    public static final String UNDO_COMMAND = "undo";
    public static final String REDO_COMMAND = "redo";
    public static final String INSERT_HORIZONTAL_RULE_COMMAND = "inserthorizontalrule";
    public static final String ALIGN_LEFT_COMMAND = "justifyleft";
    public static final String ALIGN_CENTER_COMMAND = "justifycenter";
    public static final String ALIGN_RIGHT_COMMAND = "justifyright";
    public static final String ALIGN_JUSTIFY_COMMAND = "justifyfull";
    public static final String BULLETS_COMMAND = "insertUnorderedList";
    public static final String NUMBERS_COMMAND = "insertOrderedList";
    public static final String INDENT_COMMAND = "indent";
    public static final String OUTDENT_COMMAND = "outdent";
    public static final String FORMAT_COMMAND = "formatblock";
    public static final String FONT_FAMILY_COMMAND = "fontname";
    public static final String FONT_SIZE_COMMAND = "fontsize";
    public static final String BOLD_COMMAND = "bold";
    public static final String ITALIC_COMMAND = "italic";
    public static final String UNDERLINE_COMMAND = "underline";
    public static final String STRIKETHROUGH_COMMAND = "strikethrough";
    public static final String FOREGROUND_COLOR_COMMAND = "forecolor";
    public static final String BACKGROUND_COLOR_COMMAND = "backcolor";
    private static final String FORMAT_PARAGRAPH = "<p>";
    private static final String FORMAT_HEADING_1 = "<h1>";
    private static final String FORMAT_HEADING_2 = "<h2>";
    private static final String FORMAT_HEADING_3 = "<h3>";
    private static final String FORMAT_HEADING_4 = "<h4>";
    private static final String FORMAT_HEADING_5 = "<h5>";
    private static final String FORMAT_HEADING_6 = "<h6>";
    private static final String SIZE_XX_SMALL = "1";
    private static final String SIZE_X_SMALL = "2";
    private static final String SIZE_SMALL = "3";
    private static final String SIZE_MEDIUM = "4";
    private static final String SIZE_LARGE = "5";
    private static final String SIZE_X_LARGE = "6";
    private static final String SIZE_XX_LARGE = "7";
    private static final String INSERT_NEW_LINE_COMMAND = "insertnewline";
    private static final String INSERT_TAB_COMMAND = "inserttab";
    //custom
    private Button insertImageButton;
    private Button insertTableButton;
    private boolean updateToolbarStatus = false;

    public WKToolBar(Stage stage, WKBrowser browser) {
        super();

        this.stage = stage;
        this.browser = browser;

        ColumnConstraints localColumnConstraints = new ColumnConstraints();
        localColumnConstraints.setHgrow(Priority.ALWAYS);
        this.getColumnConstraints().add(localColumnConstraints);

        this.getStyleClass().add("html-editor");
        this.bar1 = new ToolBar();
        this.bar1.getStyleClass().add("top-toolbar");
        this.add(this.bar1, 0, 0);

        this.bar2 = new ToolBar();
        this.bar2.getStyleClass().add("bottom-toolbar");
        this.add(this.bar2, 0, 1);
        this.populateToolbars();

        this.initEventHandler();
    }

    private void initEventHandler() {
        this.browser.addKeyPressedEventHandler(new EventHandler() {
            @Override
            public void handle(Event t) {
                final KeyEvent e = (KeyEvent) t;
                handleKeyPressedEvent(e);
                browser.updateView();
            }
        });

        this.browser.addKeyReleasedEventHandler(new EventHandler() {
            @Override
            public void handle(Event t) {
                final KeyEvent e = (KeyEvent) t;
                handleKeyReleasedEvent(e);
            }
        });

        this.browser.addFocusChangedListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue ov, Object t, Object t1) {
                if (t1 instanceof Boolean) {
                    final Boolean newStatus = (Boolean) t1;
                    if (newStatus.booleanValue()) {
                        browser.dispatchFocusEvent(new WCFocusEvent(2, 0));
                        enableToolbar(true);
                    } else {
                        browser.dispatchFocusEvent(new WCFocusEvent(3, 0));
                        enableToolbar(false);
                    }
                    Platform.runLater(new Runnable() {
                        public void run() {
                            updateToolbarState(true);
                        }
                    });
                    if (PlatformUtil.isEmbedded()) {
                        Platform.runLater(new Runnable() {
                            public void run() {
                                if (newStatus.booleanValue()) {
                                    FXVK.attach(browser.getView());
                                } else if ((getScene() == null)
                                        || (getScene().getWindow() == null)
                                        || (!getScene().getWindow().isFocused())
                                        || (!(getScene().getFocusOwner() instanceof TextInputControl))) {
                                    FXVK.detach();
                                }
                            }
                        });
                    }
                }
            }
        });

        this.browser.addMouseEventHandler(new EventHandler() {
            @Override
            public void handle(Event t) {
                updateToolbarState(true);
            }
        });

        this.focusedProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue ov, final Object t, final Object t1) {
                Platform.runLater(new Runnable() {
                    public void run() {
                        if (t1 instanceof Boolean) {
                            if (((Boolean) t1).booleanValue()) {
                                browser.requestFocus();
                            }
                        }
                    }
                });
            }
        });

        this.fontFamilyComboBox.valueProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue ov, Object t, Object t1) {
                if (t1 instanceof String && !updateToolbarStatus) {
                    executeCommand("fontname", (String) t1);
                }
            }
        });

        this.fontSizeComboBox.valueProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue ov, Object t, Object t1) {
                String str = getCommandValue("fontsize");
                if (!t1.equals(str) && !updateToolbarStatus) {
                    executeCommand("fontsize", fontSizeMap.get(t1));
                }
            }
        });

        this.insertHorizontalRuleButton.setOnAction(new EventHandler() {
            @Override
            public void handle(Event t) {
                executeCommand(INSERT_NEW_LINE_COMMAND, null);
                executeCommand(INSERT_HORIZONTAL_RULE_COMMAND, null);
                updateToolbarState(false);
            }
        });

        this.bgColorPicker.setOnAction(new EventHandler() {
            public void handle(Event t) {
                Color newColor = bgColorPicker.getValue();
                if (newColor != null && !updateToolbarStatus) {
                    executeCommand(BACKGROUND_COLOR_COMMAND, rgbToHex(newColor));
                }
            }
        });

        this.fgColorPicker.setOnAction(new EventHandler() {
            public void handle(Event t) {
                Color newColor = fgColorPicker.getValue();
                if (newColor != null && !updateToolbarStatus) {
                    executeCommand(FOREGROUND_COLOR_COMMAND, rgbToHex(newColor));
                }
            }
        });

        this.formatComboBox.valueProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue ov, Object t, Object t1) {
                if (t1 == null) {
                    formatComboBox.setValue(null);
                } else {
                    String style = formatStyleMap.get(t1);
                    executeCommand(FORMAT_COMMAND, style);
                    updateToolbarState(false);

                    for (int i = 0; i < DEFAULT_FORMAT_MAPPINGS.length; i++) {
                        String[] format = DEFAULT_FORMAT_MAPPINGS[i];
                        if (format[0].equalsIgnoreCase(style)) {
                            executeCommand(FONT_SIZE_COMMAND, format[2]);
                            updateToolbarState(false);
                            break;
                        }
                    }
                }
            }
        });

        this.insertImageButton.setOnAction(new EventHandler() {
            @Override
            public void handle(Event t) {
                handleInsertImageEvent((ActionEvent) t);
            }
        });

        this.insertTableButton.setOnAction(new EventHandler() {
            @Override
            public void handle(Event t) {
                handleInsertTableEvent((ActionEvent) t);
            }
        });
    }

    private void handleInsertImageEvent(ActionEvent e) {
        try {
            if (!this.browser.enableInsertElement()) {
                FXOptionPane.showMessageDialog(stage, "Not found insertion point.", FXOptionPane.Title.ERROR);
                return;
            }
            InsertImageDlg dlg = new InsertImageDlg(stage);
            dlg.showAndWait();
            if (dlg.isOk()) {
                String src = dlg.getSrc();
                String alt = dlg.getAlt();
                int w = dlg.getImageWidth();
                int h = dlg.getImageHeight();
                String style = null;

                this.browser.insertImageNode(src, alt, w, h, null);
            }
        } catch (WKException | IOException ex) {
            Logger.getLogger(WKToolBar.class.getName()).log(Level.SEVERE, null, ex);
            FXOptionPane.showMessageDialog(stage, "", FXOptionPane.Title.ERROR, ex);
        }
    }

    private void handleInsertTableEvent(ActionEvent e) {
    }

    private void handleKeyPressedEvent(final KeyEvent e) {
        applyTextFormatting();
        if ((e.getCode() == KeyCode.CONTROL) || (e.getCode() == KeyCode.META)) {
            return;
        }
        if ((e.getCode() == KeyCode.TAB) && (!e.isControlDown())) {
            if (!e.isShiftDown()) {
                if ((getCommandState(BULLETS_COMMAND)) || (getCommandState(NUMBERS_COMMAND))) {
                    executeCommand(INDENT_COMMAND, null);
                } else {
                    executeCommand(INSERT_TAB_COMMAND, null);
                }

            } else if ((getCommandState(BULLETS_COMMAND)) || (getCommandState(NUMBERS_COMMAND))) {
                executeCommand(OUTDENT_COMMAND, null);
            }
            return;
        }

        if (((this.fgColorPicker != null) && (this.fgColorPicker.isShowing()))
                || ((this.bgColorPicker != null) && (this.bgColorPicker.isShowing()))) {
            return;
        }

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                if (browser.getClientSelectedText().isEmpty()) {
                    if ((e.getCode() == KeyCode.UP) || (e.getCode() == KeyCode.DOWN)
                            || (e.getCode() == KeyCode.LEFT) || (e.getCode() == KeyCode.RIGHT)
                            || (e.getCode() == KeyCode.HOME) || (e.getCode() == KeyCode.END)) {
                        updateToolbarState(true);
                    } else if ((e.isControlDown()) || (e.isMetaDown())) {
                        if (e.getCode() == KeyCode.B) {
                            keyboardShortcuts(BOLD_COMMAND);
                        } else if (e.getCode() == KeyCode.I) {
                            keyboardShortcuts(ITALIC_COMMAND);
                        } else if (e.getCode() == KeyCode.U) {
                            keyboardShortcuts(UNDERLINE_COMMAND);
                        }
                        updateToolbarState(true);
                    } else {
                        resetToolbarState = (e.getCode() == KeyCode.ENTER);
                        updateToolbarState(resetToolbarState);
                    }
                    resetToolbarState = false;
                } else if ((e.isShiftDown()) && ((e.getCode() == KeyCode.UP) || (e.getCode() == KeyCode.DOWN)
                        || (e.getCode() == KeyCode.LEFT) || (e.getCode() == KeyCode.RIGHT))) {
                    updateToolbarState(true);
                }
            }
        });
    }

    private void handleKeyReleasedEvent(final KeyEvent e) {
        if ((e.getCode() == KeyCode.CONTROL) || (e.getCode() == KeyCode.META)) {
            return;
        }

        if (((this.fgColorPicker != null) && (this.fgColorPicker.isShowing()))
                || ((this.bgColorPicker != null) && (this.bgColorPicker.isShowing()))) {
            return;
        }
        Platform.runLater(new Runnable() {
            public void run() {
                if (browser.getClientSelectedText().isEmpty()) {
                    if ((e.getCode() == KeyCode.UP) || (e.getCode() == KeyCode.DOWN)
                            || (e.getCode() == KeyCode.LEFT) || (e.getCode() == KeyCode.RIGHT)
                            || (e.getCode() == KeyCode.HOME) || (e.getCode() == KeyCode.END)) {
                        updateToolbarState(true);
                    } else if ((e.isControlDown()) || (e.isMetaDown())) {
                        if (e.getCode() == KeyCode.B) {
                            keyboardShortcuts(BOLD_COMMAND);
                        } else if (e.getCode() == KeyCode.I) {
                            keyboardShortcuts(ITALIC_COMMAND);
                        } else if (e.getCode() == KeyCode.U) {
                            keyboardShortcuts(UNDERLINE_COMMAND);
                        }
                        updateToolbarState(true);
                    } else {
                        resetToolbarState = (e.getCode() == KeyCode.ENTER);
                        if (!resetToolbarState) {
                            updateToolbarState(false);
                        }
                    }
                    resetToolbarState = false;
                }
            }
        });
    }

    private void updateToolbarState(boolean reset) {
        if (!this.browser.isFocused()) {
            return;
        }

        this.updateToolbarStatus = true;

        this.copyButton.setDisable(!isCommandEnabled("cut"));
        this.cutButton.setDisable(!isCommandEnabled("copy"));
        this.pasteButton.setDisable(!isCommandEnabled("paste"));

        this.insertHorizontalRuleButton.setDisable(!isCommandEnabled(INSERT_HORIZONTAL_RULE_COMMAND));

        if (reset) {
            this.alignLeftButton.setDisable(!isCommandEnabled(ALIGN_LEFT_COMMAND));
            this.alignLeftButton.setSelected(getCommandState(ALIGN_LEFT_COMMAND));
            this.alignCenterButton.setDisable(!isCommandEnabled(ALIGN_CENTER_COMMAND));
            this.alignCenterButton.setSelected(getCommandState(ALIGN_CENTER_COMMAND));
            this.alignRightButton.setDisable(!isCommandEnabled(ALIGN_RIGHT_COMMAND));
            this.alignRightButton.setSelected(getCommandState(ALIGN_RIGHT_COMMAND));
            this.alignJustifyButton.setDisable(!isCommandEnabled(ALIGN_JUSTIFY_COMMAND));
            this.alignJustifyButton.setSelected(getCommandState(ALIGN_JUSTIFY_COMMAND));
        } else if (this.alignmentToggleGroup.getSelectedToggle() != null) {
            String str1 = this.alignmentToggleGroup.getSelectedToggle().getUserData().toString();
            if (!getCommandState(str1)) {
                executeCommand(str1, null);
            }
        }

        if (this.alignmentToggleGroup.getSelectedToggle() == null) {
            this.alignmentToggleGroup.selectToggle(this.alignLeftButton);
        }

        this.bulletsButton.setDisable(!isCommandEnabled(BULLETS_COMMAND));
        this.bulletsButton.setSelected(getCommandState(BULLETS_COMMAND));
        this.numbersButton.setDisable(!isCommandEnabled(NUMBERS_COMMAND));
        this.numbersButton.setSelected(getCommandState(NUMBERS_COMMAND));

        this.indentButton.setDisable(!isCommandEnabled(INDENT_COMMAND));
        this.outdentButton.setDisable(!isCommandEnabled(OUTDENT_COMMAND));

        this.formatComboBox.setDisable(!isCommandEnabled(FORMAT_COMMAND));

        String commandValue = getCommandValue(FORMAT_COMMAND);
        if ((commandValue != null)) {
            commandValue = "<" + commandValue + ">";
            String style = (String) this.styleFormatMap.get(commandValue);

            if ((this.resetToolbarState) || (commandValue.equals("<>")) || (commandValue.equalsIgnoreCase("<div>"))) {
                this.formatComboBox.setValue(this.resources.getString("paragraph"));
            } else if (!((String) this.formatComboBox.getValue()).equalsIgnoreCase(style)) {
                this.formatComboBox.setValue(style);
            }
        }

        this.fontFamilyComboBox.setDisable(!isCommandEnabled(FONT_FAMILY_COMMAND));
        String command = getCommandValue(FONT_FAMILY_COMMAND);
        if ((command != null)) {
            if (command.startsWith("'") || command.startsWith("\"")) {
                command = command.substring(1);
            }
            if (command.endsWith("'") || command.endsWith("\"")) {
                command = command.substring(0, command.length() - 1);
            }

            if (!command.isEmpty()) {
                Object selFamily = this.fontFamilyComboBox.getValue();
                if (((selFamily instanceof String)) && (!selFamily.equals(command))) {
                    ObservableList familyList = this.fontFamilyComboBox.getItems();
                    Object selItem = null;
                    for (Object family : familyList) {
                        if (family.equals(command)) {
                            selItem = family;
                            break;
                        }

                        if ((family.equals(DEFAULT_OS_FONT)) && (command.equals("Dialog"))) {
                            selItem = family;
                            break;
                        }
                    }

                    if (selItem != null) {
                        EventHandler action = this.fontFamilyComboBox.getOnAction();
                        this.fontFamilyComboBox.setOnAction(null);
                        this.fontFamilyComboBox.setValue(selItem);
                        this.fontFamilyComboBox.setOnAction(action);
                    }
                }
            }
        }

        this.fontSizeComboBox.setDisable(!isCommandEnabled(FONT_SIZE_COMMAND));
        command = getCommandValue(FONT_SIZE_COMMAND);
        if (this.resetToolbarState) {
            this.fontSizeComboBox.setValue(this.sizeFontMap.get(SIZE_SMALL));
        } else if ((command instanceof String)) {
            if (!this.fontSizeComboBox.getValue().equals(this.sizeFontMap.get(command))) {
                this.fontSizeComboBox.setValue(this.sizeFontMap.get(command));
            }
        } else if (!this.fontSizeComboBox.getValue().equals(this.sizeFontMap.get(SIZE_SMALL))) {
            this.fontSizeComboBox.setValue(this.sizeFontMap.get(SIZE_SMALL));
        }

        this.boldButton.setDisable(!isCommandEnabled(BOLD_COMMAND));
        this.boldButton.setSelected(getCommandState(BOLD_COMMAND));
        this.italicButton.setDisable(!isCommandEnabled(ITALIC_COMMAND));
        this.italicButton.setSelected(getCommandState(ITALIC_COMMAND));
        this.underlineButton.setDisable(!isCommandEnabled(UNDERLINE_COMMAND));
        this.underlineButton.setSelected(getCommandState(UNDERLINE_COMMAND));
        this.strikethroughButton.setDisable(!isCommandEnabled(STRIKETHROUGH_COMMAND));
        this.strikethroughButton.setSelected(getCommandState(STRIKETHROUGH_COMMAND));

        this.fgColorPicker.setDisable(!isCommandEnabled(FOREGROUND_COLOR_COMMAND));
        Object foreColor = getCommandValue(FOREGROUND_COLOR_COMMAND);
        if (foreColor != null && foreColor instanceof String) {
            Color sel = this.fgColorPicker.getValue();
            Color color = Color.valueOf((String) foreColor);
            if (!color.equals(sel)) {
                this.fgColorPicker.setValue(color);
            }
        }

        this.bgColorPicker.setDisable(!isCommandEnabled(BACKGROUND_COLOR_COMMAND));
        Object backColor = getCommandValue(BACKGROUND_COLOR_COMMAND);
        if (backColor != null && backColor instanceof String) {
            Color sel = this.bgColorPicker.getValue();
            Color color = Color.valueOf((String) backColor);
            if (!color.equals(sel)) {
                this.bgColorPicker.setValue(color);
            }
        }

        this.updateToolbarStatus = false;
    }

    private void applyTextFormatting() {
        if ((getCommandState(BULLETS_COMMAND)) || (getCommandState(NUMBERS_COMMAND))) {
            return;
        }

        if (this.browser.getClientCommittedTextLength() == 0) {
            String formatblock = (String) this.formatStyleMap.get(this.formatComboBox.getValue());
            String fontname = this.fontFamilyComboBox.getValue().toString();

            executeCommand(FORMAT_COMMAND, formatblock);
            executeCommand(FONT_FAMILY_COMMAND, fontname);
        }
    }

    private boolean isCommandEnabled(String command) {
        return this.browser.isCommandEnabled(command);
    }

    private boolean getCommandState(String command) {
        return this.browser.getCommandState(command);
    }

    private String getCommandValue(String command) {
        return this.browser.getCommandValue(command);
    }

    private boolean executeCommand(String command, String value) {
        return this.browser.executeCommand(command, value);
    }

    private void populateToolbars() {
        this.resources = ResourceBundle.getBundle(getClass().getName());

        this.cutButton = addButton(this.bar1, this.resources.getString("cutIcon"), this.resources.getString("cut"), "cut");
        this.copyButton = addButton(this.bar1, this.resources.getString("copyIcon"), this.resources.getString("copy"), "copy");
        this.pasteButton = addButton(this.bar1, this.resources.getString("pasteIcon"), this.resources.getString("paste"), "paste");

        this.bar1.getItems().add(new Separator());

        this.alignmentToggleGroup = new ToggleGroup();
        this.alignLeftButton = addToggleButton(this.bar1, this.alignmentToggleGroup,
                this.resources.getString("alignLeftIcon"), this.resources.getString("alignLeft"), ALIGN_LEFT_COMMAND);
        this.alignCenterButton = addToggleButton(this.bar1, this.alignmentToggleGroup,
                this.resources.getString("alignCenterIcon"), this.resources.getString("alignCenter"), ALIGN_CENTER_COMMAND);
        this.alignRightButton = addToggleButton(this.bar1, this.alignmentToggleGroup,
                this.resources.getString("alignRightIcon"), this.resources.getString("alignRight"), ALIGN_RIGHT_COMMAND);
        this.alignJustifyButton = addToggleButton(this.bar1, this.alignmentToggleGroup,
                this.resources.getString("alignJustifyIcon"), this.resources.getString("alignJustify"), ALIGN_JUSTIFY_COMMAND);

        this.bar1.getItems().add(new Separator());

        this.outdentButton = addButton(this.bar1, this.resources.getString("outdentIcon"), this.resources.getString("outdent"), OUTDENT_COMMAND);
        this.indentButton = addButton(this.bar1, this.resources.getString("indentIcon"), this.resources.getString("indent"), INDENT_COMMAND);

        this.bar1.getItems().add(new Separator());

        ToggleGroup localToggleGroup = new ToggleGroup();
        this.bulletsButton = addToggleButton(this.bar1, localToggleGroup, this.resources.getString("bulletsIcon"), this.resources.getString("bullets"), BULLETS_COMMAND);
        this.numbersButton = addToggleButton(this.bar1, localToggleGroup, this.resources.getString("numbersIcon"), this.resources.getString("numbers"), NUMBERS_COMMAND);

        this.bar1.getItems().add(new Separator());

        this.fgColorPicker = new ColorPicker(DEFAULT_FG_COLOR);
        this.fgColorPicker.setFocusTraversable(false);
        this.bar1.getItems().add(fgColorPicker);
        this.bgColorPicker = new ColorPicker(DEFAULT_BG_COLOR);
        this.bgColorPicker.setFocusTraversable(false);
        this.bar1.getItems().add(bgColorPicker);

        this.bar1.getItems().add(new Separator());
        this.insertImageButton = addButton(this.bar1, this.resources.getString("insertImageIcon"), this.resources.getString("insertImage"));
        this.insertTableButton = addButton(this.bar1, this.resources.getString("insertTableIcon"), this.resources.getString("insertTable"));

        this.formatComboBox = new ComboBox();
        this.formatComboBox.getStyleClass().add("font-menu-button");
        this.formatComboBox.setMinWidth(100.0D);
        this.formatComboBox.setPrefWidth(100.0D);
        this.formatComboBox.setMaxWidth(100.0D);
        this.formatComboBox.setFocusTraversable(false);
        this.bar2.getItems().add(this.formatComboBox);

        this.formatStyleMap = new HashMap();
        this.styleFormatMap = new HashMap();

        createFormatMenuItem(FORMAT_PARAGRAPH, this.resources.getString("paragraph"));
        createFormatMenuItem(FORMAT_HEADING_1, this.resources.getString("heading1"));
        createFormatMenuItem(FORMAT_HEADING_2, this.resources.getString("heading2"));
        createFormatMenuItem(FORMAT_HEADING_3, this.resources.getString("heading3"));
        createFormatMenuItem(FORMAT_HEADING_4, this.resources.getString("heading4"));
        createFormatMenuItem(FORMAT_HEADING_5, this.resources.getString("heading5"));
        createFormatMenuItem(FORMAT_HEADING_6, this.resources.getString("heading6"));
        Platform.runLater(new Runnable() {
            public void run() {
                WKToolBar.this.formatComboBox.setValue(resources.getString("paragraph"));
            }
        });
        this.formatComboBox.setTooltip(new Tooltip(this.resources.getString("format")));

        this.fontFamilyComboBox = new ComboBox();
        this.fontFamilyComboBox.getStyleClass().add("font-menu-button");
        this.fontFamilyComboBox.setMinWidth(150.0D);
        this.fontFamilyComboBox.setPrefWidth(150.0D);
        this.fontFamilyComboBox.setMaxWidth(150.0D);
        this.fontFamilyComboBox.setMaxHeight(21.0D);
        this.fontFamilyComboBox.setPrefHeight(21.0D);
        this.fontFamilyComboBox.setMinHeight(21.0D);
        this.fontFamilyComboBox.setFocusTraversable(false);
        this.fontFamilyComboBox.setTooltip(new Tooltip(this.resources.getString("fontFamily")));
        this.bar2.getItems().add(this.fontFamilyComboBox);

        Platform.runLater(new Runnable() {
            public void run() {
                ObservableList fontList = FXCollections.observableArrayList(Font.getFamilies());
                for (Object str : fontList) {
                    if (DEFAULT_OS_FONT.equals(str.toString())) {
                        fontFamilyComboBox.setValue(str);
                    }
                    fontFamilyComboBox.setItems(fontList);
                }
            }
        });

        this.fontSizeComboBox = new ComboBox();
        this.fontSizeComboBox.getStyleClass().add("font-menu-button");
        this.fontSizeComboBox.setMinWidth(80.0D);
        this.fontSizeComboBox.setPrefWidth(80.0D);
        this.fontSizeComboBox.setMaxWidth(80.0D);
        this.fontSizeComboBox.setFocusTraversable(false);
        this.bar2.getItems().add(this.fontSizeComboBox);

        this.fontSizeMap = new HashMap();
        this.sizeFontMap = new HashMap();

        createFontSizeMenuItem(SIZE_XX_SMALL, this.resources.getString("extraExtraSmall"));
        createFontSizeMenuItem(SIZE_X_SMALL, this.resources.getString("extraSmall"));
        createFontSizeMenuItem(SIZE_SMALL, this.resources.getString("small"));
        createFontSizeMenuItem(SIZE_MEDIUM, this.resources.getString("medium"));
        createFontSizeMenuItem(SIZE_LARGE, this.resources.getString("large"));
        createFontSizeMenuItem(SIZE_X_LARGE, this.resources.getString("extraLarge"));
        createFontSizeMenuItem(SIZE_XX_LARGE, this.resources.getString("extraExtraLarge"));
        this.fontSizeComboBox.setTooltip(new Tooltip(this.resources.getString("fontSize")));
        Platform.runLater(new Runnable() {
            public void run() {
                fontSizeComboBox.setValue(resources.getString("small"));
            }
        });

        this.bar2.getItems().add(new Separator());

        this.boldButton = addToggleButton(bar2, null, this.resources.getString("boldIcon"), this.resources.getString("bold"), BOLD_COMMAND);
        this.italicButton = addToggleButton(bar2, null, this.resources.getString("italicIcon"), this.resources.getString("italic"), ITALIC_COMMAND);
        this.underlineButton = addToggleButton(bar2, null, this.resources.getString("underlineIcon"), this.resources.getString("underline"), UNDERLINE_COMMAND);
        this.strikethroughButton = addToggleButton(bar2, null, this.resources.getString("strikethroughIcon"), this.resources.getString("strikethrough"), STRIKETHROUGH_COMMAND);

        this.bar2.getItems().add(new Separator());

        this.insertHorizontalRuleButton = addButton(bar2, this.resources.getString("insertHorizontalRuleIcon"), this.resources.getString("insertHorizontalRule"), INSERT_HORIZONTAL_RULE_COMMAND);
    }

    private void enableToolbar(final boolean enable) {
        Platform.runLater(new Runnable() {
            public void run() {
                if (enable) {
                    copyButton.setDisable(!isCommandEnabled("copy"));
                    cutButton.setDisable(!isCommandEnabled("cut"));
                    pasteButton.setDisable(!isCommandEnabled("paste"));
                } else {
                    copyButton.setDisable(true);
                    cutButton.setDisable(true);
                    pasteButton.setDisable(true);
                }

                insertHorizontalRuleButton.setDisable(!enable);
                alignLeftButton.setDisable(!enable);
                alignCenterButton.setDisable(!enable);
                alignRightButton.setDisable(!enable);
                alignJustifyButton.setDisable(!enable);
                bulletsButton.setDisable(!enable);
                numbersButton.setDisable(!enable);
                indentButton.setDisable(!enable);
                outdentButton.setDisable(!enable);
                formatComboBox.setDisable(!enable);
                fontFamilyComboBox.setDisable(!enable);
                fontSizeComboBox.setDisable(!enable);
                boldButton.setDisable(!enable);
                italicButton.setDisable(!enable);
                underlineButton.setDisable(!enable);
                strikethroughButton.setDisable(!enable);
                fgColorPicker.setDisable(!enable);
                bgColorPicker.setDisable(!enable);
                insertImageButton.setDisable(!enable);
                insertTableButton.setDisable(!enable);
            }
        });
    }

    private Button addButton(ToolBar paramToolBar, final String resourceName, String tooltip) {
        Button localButton = new Button();
        localButton.setFocusTraversable(false);
        paramToolBar.getItems().add(localButton);

        Image localImage = (Image) AccessController.doPrivileged(new PrivilegedAction() {
            public Image run() {
                return new Image(WKToolBar.class.getResource(resourceName).toString());
            }
        });
        localButton.setGraphic(new ImageView(localImage));
        localButton.setTooltip(new Tooltip(tooltip));

        return localButton;
    }

    private Button addButton(ToolBar paramToolBar, final String resourceName, String tooltip, final String command) {
        Button localButton = this.addButton(paramToolBar, resourceName, tooltip);
        localButton.setOnAction(new EventHandler() {
            @Override
            public void handle(Event t) {
                executeCommand(command, null);
                updateToolbarState(false);
            }
        });
        return localButton;
    }

    private ToggleButton addToggleButton(ToolBar paramToolBar, ToggleGroup toggleGroup,
            final String resourceName, String tooltip, final String command) {
        ToggleButton toggleBotton = new ToggleButton();
        toggleBotton.setUserData(command);
        toggleBotton.setFocusTraversable(false);
        paramToolBar.getItems().add(toggleBotton);
        if (toggleGroup != null) {
            toggleBotton.setToggleGroup(toggleGroup);
        }

        Image icon = (Image) AccessController.doPrivileged(new PrivilegedAction() {
            public Image run() {
                return new Image(WKToolBar.class.getResource(resourceName).toString());
            }
        });
        toggleBotton.setGraphic(new ImageView(icon));
        toggleBotton.setTooltip(new Tooltip(tooltip));

        toggleBotton.selectedProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue ov, Object t, Object t1) {
                if (t1 instanceof Boolean) {
                    if (getCommandState(command) != ((Boolean) t1).booleanValue()) {
                        executeCommand(command, null);
                    }
                }
            }
        });
        return toggleBotton;
    }

    private void createFormatMenuItem(String value, String name) {
        this.formatComboBox.getItems().add(name);
        this.formatStyleMap.put(name, value);
        this.styleFormatMap.put(value, name);
    }

    private void createFontSizeMenuItem(String value, String name) {
        this.fontSizeComboBox.getItems().add(name);
        this.fontSizeMap.put(name, value);
        this.sizeFontMap.put(value, name);
    }

    public void keyboardShortcuts(String key) {
        if (BOLD_COMMAND.equals(key)) {
            this.boldButton.setSelected(!this.boldButton.isSelected());
        } else if (ITALIC_COMMAND.equals(key)) {
            this.italicButton.setSelected(!this.italicButton.isSelected());
        } else if (UNDERLINE_COMMAND.equals(key)) {
            this.underlineButton.setSelected(!this.underlineButton.isSelected());
        }
    }

    private static String[] getOSMappings() {
        String str = System.getProperty("os.name");
        for (int i = 0; i < DEFAULT_OS_MAPPINGS.length; i++) {
            if (str.equals(DEFAULT_OS_MAPPINGS[i][0])) {
                return DEFAULT_OS_MAPPINGS[i];
            }
        }

        return DEFAULT_WINDOWS_7_MAPPINGS;
    }

    private static String rgbToHex(Color color) {
        String s = color.toString();
        if (s.toString() != null && s.toString().length() >= 8) {
            s = "#" + s.substring(2, 8);
        }
        return s;
    }
}
