package de.vonengel.g930beat;

import java.io.IOException;
import java.util.concurrent.Executors;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class G930Beat extends Application {
    private static final Logger LOG = LoggerFactory.getLogger(G930Beat.class);

    private Stage primaryStage;

    private HeartbeatPreferences preferences;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws IOException {
        primaryStage = stage;
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("g930beat.fxml"));
        Parent root = loader.load();
        G930BeatController g930Controller = loader.getController();
        g930Controller.setG930Beat(this);
        g930Controller.setExecutor(Executors.newWorkStealingPool());
        Heartbeat heartbeat = new Heartbeat();
        preferences = new HeartbeatPreferences();
        heartbeat.setPreferences(preferences);
        g930Controller.setHeartbeat(heartbeat);

        Scene scene = new Scene(root);

        stage.setTitle("G930 Beat");
        stage.setScene(scene);
        stage.show();
    }

    public void openPreferences() {
        // Load the fxml file and create a new stage for the popup dialog.
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("preferences.fxml"));
        Pane page;
        try {
            page = (Pane) loader.load();
        } catch (IOException e) {
            LOG.error("Error loading preferences dialog", e);
            return;
        }

        Stage dialogStage = new Stage();
        dialogStage.setTitle("Preferences");
        dialogStage.initModality(Modality.WINDOW_MODAL);
        dialogStage.initOwner(primaryStage);
        Scene scene = new Scene(page);
        dialogStage.setScene(scene);

        PreferencesController controller = loader.getController();
        controller.setDialogStage(dialogStage);
        controller.setPreferences(preferences);

        // Show the dialog and wait until the user closes it
        dialogStage.showAndWait();
    }

    public void openAboutDialog() {
        // Load the fxml file and create a new stage for the popup dialog.
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("about.fxml"));
        Pane page;
        try {
            page = (Pane) loader.load();
        } catch (IOException e) {
            LOG.error("Error loading preferences dialog", e);
            return;
        }

        Stage dialogStage = new Stage();
        dialogStage.setTitle("Preferences");
        dialogStage.initModality(Modality.WINDOW_MODAL);
        dialogStage.initOwner(primaryStage);
        Scene scene = new Scene(page);
        dialogStage.setScene(scene);

        AboutController controller = loader.getController();
        controller.setDialogStage(dialogStage);

        // Show the dialog and wait until the user closes it
        dialogStage.showAndWait();
    }
}