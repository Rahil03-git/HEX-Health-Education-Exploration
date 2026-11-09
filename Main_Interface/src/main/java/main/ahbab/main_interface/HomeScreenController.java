package main.ahbab.main_interface;

import javafx.animation.FadeTransition;
import javafx.animation.PauseTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import main.ahbab.main_interface.User.GuestScreenController;
import main.ahbab.main_interface.server.ServerManager;

import java.io.IOException;

public class HomeScreenController {
    @FXML
    private Button userLoginBtn;
    @FXML
    private Button adminLoginBtn;
    @FXML
    private Button guestBtn;
    @FXML
    private ImageView logo;
    @FXML
    private Text WelcomeText1, WelcomeText2;
    
    // Server instance
    private ServerManager serverManager;

    @FXML
    public void initialize() {
        // Start the server when the application starts
        serverManager = ServerManager.getInstance();
        new Thread(() -> serverManager.startServer()).start();
    }

    @FXML
    public void onUserLogin(ActionEvent event) {
        navigateToScreen("UserLogin.fxml", event);
    }

    @FXML
    public void onAdminLogin(ActionEvent event) {
        navigateToScreen("Admin/AdminLogin.fxml", event);
    }

    @FXML
    public void onGuestAccess(ActionEvent event) {
        navigateToScreen("User/GuestScreen.fxml", event);
    }

    private void navigateToScreen(String fxmlPath, ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Scene scene = new Scene(loader.load());
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Legacy method for backward compatibility
    public void onContinue(ActionEvent event) {
        onGuestAccess(event);
    }
}
