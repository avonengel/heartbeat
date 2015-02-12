package de.vonengel.g930beat;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;

public class G930BeatController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button removeBeatButton;

    @FXML
    private Button addBeatButton;

    @FXML
    private ListView<?> leftList;

    @FXML
    private ListView<?> rightList;

    @FXML
    void initialize() {
        assert removeBeatButton != null : "fx:id=\"removeBeatButton\" was not injected: check your FXML file 'g930beat.fxml'.";
        assert addBeatButton != null : "fx:id=\"addBeatButton\" was not injected: check your FXML file 'g930beat.fxml'.";
        assert leftList != null : "fx:id=\"leftList\" was not injected: check your FXML file 'g930beat.fxml'.";
        assert rightList != null : "fx:id=\"rightList\" was not injected: check your FXML file 'g930beat.fxml'.";

    }

    @FXML
    void addSelectedBeat(ActionEvent event) {

    }

    @FXML
    void removeSelectedBeat(ActionEvent event) {

    }
}
