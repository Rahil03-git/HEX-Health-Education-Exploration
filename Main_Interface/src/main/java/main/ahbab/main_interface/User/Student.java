package main.ahbab.main_interface.User;

import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.sql.*;

public class Student {

    @FXML
    private TableView<Object[]> tutorsTableView;

    @FXML
    private TableColumn<Object[], String> nameColumn;

    @FXML
    private TableColumn<Object[], String> subjectColumn;

    @FXML
    private TableColumn<Object[], String> classColumn;

    @FXML
    private TableColumn<Object[], String> salaryColumn;

    @FXML
    private Text Text1;

    @FXML
    private Text text2;
    @FXML
    private Button SeeTutors;

    @FXML
    void initialize(ActionEvent event) {
        // Connect to the database and populate the tutorsTableView
        loadTutorData();
    }

    private void loadTutorData() {
        // JDBC URL, username, and password of your database
        String url = "jdbc:mysql://localhost:3306/hex_database";
        String user = "root";
        String password = "root";

        tutorsTableView.getItems().clear(); // Clear existing data

        try (Connection connection = DriverManager.getConnection(url, user, password)) {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT `Name`, `Subjects`, `Class`, `Expected Salary` FROM `hiretutors` where Adminverified = 1");

            // Add columns dynamically based on ResultSet metadata
            tutorsTableView.getColumns().clear();
            for (int i = 0; i < resultSet.getMetaData().getColumnCount(); i++) {
                final int columnIndex = i;
                TableColumn<Object[], String> column = new TableColumn<>(resultSet.getMetaData().getColumnName(i + 1));
                column.setCellValueFactory(cellData ->
                        new SimpleStringProperty(cellData.getValue()[columnIndex].toString()));
                tutorsTableView.getColumns().add(column);
            }

            // Populate the TableView with data
            while (resultSet.next()) {
                Object[] rowData = new Object[resultSet.getMetaData().getColumnCount()];
                for (int i = 0; i < rowData.length; i++) {
                    rowData[i] = resultSet.getObject(i + 1);
                }
                tutorsTableView.getItems().add(rowData);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void handleBack(ActionEvent event) {
        try {
            javafx.stage.Stage stage = (javafx.stage.Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(getClass().getResource("/main/ahbab/main_interface/User/Tutor.fxml"));
            javafx.scene.Parent root = loader.load();
            stage.setScene(new javafx.scene.Scene(root));
            stage.show();
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
    }
}