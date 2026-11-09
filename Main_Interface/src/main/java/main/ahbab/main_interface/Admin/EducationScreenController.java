package main.ahbab.main_interface.Admin;

import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import main.ahbab.main_interface.SimpleServer;

import java.io.IOException;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class EducationScreenController {
    @FXML
    private TableView<Map<String, String>> Bookfindertable;

    @FXML
    private TableColumn<Map<String, String>, String> BookName;

    @FXML
    private TableColumn<String, String> Library;
    @FXML
    private TableColumn<String, String> Availablity;
    @FXML
    private TableColumn<String, String> Price;

    @FXML
    private TableView<Map<String, String>> tutortable;

    @FXML
    private TextField availablity;
    @FXML
    private TextField bookname;
    @FXML
    private TextField libraryname;
    @FXML
    private TextField price;
    @FXML
    private TextField tutorname;

    @FXML
    private Button Libraryloader;


    @FXML
    void loadtutors(ActionEvent event) {
        String url = "jdbc:mysql://localhost:3306/hex_database";
        String user = "root";
        String password = "root";

        tutortable.getItems().clear(); // Clear existing data
        tutortable.getColumns().clear(); // Clear existing columns

        try (Connection connection = DriverManager.getConnection(url, user, password)) {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM `hiretutors` WHERE 1");

            // Add columns to the table
            for (int i = 0; i < resultSet.getMetaData().getColumnCount(); i++) {
                final int columnIndex = i;
                TableColumn<Map<String, String>, String> column = new TableColumn<>(resultSet.getMetaData().getColumnName(i + 1));
                column.setCellValueFactory(cellData ->
                        new SimpleStringProperty(cellData.getValue().get(column.getText())));
                tutortable.getColumns().add(column);
            }

            // Populate the TableView with data
            while (resultSet.next()) {
                Map<String, String> rowData = new HashMap<>();
                for (int i = 1; i <= resultSet.getMetaData().getColumnCount(); i++) {
                    String columnName = resultSet.getMetaData().getColumnName(i);
                    String value = resultSet.getString(i);
                    rowData.put(columnName, value);
                }
                tutortable.getItems().add(rowData);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    @FXML
    void loadbookfindertable(ActionEvent event) {
        String url = "jdbc:mysql://localhost:3306/hex_database";
        String user = "root";
        String password = "root";

        Bookfindertable.getItems().clear(); // Clear existing data
        Bookfindertable.getColumns().clear(); // Clear existing columns

        try (Connection connection = DriverManager.getConnection(url, user, password)) {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM `book_keeper` WHERE 1");

            // Add columns to the table
            for (int i = 0; i < resultSet.getMetaData().getColumnCount(); i++) {
                final int columnIndex = i;
                TableColumn<Map<String, String>, String> column = new TableColumn<>(resultSet.getMetaData().getColumnName(i + 1));
                column.setCellValueFactory(cellData ->
                        new SimpleStringProperty(cellData.getValue().get(column.getText())));
                Bookfindertable.getColumns().add(column);
            }

            // Populate the TableView with data
            while (resultSet.next()) {
                Map<String, String> rowData = new HashMap<>();
                for (int i = 1; i <= resultSet.getMetaData().getColumnCount(); i++) {
                    String columnName = resultSet.getMetaData().getColumnName(i);
                    String value = resultSet.getString(i);
                    rowData.put(columnName, value);
                }
                Bookfindertable.getItems().add(rowData);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    @FXML
    void Insertbooks(ActionEvent event) {
        String url = "jdbc:mysql://localhost:3306/hex_database";
        String user = "root";
        String password = "root";

        try (Connection connection = DriverManager.getConnection(url, user, password)) {
            String sql = "INSERT INTO book_keeper (Book, Library, Availability, Price) VALUES (?, ?, ?, ?)";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, bookname.getText());
                statement.setString(2, libraryname.getText());
                statement.setString(3, availablity.getText());
                statement.setString(4, price.getText());

                statement.executeUpdate();
                showSuccessAlert("Book inserted successfully!");
            }
        } catch (SQLException e) {
            showErrorAlert("Error inserting book: " + e.getMessage());
        }
    }

    @FXML
    void UpdateBooks(ActionEvent event) {
        String url = "jdbc:mysql://localhost:3306/hex_database";
        String user = "root";
        String password = "root";

        try (Connection connection = DriverManager.getConnection(url, user, password)) {
            String sql = "UPDATE book_keeper SET Price = ?, Availability = ?, Library=? WHERE Book = ?";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, price.getText());
                statement.setString(2, availablity.getText());
                statement.setString(3, libraryname.getText());
                statement.setString(4, bookname.getText());

                statement.executeUpdate();
                showSuccessAlert("Book updated successfully!");
            }
        } catch (SQLException e) {
            showErrorAlert("Error updating book: " + e.getMessage());
        }
    }

    @FXML
    void deletebooks(ActionEvent event) {
        String url = "jdbc:mysql://localhost:3306/hex_database";
        String user = "root";
        String password = "root";

        try (Connection connection = DriverManager.getConnection(url, user, password)) {
            String sql = "DELETE FROM book_keeper WHERE Book = ?";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, bookname.getText());

                statement.executeUpdate();
                showSuccessAlert("Book deleted successfully!");
            }
        } catch (SQLException e) {
            showErrorAlert("Error deleting book: " + e.getMessage());
        }
    }

    @FXML
    void verifyteacher(ActionEvent event) {
        String url = "jdbc:mysql://localhost:3306/hex_database";
        String user = "root";
        String password = "root";

        try (Connection connection = DriverManager.getConnection(url, user, password)) {
            String sql = "UPDATE hiretutors SET Adminverified = true WHERE Name = ?";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, tutorname.getText());

                statement.executeUpdate();
                showSuccessAlert("Teacher verified successfully!");
            }
        } catch (SQLException e) {
            showErrorAlert("Error verifying teacher: " + e.getMessage());
        }
    }

    @FXML
    void deleteteacher(ActionEvent event) {
        String url = "jdbc:mysql://localhost:3306/hex_database";
        String user = "root";
        String password = "root";

        try (Connection connection = DriverManager.getConnection(url, user, password)) {
            String sql = "DELETE FROM hiretutors WHERE Name = ?";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, tutorname.getText());

                statement.executeUpdate();
                showSuccessAlert("Teacher deleted successfully!");
            }
        } catch (SQLException e) {
            showErrorAlert("Error deleting teacher: " + e.getMessage());
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

    private void showSuccessAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Success");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
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

    private void showErrorAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
