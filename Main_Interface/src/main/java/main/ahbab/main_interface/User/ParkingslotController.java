package main.ahbab.main_interface.User;

import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.sql.*;

public class ParkingslotController {

    @FXML
    private TableView<Object[]> ParkingTable;

    @FXML
    private ImageView Car;

    @FXML
    private Button Reloadpark;

    @FXML
    private void loadParkingData() {
        // JDBC URL, username, and password of your database
        String url = "jdbc:mysql://localhost:3306/hex_database";
        String user = "root";
        String password = "root";

        ParkingTable.getItems().clear(); // Clear existing data

        try (Connection connection = DriverManager.getConnection(url, user, password)) {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM `parking_slot` WHERE 1");

            // Add columns dynamically based on ResultSet metadata
            ParkingTable.getColumns().clear();
            for (int i = 0; i < resultSet.getMetaData().getColumnCount(); i++) {
                final int columnIndex = i;
                TableColumn<Object[], String> column = new TableColumn<>(resultSet.getMetaData().getColumnName(i + 1));
                column.setCellValueFactory(cellData ->
                        new SimpleStringProperty(cellData.getValue()[columnIndex].toString()));
                ParkingTable.getColumns().add(column);
            }

            // Populate the TableView with data
            while (resultSet.next()) {
                Object[] rowData = new Object[resultSet.getMetaData().getColumnCount()];
                for (int i = 0; i < rowData.length; i++) {
                    rowData[i] = resultSet.getObject(i + 1);
                }
                ParkingTable.getItems().add(rowData);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void handleBack(ActionEvent event) {
        javafx.stage.Stage stage = (javafx.stage.Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
        if (stage.getOwner() != null) {
            stage.close();
        } else {
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
