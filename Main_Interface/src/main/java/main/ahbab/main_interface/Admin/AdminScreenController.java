package main.ahbab.main_interface.Admin;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import main.ahbab.main_interface.SimpleServer;

import java.io.IOException;
import java.sql.*;

public class AdminScreenController {

    @FXML
    private Button EducationServices;
    @FXML
    private Button Adduserinfo;
    @FXML
    private Button HealthServices;
    @FXML
    private Button OtherServices;
    @FXML
    private Button logout;

    @FXML
    private Stage stage;
    @FXML
    private Scene scene;
    @FXML
    private TextField adminname;

    @FXML
    private PasswordField adminpassword;

    private static final String DB_URL = "jdbc:mysql://localhost:3306/hex_database";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "root";

    @FXML
    public void gotoGuestscreen(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/main/ahbab/main_interface/User/GuestScreen.fxml"));
        Parent root = loader.load();
        Scene newScene = new Scene(root);
        Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        currentStage.setScene(newScene);
        currentStage.show();
    }
    @FXML
    public void AdminLogin(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/main/ahbab/main_interface/Admin/AdminLogin.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(loader.load());
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    public void WelcomeScreen(ActionEvent event) {
        String adminName = adminname.getText();
        String adminPassword = adminpassword.getText();

        String department = getAdminDepartment(adminName, adminPassword);
        if (department != null) {
            try {
                switch (department) {
                    case "HEALTH" -> loadScene("/main/ahbab/main_interface/Admin/Health.fxml");
                    case "OTHERS" -> loadScene("/main/ahbab/main_interface/Admin/Others.fxml");
                    case "EDUCATION" -> loadScene("/main/ahbab/main_interface/Admin/Education.fxml");
                    default -> System.out.println("Unknown department for admin: " + department);
                }
                SimpleServer.loggedadminname = adminname.getText();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Invalid credentials!");
        }
    }

    private String getAdminDepartment(String adminName, String adminPassword) {
        String sql = "SELECT department FROM users WHERE username = ? AND password = ? AND role = 'ADMIN'";
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, adminName);
            ps.setString(2, adminPassword);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("department");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Helper method to load a new scene
    private void loadScene(String resourcePath) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(resourcePath));
            Stage stage = (Stage) adminname.getScene().getWindow();
            Scene scene = new Scene(loader.load());
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
