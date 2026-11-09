package main.ahbab.main_interface.server;

import java.io.*;
import java.net.Socket;
import java.util.logging.Logger;
import java.util.logging.Level;

/**
 * Handles individual client connections and communication
 */
public class ClientHandler implements Runnable {
    private static final Logger LOGGER = Logger.getLogger(ClientHandler.class.getName());
    
    private Socket clientSocket;
    private ServerManager serverManager;
    private BufferedReader reader;
    private PrintWriter writer;
    private String username;
    private boolean isConnected = true;
    
    public ClientHandler(Socket clientSocket, ServerManager serverManager) {
        this.clientSocket = clientSocket;
        this.serverManager = serverManager;
        
        try {
            reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            writer = new PrintWriter(clientSocket.getOutputStream(), true);
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Error initializing client handler", e);
        }
    }
    
    @Override
    public void run() {
        try {
            handleClient();
        } catch (IOException e) {
            LOGGER.log(Level.WARNING, "Client connection error", e);
        } finally {
            cleanup();
        }
    }
    
    private void handleClient() throws IOException {
        String message;
        while (isConnected && (message = reader.readLine()) != null) {
            processMessage(message);
        }
    }
    
    private void processMessage(String message) {
        String[] parts = message.split(":", 3);
        if (parts.length < 2) {
            sendMessage("ERROR:Invalid message format");
            return;
        }
        
        String command = parts[0];
        
        switch (command) {
            case "LOGIN":
                handleLogin(parts);
                break;
            case "REGISTER":
                handleRegister(parts);
                break;
            case "SEND_MESSAGE":
                handleSendMessage(parts);
                break;
            case "GET_MESSAGES":
                handleGetMessages(parts);
                break;
            case "GET_USERS":
                handleGetUsers();
                break;
            case "HEALTH_DATA":
                handleHealthData(parts);
                break;
            case "EDUCATION_CONTENT":
                handleEducationContent(parts);
                break;
            case "PARKING_STATUS":
                handleParkingStatus();
                break;
            case "RESERVE_PARKING":
                handleParkingReservation(parts);
                break;
            case "DISCONNECT":
                isConnected = false;
                break;
            default:
                sendMessage("ERROR:Unknown command: " + command);
        }
    }
    
    private void handleLogin(String[] parts) {
        if (parts.length < 3) {
            sendMessage("ERROR:Invalid login format");
            return;
        }
        
        String username = parts[1];
        String password = parts[2];
        
        if (serverManager.authenticateUser(username, password)) {
            this.username = username;
            serverManager.registerClient(username, this);
            sendMessage("SUCCESS:Login successful");
            LOGGER.info("User logged in: " + username);
        } else {
            sendMessage("ERROR:Invalid credentials");
        }
    }
    
    private void handleRegister(String[] parts) {
        if (parts.length < 3) {
            sendMessage("ERROR:Invalid registration format");
            return;
        }
        
        // Implementation for user registration
        sendMessage("SUCCESS:Registration successful");
    }
    
    private void handleSendMessage(String[] parts) {
        if (parts.length < 3 || username == null) {
            sendMessage("ERROR:Invalid message format or not logged in");
            return;
        }
        
        String receiver = parts[1];
        String messageContent = parts[2];
        
        if (serverManager.sendMessage(username, receiver, messageContent)) {
            sendMessage("SUCCESS:Message sent");
        } else {
            sendMessage("ERROR:Failed to send message");
        }
    }
    
    private void handleGetMessages(String[] parts) {
        if (parts.length < 2 || username == null) {
            sendMessage("ERROR:Invalid format or not logged in");
            return;
        }
        
        String chatWith = parts[1];
        String messages = serverManager.getMessages(username, chatWith);
        sendMessage("MESSAGES:" + messages);
    }
    
    private void handleGetUsers() {
        if (username == null) {
            sendMessage("ERROR:Not logged in");
            return;
        }
        
        // Implementation to get list of users
        sendMessage("USERS:user1,user2,user3"); // Placeholder
    }
    
    private void handleHealthData(String[] parts) {
        if (username == null) {
            sendMessage("ERROR:Not logged in");
            return;
        }
        
        // Implementation for health data handling
        sendMessage("SUCCESS:Health data processed");
    }
    
    private void handleEducationContent(String[] parts) {
        if (username == null) {
            sendMessage("ERROR:Not logged in");
            return;
        }
        
        // Implementation for education content
        sendMessage("SUCCESS:Education content processed");
    }
    
    private void handleParkingStatus() {
        if (username == null) {
            sendMessage("ERROR:Not logged in");
            return;
        }
        
        // Implementation for parking status
        sendMessage("PARKING_STATUS:Available slots: 5");
    }
    
    private void handleParkingReservation(String[] parts) {
        if (parts.length < 2 || username == null) {
            sendMessage("ERROR:Invalid format or not logged in");
            return;
        }
        
        String slotNumber = parts[1];
        // Implementation for parking reservation
        sendMessage("SUCCESS:Parking slot " + slotNumber + " reserved");
    }
    
    public void sendMessage(String message) {
        if (writer != null) {
            writer.println(message);
        }
    }
    
    private void cleanup() {
        try {
            if (username != null) {
                serverManager.unregisterClient(username);
            }
            if (reader != null) reader.close();
            if (writer != null) writer.close();
            if (clientSocket != null) clientSocket.close();
        } catch (IOException e) {
            LOGGER.log(Level.WARNING, "Error during cleanup", e);
        }
    }
    
    public String getUsername() {
        return username;
    }
}