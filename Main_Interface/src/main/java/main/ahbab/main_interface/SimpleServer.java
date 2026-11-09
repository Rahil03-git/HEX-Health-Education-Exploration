package main.ahbab.main_interface;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class SimpleServer {
    private static final int PORT = 12345;
    private static final String DB_URL = "jdbc:mysql://localhost:3306/hex_database";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "root";

    private static ServerSocket serverSocket;

    public static void main(String[] args) {
        try {
            try {
                serverSocket = new ServerSocket(PORT);
            } catch (BindException e) {
                System.err.println("Port " + PORT + " is already in use. Server may already be running.");
                return;
            }

            System.out.println("Server is running on port " + PORT + " and waiting for connections...");

            while (!serverSocket.isClosed()) {
                Socket clientSocket = serverSocket.accept();
                new Thread(() -> handleClient(clientSocket)).start();
            }
        } catch (IOException e) {
            System.err.println("Server error: " + e.getMessage());
        } finally {
            closeServer();
        }
    }

    public static void closeServer() {
        try {
            if (serverSocket != null && !serverSocket.isClosed()) {
                serverSocket.close();
                System.out.println("Server socket closed.");
            }
        } catch (IOException e) {
            System.err.println("Error closing server socket: " + e.getMessage());
        }
    }

    private static void handleClient(Socket clientSocket) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
             PrintWriter writer = new PrintWriter(clientSocket.getOutputStream(), true)) {

            String message;
            while ((message = reader.readLine()) != null) {
                System.out.println("Received: " + message);
                writer.println("Message received and stored in the database");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (clientSocket != null && !clientSocket.isClosed()) {
                    clientSocket.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static String loggedInUsername;
    public static String loggedadminname;

    public static void storeMessageInDatabase(String receiverUsername, String message, String senderUsername) {
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            Integer senderId = getUserIdByUsername(connection, senderUsername);
            Integer receiverId = getUserIdByUsername(connection, receiverUsername);
            if (senderId == null || receiverId == null) {
                System.err.println("Unable to resolve user IDs for message storage.");
                return;
            }
            String sql = "INSERT INTO messages (sender_id, receiver_id, message) VALUES (?, ?, ?)";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setInt(1, senderId);
                statement.setInt(2, receiverId);
                statement.setString(3, message);
                statement.executeUpdate();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static Integer getUserIdByUsername(Connection conn, String username) {
        try (PreparedStatement stmt = conn.prepareStatement("SELECT id FROM users WHERE username = ?")) {
            stmt.setString(1, username);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("id");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}