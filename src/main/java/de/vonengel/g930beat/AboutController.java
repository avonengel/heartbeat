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
    private static final String CONTENT_FILE = "about.txt";

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
        URL resource = AboutController.class.getResource("/" + CONTENT_FILE);
        assert resource != null : "Could not load " + CONTENT_FILE;
        try {
            String text = Resources.toString(resource, Charsets.UTF_8);
            aboutTextArea.setText(text);
        } catch (IOException e) {
            LOG.error("Could not load " + CONTENT_FILE, e);
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
