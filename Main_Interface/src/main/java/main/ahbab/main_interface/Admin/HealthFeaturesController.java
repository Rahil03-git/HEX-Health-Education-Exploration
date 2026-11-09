package main.ahbab.main_interface.Admin;

import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import main.ahbab.main_interface.SimpleServer;

import java.io.IOException;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class HealthFeaturesController {
    @FXML
    private Button healthButton;

    @FXML
    private Button bloodPharmaButton;

    @FXML
    private Button backButton;

    @FXML
    private ImageView pic1;

    @FXML
    private TableView<Map<String, String>> healthFeaturesTable;

    // Other FXML elements for input fields, buttons, etc.

    // Initialize database connection parameters
    private static final String DB_URL = "jdbc:mysql://localhost:3306/hex_database";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "root";


    @FXML
    public void gotoadminlogin(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/main/ahbab/main_interface/Admin/AdminLogin.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(loader.load());
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    private void loadDataFromDatabase() {
        healthFeaturesTable.getItems().clear(); // Clear existing data
        healthFeaturesTable.getColumns().clear(); // Clear existing columns

        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM `hospital`");

            // Add columns to the table
            for (int i = 0; i < resultSet.getMetaData().getColumnCount(); i++) {
                final int columnIndex = i;
                TableColumn<Map<String, String>, String> column = new TableColumn<>(resultSet.getMetaData().getColumnName(i + 1));
                column.setCellValueFactory(cellData ->
                        new SimpleStringProperty(cellData.getValue().get(column.getText())));
                healthFeaturesTable.getColumns().add(column);
            }

            // Populate the TableView with data
            while (resultSet.next()) {
                Map<String, String> rowData = new HashMap<>();
                for (int i = 1; i <= resultSet.getMetaData().getColumnCount(); i++) {
                    String columnName = resultSet.getMetaData().getColumnName(i);
                    String value = resultSet.getString(i);
                    rowData.put(columnName, value);
                }
                healthFeaturesTable.getItems().add(rowData);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void loadDataFromDatabase2() {
        healthFeaturesTable.getItems().clear(); // Clear existing data
        healthFeaturesTable.getColumns().clear(); // Clear existing columns

        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT beds.ICU_Available, beds.Bed_Available, hospital.HospitalName, hospital.ICUAvailability\n" +
                    "FROM beds JOIN hospital ON beds.HospitalID = hospital.HospitalID;\n");

            // Add columns to the table
            for (int i = 0; i < resultSet.getMetaData().getColumnCount(); i++) {
                final int columnIndex = i;
                TableColumn<Map<String, String>, String> column = new TableColumn<>(resultSet.getMetaData().getColumnName(i + 1));
                column.setCellValueFactory(cellData ->
                        new SimpleStringProperty(cellData.getValue().get(column.getText())));
                healthFeaturesTable.getColumns().add(column);
            }

            // Populate the TableView with data
            while (resultSet.next()) {
                Map<String, String> rowData = new HashMap<>();
                for (int i = 1; i <= resultSet.getMetaData().getColumnCount(); i++) {
                    String columnName = resultSet.getMetaData().getColumnName(i);
                    String value = resultSet.getString(i);
                    rowData.put(columnName, value);
                }
                healthFeaturesTable.getItems().add(rowData);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    @FXML
    void loadDataFromDatabase1(ActionEvent event) {
        healthFeaturesTable.getItems().clear(); // Clear existing data
        healthFeaturesTable.getColumns().clear(); // Clear existing columns

        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM `healthtest`");

            // Add columns to the table
            for (int i = 0; i < resultSet.getMetaData().getColumnCount(); i++) {
                final int columnIndex = i;
                TableColumn<Map<String, String>, String> column = new TableColumn<>(resultSet.getMetaData().getColumnName(i + 1));
                column.setCellValueFactory(cellData ->
                        new SimpleStringProperty(cellData.getValue().get(column.getText())));
                healthFeaturesTable.getColumns().add(column);
            }

            // Populate the TableView with data
            while (resultSet.next()) {
                Map<String, String> rowData = new HashMap<>();
                for (int i = 1; i <= resultSet.getMetaData().getColumnCount(); i++) {
                    String columnName = resultSet.getMetaData().getColumnName(i);
                    String value = resultSet.getString(i);
                    rowData.put(columnName, value);
                }
                healthFeaturesTable.getItems().add(rowData);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private TextField UpdateICUavailability;
    @FXML
    private TextField Hospitalname;
    @FXML
    private TextField Updatebeds;
    @FXML
    private Button UpdateICUavail;

    @FXML
    private Button Updatebed;


    @FXML
    void updateICUAvailability(ActionEvent event) {
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String sql = "UPDATE hospital SET ICUAvailability=? WHERE HospitalName = ?";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, UpdateICUavailability.getText()); // Corrected index
                statement.setString(2, Hospitalname.getText());

                statement.executeUpdate();
                showSuccessAlert("ICU Status updated successfully!");
            }
        } catch (SQLException e) {
            showErrorAlert("Error updating ICU Availability: " + e.getMessage());
        }
    }


    @FXML
    void updatebeds(ActionEvent event) {
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String sql = "UPDATE beds JOIN hospital ON beds.HospitalID = hospital.HospitalID SET beds.Bed_Available = ? WHERE hospital.HospitalName = ?";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, Updatebeds.getText());
                statement.setString(2, Hospitalname.getText());

                statement.executeUpdate();
                showSuccessAlert("Bed Number updated successfully!");
            }
        } catch (SQLException e) {
            showErrorAlert("Error updating beds: " + e.getMessage());
        }
    }

    @FXML
    private void openchatUI() throws IOException {
        new Thread(() -> {
            SimpleServer.main(null);
        }).start();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/main/ahbab/main_interface/UserChat2.fxml"));
        Parent root = loader.load();
        Stage stage = new Stage();
        stage.setTitle("Developer Screen");
        stage.setScene(new Scene(root));
        stage.show();

    }
    private void showSuccessAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Success");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showErrorAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
