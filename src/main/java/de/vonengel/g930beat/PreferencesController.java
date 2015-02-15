package de.vonengel.g930beat;

import java.io.File;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PreferencesController {

    private static final Logger LOG = LoggerFactory.getLogger(PreferencesController.class);
    @FXML
    private TextField delayTextField;
    @FXML
    TextField fileTextField;

    @FXML
    private Button cancelButton;
    @FXML
    private Button okButton;

    private Stage dialogStage;

    private HeartbeatPreferences preferences;

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    @FXML
    void initialize() {
        delayTextField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (!newValue.matches("\\d*")) {
                    delayTextField.setText(oldValue);
                }
            }
        });
    }

    @FXML
    void handleOkClicked() {
        if (validateFields()) {
            preferences.setPeriod(getDelayValue());
            preferences.setFile(fileTextField.getText());
            dialogStage.close();
        } else {
            // TODO notify user
        }
    }

    private long getDelayValue() throws NumberFormatException {
        return Long.parseLong(delayTextField.getText());
    }

    private boolean validateFields() {
        try {
            getDelayValue();
        } catch (NumberFormatException e) {
            LOG.error("Could not parse long from value '{}'", delayTextField.getText(), e);
            return false;
        }
        return true;
    }

    @FXML
    void handleCancelClicked() {
        dialogStage.close();
    }

    public void setPreferences(HeartbeatPreferences preferences) {
        this.preferences = preferences;
        setFieldsFromPreferences();
    }

    private void setFieldsFromPreferences() {
        delayTextField.setText(Long.toString(preferences.getPeriod()));
        fileTextField.setText(preferences.getFile());
    }

    @FXML
    public void selectFileButton(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setSelectedExtensionFilter(new FileChooser.ExtensionFilter("WAV Files", "wav"));
        File selectedFile = fileChooser.showOpenDialog(dialogStage);
        if (selectedFile.exists()) {
            fileTextField.setText(selectedFile.getAbsolutePath());
        }
    }

    @FXML
    public void resetFileToDefault(ActionEvent event) {
        preferences.setFile(HeartbeatPreferences.DEFAULT_SOUND);
        fileTextField.setText(preferences.getFile());
    }

    @FXML
    public void resetDelayToDefault(ActionEvent event) {
        preferences.setPeriod(HeartbeatPreferences.DEFAULT_PERIOD);
        delayTextField.setText(Long.toString(preferences.getPeriod()));
    }
}
