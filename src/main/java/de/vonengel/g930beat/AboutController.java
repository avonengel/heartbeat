package de.vonengel.g930beat;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;

public class AboutController {

    private static final Logger LOG = LoggerFactory.getLogger(AboutController.class);

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TextArea aboutTextArea;

    private Stage dialogStage;

    @FXML
    void initialize() {
        assert aboutTextArea != null : "fx:id=\"aboutTextArea\" was not injected: check your FXML file 'about.fxml'.";
        initTextArea();
    }

    private void initTextArea() {
        URL resource = AboutController.class.getResource("/README.md");
        assert resource != null : "Could not load README.md";
        try {
            String text = Resources.toString(resource, Charsets.UTF_8);
            aboutTextArea.setText(text);
        } catch (IOException e) {
            LOG.error("Could not load README.md", e);
        }
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    @FXML
    void closeDialog() {
        dialogStage.close();
    }
}
