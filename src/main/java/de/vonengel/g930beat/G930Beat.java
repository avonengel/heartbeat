/*
 * Copyright (c) 2015 Axel von Engel
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
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

        stage.setTitle("Heartbeat");
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