package de.vonengel.g930beat;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
    private Heartbeat heartbeat = new Heartbeat();

    @FXML
    void initialize() {
        LOG.debug("location url is: {}", location);
        assert removeBeatButton != null : "fx:id=\"removeBeatButton\" was not injected: check your FXML file 'g930beat.fxml'.";
        assert addBeatButton != null : "fx:id=\"addBeatButton\" was not injected: check your FXML file 'g930beat.fxml'.";
        assert leftList != null : "fx:id=\"leftList\" was not injected: check your FXML file 'g930beat.fxml'.";
        assert rightList != null : "fx:id=\"rightList\" was not injected: check your FXML file 'g930beat.fxml'.";
        leftList.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        rightList.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        populateLeftListWithAvailableMixers();
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
                rightList.getItems().add(mixerName);
                try {
                    heartbeat.start(mixerName);
                } catch (Exception e) {
                    LOG.error("Could not start mixer '{}'", mixerName, e);
                }
            });
        }
    }

    @FXML
    void removeSelectedBeat(ActionEvent event) {
        List<String> selectedItems = new ArrayList<String>(rightList.getSelectionModel().getSelectedItems());
        if (!selectedItems.isEmpty()) {
            selectedItems.forEach(mixerName -> {
                leftList.getItems().add(mixerName);
                rightList.getItems().remove(mixerName);
                heartbeat.cancelMixer(mixerName);
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
}
