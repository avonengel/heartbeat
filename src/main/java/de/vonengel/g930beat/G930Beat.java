package de.vonengel.g930beat;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class G930Beat extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("g930beat.fxml"));

        Scene scene = new Scene(root);

        stage.setTitle("G930 Beat");
        stage.setScene(scene);
        stage.show();
    }
}