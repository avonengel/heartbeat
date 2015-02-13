package de.vonengel.g930beat;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class PreferencesController {

    @FXML
    private Button cancelButton;

    @FXML
    private TextField delayTextField;

    @FXML
    private Button okButton;
    
    private Stage dialogStage;

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
            // TODO apply fields
        } else {
            // TODO notify user
        }
    }

    private boolean validateFields() {
        // TODO implement
        return true;
    }

    @FXML
    void handleCancelClicked() {
        dialogStage.close();
    }
}
