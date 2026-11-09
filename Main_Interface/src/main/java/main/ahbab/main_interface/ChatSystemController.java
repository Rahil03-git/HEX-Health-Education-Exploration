package main.ahbab.main_interface;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class ChatSystemController {
        @FXML
        private Label nameLabel;

        @FXML
        private Button sendButton;

        @FXML
        private TextArea messageArea;

        @FXML
        private TextArea inputTextArea;

        @FXML
        private VBox userListVBox;

        private static final String SERVER_HOST = "localhost";
        private static final int SERVER_PORT = 12345;
        private static final String DB_URL = "jdbc:mysql://localhost:3306/hex_database";
        private static final String DB_USER = "root";
        private static final String DB_PASSWORD = "root";
        private String selectedAdminName;
        private Socket socket;
        private PrintWriter writer;
        private BufferedReader reader;

        @FXML
        void initialize() {
                try {
                        // Connect to the server
                        socket = new Socket(SERVER_HOST, SERVER_PORT);
                        writer = new PrintWriter(socket.getOutputStream(), true);
                        reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                        loadAdminNames();

                        userListVBox.getChildren().forEach(node -> {
                                if (node instanceof Label) {
                                        Label adminLabel = (Label) node;
                                        adminLabel.setOnMouseClicked(e -> handleAdminClick(adminLabel.getText()));
                                }
                        });
                } catch (Exception e) {
                        e.printStackTrace();
                }
        }
        @FXML
        private void handleAdminClick(String adminName) {
                Platform.runLater(() -> {
                        // Set the selected admin's name
                        selectedAdminName = adminName;
                        // You can update UI or perform other actions based on the selected admin if needed

                });
                loadMessagesFromDatabase(selectedAdminName);}
        private void loadMessagesFromDatabase(String adminName) {
                // Execute this task in a background thread to avoid blocking the JavaFX Application Thread
                new Thread(() -> {
                        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
                                String sql = "SELECT u1.username as sender, m.message, m.timestamp \n" +
                                        "FROM messages m \n" +
                                        "JOIN users u1 ON m.sender_id = u1.id \n" +
                                        "JOIN users u2 ON m.receiver_id = u2.id \n" +
                                        "WHERE (u1.username = ? AND u2.username = ?) OR (u1.username = ? AND u2.username = ?) \n" +
                                        "ORDER BY m.timestamp";
                                try (PreparedStatement statement = connection.prepareStatement(sql)) {
                                        statement.setString(1, adminName);
                                        statement.setString(2, SimpleServer.loggedInUsername);
                                        statement.setString(3, SimpleServer.loggedInUsername);
                                        statement.setString(4, adminName);

                                        try (ResultSet resultSet = statement.executeQuery()) {
                                                // Clear existing messages
                                                Platform.runLater(() -> messageArea.clear());

                                                while (resultSet.next()) {
                                                        String sender = resultSet.getString("sender");
                                                        String messageText = resultSet.getString("message");
                                                        Platform.runLater(() -> messageArea.appendText(sender + ": " + messageText + "\n"));
                                                }
                                        }
                                }
                        } catch (Exception e) {
                                e.printStackTrace();
                        }
                }).start();
        }
        @FXML
        private void loadAdminNames() {
                try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
                        String sql = "SELECT username AS AdminName FROM users WHERE role = 'ADMIN'";
                        try (PreparedStatement statement = connection.prepareStatement(sql)) {
                                try (ResultSet resultSet = statement.executeQuery()) {
                                        while (resultSet.next()) {
                                                String adminName = resultSet.getString("AdminName");
                                                addAdminToUserList(adminName);
                                        }
                                }
                        }
                } catch (Exception e) {
                        e.printStackTrace();
                }
        }
@FXML
        private void addAdminToUserList(String adminName) {
                Label adminLabel = new Label(adminName);
                userListVBox.getChildren().add(adminLabel);
        }
@FXML
        void sendMessage(ActionEvent event) {
                String message = inputTextArea.getText();
                if (!message.isEmpty() && selectedAdminName != null) {
                        // Send message to the server
                        writer.println(message);
                        String username = SimpleServer.loggedInUsername;
                        nameLabel.setText(username);
                        // Store the message in the database
                        SimpleServer.storeMessageInDatabase(selectedAdminName, message, username);
                        // Clear the input field
                        inputTextArea.clear();
                        loadMessagesFromDatabase(selectedAdminName);
                }
        }

        // You may want to call this method when the application is closing
        private void closeConnection() {
                try {
                        if (socket != null && !socket.isClosed()) {
                                socket.close();
                        }
                } catch (Exception e) {
                        e.printStackTrace();
                }
        }
        @FXML
        public void handleBack(ActionEvent event) {
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.close();
        }
}
