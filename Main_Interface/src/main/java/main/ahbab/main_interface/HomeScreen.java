package main.ahbab.main_interface;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.InputStream;

public class HomeScreen extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HomeScreen.class.getResource("HomeScreen.fxml"));
        Scene scene = new Scene(fxmlLoader.load());

        try (InputStream iconStream = getClass().getResourceAsStream("H.E.X demo.png")) {
            if (iconStream != null) {
                Image appIcon = new Image(iconStream);
                stage.getIcons().add(appIcon);
            }
        }
        stage.setTitle("Health Education and Co.");
        stage.setIconified(false);
        stage.setScene(scene);
        stage.setWidth(1200);
        stage.setHeight(800);
        stage.centerOnScreen();
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}