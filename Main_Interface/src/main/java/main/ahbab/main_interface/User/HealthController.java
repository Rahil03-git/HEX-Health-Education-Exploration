package main.ahbab.main_interface.User;

import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class HealthController {

    @FXML
    private TableView<Map<String, String>> BedsTable;

    @FXML
    private Button SearchButton;

    @FXML
    private TextField searchedArea;

    @FXML
    void SearchICUBeds(ActionEvent event) {
        String url = "jdbc:mysql://localhost:3306/hex_database";
        String user = "root";
        String password = "root";

        BedsTable.getItems().clear(); // Clear existing data
        BedsTable.getColumns().clear(); // Clear existing columns

        try (Connection connection = DriverManager.getConnection(url, user, password)) {
            String searchString = searchedArea.getText();
            String query = "SELECT beds.Bed_Available, beds.HospitalID, hospital.HospitalName, hospital.Location, beds.ICU_Available\n" +
                    "FROM beds JOIN hospital ON beds.HospitalID = hospital.HospitalID\n" +
                    "WHERE hospital.Location LIKE ?;\n";

            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setString(1, "%" + searchString + "%");

                ResultSet resultSet = statement.executeQuery();

                // Add columns to the table
                for (int i = 0; i < resultSet.getMetaData().getColumnCount(); i++) {
                    final int columnIndex = i;
                    TableColumn<Map<String, String>, String> column = new TableColumn<>(resultSet.getMetaData().getColumnName(i + 1));
                    column.setCellValueFactory(cellData ->
                            new SimpleStringProperty(cellData.getValue().get(column.getText())));
                    BedsTable.getColumns().add(column);
                }

                // Populate the TableView with data
                while (resultSet.next()) {
                    Map<String, String> rowData = new HashMap<>();
                    for (int i = 1; i <= resultSet.getMetaData().getColumnCount(); i++) {
                        String columnName = resultSet.getMetaData().getColumnName(i);
                        String value = resultSet.getString(i);
                        rowData.put(columnName, value);
                    }
                    BedsTable.getItems().add(rowData);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private TableView<Map<String, String>> TestsTable;
    @FXML
    private TextField TestSearchfield;

    @FXML
    private Button searchingtestbutton;

    @FXML
    void SearchTest(ActionEvent event) {
        String url = "jdbc:mysql://localhost:3306/hex_database";
        String user = "root";
        String password = "root";

        TestsTable.getItems().clear(); // Clear existing data
        TestsTable.getColumns().clear(); // Clear existing columns

        try (Connection connection = DriverManager.getConnection(url, user, password)) {
            String searchString = TestSearchfield.getText();
            String query = "SELECT hospital.HospitalName, hospital.Location, healthtest.TestFees\n" +
                    "FROM healthtest JOIN hospital ON healthtest.HospitalID = hospital.HospitalID\n" +
                    "WHERE healthtest.TestName LIKE ?;\n";

            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setString(1, "%" + searchString + "%");

                ResultSet resultSet = statement.executeQuery();

                // Add columns to the table
                for (int i = 0; i < resultSet.getMetaData().getColumnCount(); i++) {
                    final int columnIndex = i;
                    TableColumn<Map<String, String>, String> column = new TableColumn<>(resultSet.getMetaData().getColumnName(i + 1));
                    column.setCellValueFactory(cellData ->
                            new SimpleStringProperty(cellData.getValue().get(column.getText())));
                    TestsTable.getColumns().add(column);
                }

                // Populate the TableView with data
                while (resultSet.next()) {
                    Map<String, String> rowData = new HashMap<>();
                    for (int i = 1; i <= resultSet.getMetaData().getColumnCount(); i++) {
                        String columnName = resultSet.getMetaData().getColumnName(i);
                        String value = resultSet.getString(i);
                        rowData.put(columnName, value);
                    }
                    TestsTable.getItems().add(rowData);
                }
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }



    @FXML
    private TextField BloodLocation;

    @FXML
    private Button bloodbankPharmacyButton;

    @FXML
    private TableView<Map<String, String>> BloodPharmacyField;

    @FXML
    void SearchBloodbankandPharmacy(ActionEvent event) {
        String url = "jdbc:mysql://localhost:3306/hex_database";
        String user = "root";
        String password = "root";

        BloodPharmacyField.getItems().clear(); // Clear existing data
        BloodPharmacyField.getColumns().clear(); // Clear existing columns

        try (Connection connection = DriverManager.getConnection(url, user, password)) {
            String searchString = BloodLocation.getText();
            String query = "SELECT * FROM hospital WHERE Location LIKE ?";


            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setString(1, "%" + searchString + "%");

                ResultSet resultSet = statement.executeQuery();

                // Add columns to the table
                for (int i = 0; i < resultSet.getMetaData().getColumnCount(); i++) {
                    final int columnIndex = i;
                    TableColumn<Map<String, String>, String> column = new TableColumn<>(resultSet.getMetaData().getColumnName(i + 1));
                    column.setCellValueFactory(cellData ->
                            new SimpleStringProperty(cellData.getValue().get(column.getText())));
                    BloodPharmacyField.getColumns().add(column);
                }

                // Populate the TableView with data
                while (resultSet.next()) {
                    Map<String, String> rowData = new HashMap<>();
                    for (int i = 1; i <= resultSet.getMetaData().getColumnCount(); i++) {
                        String columnName = resultSet.getMetaData().getColumnName(i);
                        String value = resultSet.getString(i);
                        rowData.put(columnName, value);
                    }
                    BloodPharmacyField.getItems().add(rowData);
                }

            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void handleBack(ActionEvent event) {
        javafx.stage.Stage stage = (javafx.stage.Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
        if (stage.getOwner() != null) {
            // This was opened as a modal dialog; just close to reveal the previous window
            stage.close();
        } else {
            // Scene replacement case: navigate back to GuestScreen in the same Stage
            try {
                javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(getClass().getResource("/main/ahbab/main_interface/User/GuestScreen.fxml"));
                javafx.scene.Parent root = loader.load();
                stage.setScene(new javafx.scene.Scene(root));
                stage.show();
            } catch (java.io.IOException e) {
                e.printStackTrace();
            }
        }
    }
}
