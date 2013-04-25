/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package spanner;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

/**
 *
 * @author lim16
 */
public class Spanner extends Application {

    @Override
    public void start(Stage primaryStage) {
        BorderPane root = new BorderPane();
        Scene scene = new Scene(root, 1024, 800);

        primaryStage.setTitle("Spanner");
        WKEditor editor = new WKEditor(primaryStage);
        MainToolBar toolbar = new MainToolBar(primaryStage, editor);
        
        root.setTop(toolbar);
        root.setCenter(editor);

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * The main() method is ignored in correctly deployed JavaFX application.
     * main() serves only as fallback in case the application can not be
     * launched through deployment artifacts, e.g., in IDEs with limited FX
     * support. NetBeans ignores main().
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
}
