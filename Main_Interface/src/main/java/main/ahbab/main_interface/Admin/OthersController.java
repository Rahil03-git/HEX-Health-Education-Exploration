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

public class OthersController {

    @FXML
    private ImageView pic3;

    @FXML
    private Label text1;
    @FXML
    private TextField AreaTextField;

    @FXML
    private TextField LocationTextField;
    @FXML
    private Button DataLoader;

    @FXML
    private Button backid;
    @FXML
    private TextField dataTextField;

    @FXML
    private Button pinger;

    @FXML
    private Button Delete;

    @FXML
    private Button Update;

    @FXML
    private TableView<Object[]> ParkingTable;

    @FXML
    private TableColumn<Object[], String> Slot;

    @FXML
    private TableColumn<Object[], String> Location;

    @FXML
    private TableColumn<Object[], String> Area;

    @FXML
    private ImageView Car;

    @FXML
    private Button Reloadpark;

    @FXML
    private void loadParkingData() {
        String url = "jdbc:mysql://localhost:3306/hex_database";
        String user = "root";
        String password = "root";

        ParkingTable.getItems().clear();

        try (Connection connection = DriverManager.getConnection(url, user, password)) {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM `parking_slot` WHERE 1");

            ParkingTable.getColumns().clear();
            for (int i = 0; i < resultSet.getMetaData().getColumnCount(); i++) {
                final int columnIndex = i;
                TableColumn<Object[], String> column = new TableColumn<>(resultSet.getMetaData().getColumnName(i + 1));
                column.setCellValueFactory(cellData ->
                        new SimpleStringProperty(cellData.getValue()[columnIndex].toString()));
                ParkingTable.getColumns().add(column);
            }

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
    void Delete(ActionEvent event) {
        String dataToDelete = dataTextField.getText();

        if (!dataToDelete.isEmpty()) {
            String url = "jdbc:mysql://localhost:3306/hex_database";
            String user = "root";
            String password = "root";

            try (Connection connection = DriverManager.getConnection(url, user, password)) {
                String deleteQuery = "DELETE FROM parking_slot WHERE Slot = ?";
                try (PreparedStatement preparedStatement = connection.prepareStatement(deleteQuery)) {
                    preparedStatement.setString(1, dataToDelete);
                    int rowsAffected = preparedStatement.executeUpdate();

                    if (rowsAffected > 0) {
                        System.out.println("Data deleted successfully.");
                        loadParkingData();
                    } else {
                        System.out.println("Data not found or deletion failed.");
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Please enter data to delete.");
        }
    }

    @FXML
    void UpdateData(ActionEvent event) {
        String newData = dataTextField.getText();
        String newData1 = AreaTextField.getText();
        String newdata2 = LocationTextField.getText();

        if (!newData.isEmpty()) {
            String url = "jdbc:mysql://localhost:3306/hex_database";
            String user = "root";
            String password = "root";

            try (Connection connection = DriverManager.getConnection(url, user, password)) {
                String updateQuery = "UPDATE parking_slot SET Slot = ?, Area = ? WHERE Location = ?";
                try (PreparedStatement preparedStatement = connection.prepareStatement(updateQuery)) {
                    preparedStatement.setString(1, newData);
                    preparedStatement.setString(2, newData1);
                    preparedStatement.setString(3, newdata2);

                    int rowsAffected = preparedStatement.executeUpdate();

                    if (rowsAffected > 0) {
                        System.out.println("Data updated successfully.");
                        loadParkingData();
                    } else {
                        System.out.println("Data not found or update failed.");
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Please enter new data to update.");
        }
    }


    @FXML
    public void gotoadminlogin(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/main/ahbab/main_interface/Admin/AdminLogin.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(loader.load());
        stage.setScene(scene);
        stage.show();
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
}
