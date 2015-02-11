package de.vonengel.g930beat;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class G930Beat extends Application {

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage stage) {
		// create root node of scene, i.e. group
		Group rootGroup = new Group();

		Scene scene = new Scene(rootGroup, 200, 200);
		stage.setScene(scene);
		stage.setTitle("G930 Beat");
		stage.centerOnScreen();
		stage.show();

		stage.show();
	}
}