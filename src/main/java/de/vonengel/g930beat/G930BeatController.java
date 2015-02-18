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

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class G930BeatController {
    private static final Logger LOG = LoggerFactory.getLogger(G930BeatController.class);

    @FXML
    private ResourceBundle resources;
    @FXML
    private URL location;

    @FXML
    private Button removeBeatButton;
    @FXML
    private Button addBeatButton;
    @FXML
    private ListView<String> leftList;
    @FXML
    private ListView<String> rightList;

    private G930Beat g930Beat;
    private Heartbeat heartbeat;
    private ExecutorService executor;

    @FXML
    void initialize() {
        LOG.debug("location url is: {}", location);
        assert removeBeatButton != null : "fx:id=\"removeBeatButton\" was not injected: check your FXML file 'g930beat.fxml'.";
        assert addBeatButton != null : "fx:id=\"addBeatButton\" was not injected: check your FXML file 'g930beat.fxml'.";
        assert leftList != null : "fx:id=\"leftList\" was not injected: check your FXML file 'g930beat.fxml'.";
        assert rightList != null : "fx:id=\"rightList\" was not injected: check your FXML file 'g930beat.fxml'.";
        leftList.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        rightList.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    }

    private void populateLeftListWithAvailableMixers() {
        ObservableList<String> availableMixers = FXCollections.observableArrayList(heartbeat.getAvailableMixers());
        leftList.setItems(availableMixers);
    }

    @FXML
    void addSelectedBeat(ActionEvent event) {
        List<String> selectedItems = new ArrayList<String>(leftList.getSelectionModel().getSelectedItems());
        if (!selectedItems.isEmpty()) {
            selectedItems.forEach(mixerName -> {
                leftList.getItems().remove(mixerName);
                Task<Void> task = new Task<Void>() {

                    @Override
                    protected Void call() throws Exception {
                        try {
                            heartbeat.start(mixerName);
                        } catch (Exception e) {
                            LOG.error("Could not start mixer '{}'", mixerName, e);
                        }
                        return null;
                    }
                };
                task.setOnSucceeded(event1 -> rightList.getItems().add(mixerName));
                executor.submit(task);
            });
        }
    }

    @FXML
    void removeSelectedBeat(ActionEvent event) {
        List<String> selectedItems = new ArrayList<String>(rightList.getSelectionModel().getSelectedItems());
        if (!selectedItems.isEmpty()) {
            selectedItems.forEach(mixerName -> {
                rightList.getItems().remove(mixerName);
                Task<Void> task = new Task<Void>() {

                    @Override
                    protected Void call() throws Exception {
                        heartbeat.cancelMixer(mixerName);
                        return null;
                    }
                };
                task.setOnSucceeded(event1 -> leftList.getItems().add(mixerName));
                executor.submit(task);
            });
        }
    }

    @FXML
    void exitApplication(ActionEvent event) {
        System.exit(0);
    }

    @FXML
    void openPreferences(ActionEvent event) {
        g930Beat.openPreferences();
    }

    public void setG930Beat(G930Beat g930Beat) {
        this.g930Beat = g930Beat;
    }

    public void setExecutor(ExecutorService executor) {
        this.executor = executor;
    }

    public void setHeartbeat(Heartbeat heartbeat) {
        this.heartbeat = heartbeat;
        populateLeftListWithAvailableMixers();
    }

    @FXML
    public void openAboutDialog(ActionEvent event) {
        g930Beat.openAboutDialog();
    }
}
