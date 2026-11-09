package main.ahbab.main_interface.server;

import java.io.*;
import java.net.*;
import java.sql.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Logger;
import java.util.logging.Level;

/**
 * Enhanced Server Manager for H.E.X Application
 */
public class ServerManager {
    private static final Logger LOGGER = Logger.getLogger(ServerManager.class.getName());

    private static final int DEFAULT_PORT = 12345;
    private static final String DB_HOST_URL = "jdbc:mysql://localhost:3306/";
    private static final String DB_NAME = "hex_database";
    private static final String DB_URL = DB_HOST_URL + DB_NAME;
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "root";

    private ServerSocket serverSocket;
    private ExecutorService threadPool;
    private ConcurrentHashMap<String, ClientHandler> connectedClients;
    private boolean isRunning = false;

    private Connection dbConnection;

    private static ServerManager instance;

    private ServerManager() {
        threadPool = Executors.newCachedThreadPool();
        connectedClients = new ConcurrentHashMap<>();
        initializeDatabase();
    }

    public static synchronized ServerManager getInstance() {
        if (instance == null) {
            instance = new ServerManager();
        }
        return instance;
    }

    /**
     * Initialize database connection and create tables if needed
     */
    private void initializeDatabase() {
        try {
            // Try connecting to target DB first
            dbConnection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
        } catch (SQLException first) {
            LOGGER.log(Level.INFO, "Database '{0}' not found, attempting to create it...", DB_NAME);
            // Connect to host without DB and create it
            try (Connection rootConn = DriverManager.getConnection(DB_HOST_URL, DB_USER, DB_PASSWORD);
                 Statement st = rootConn.createStatement()) {
                st.execute("CREATE DATABASE IF NOT EXISTS " + DB_NAME + " CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci");
                dbConnection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            } catch (SQLException createEx) {
                LOGGER.log(Level.SEVERE, "Failed to create database " + DB_NAME, createEx);
            }
        }

        if (dbConnection != null) {
            try {
                createTables();
                LOGGER.info("Database initialized successfully");
            } catch (SQLException e) {
                LOGGER.log(Level.SEVERE, "Failed to initialize database tables", e);
            }
        }
    }

    /**
     * Create necessary database tables (aligned with unified schema)
     */
    private void createTables() throws SQLException {
        String[] tables = {
            // Users (admins and regular users)
            """
            CREATE TABLE IF NOT EXISTS users (
                id INT AUTO_INCREMENT PRIMARY KEY,
                username VARCHAR(100) UNIQUE NOT NULL,
                password VARCHAR(255) NOT NULL,
                email VARCHAR(100),
                location TEXT,
                role ENUM('USER','ADMIN') DEFAULT 'USER',
                department ENUM('HEALTH','EDUCATION','OTHERS') NULL,
                created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                last_login TIMESTAMP NULL
            )
            """,
            // Messages between users
            """
            CREATE TABLE IF NOT EXISTS messages (
                id INT AUTO_INCREMENT PRIMARY KEY,
                sender_id INT NOT NULL,
                receiver_id INT NOT NULL,
                message TEXT NOT NULL,
                timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                is_read BOOLEAN DEFAULT FALSE,
                FOREIGN KEY (sender_id) REFERENCES users(id) ON DELETE CASCADE,
                FOREIGN KEY (receiver_id) REFERENCES users(id) ON DELETE CASCADE
            )
            """,
            // Optional: generic health records storage
            """
            CREATE TABLE IF NOT EXISTS health_records (
                id INT AUTO_INCREMENT PRIMARY KEY,
                user_id INT,
                record_type VARCHAR(50),
                data JSON,
                created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                FOREIGN KEY (user_id) REFERENCES users(id)
            )
            """,
            // Health domain tables used by UI
            """
            CREATE TABLE IF NOT EXISTS hospital (
                HospitalID INT NOT NULL PRIMARY KEY,
                HospitalName TEXT NOT NULL,
                Location MEDIUMTEXT NOT NULL,
                HasBlood MEDIUMTEXT NOT NULL,
                HasPharmacy TINYTEXT NOT NULL,
                ICUAvailability TEXT NOT NULL
            )
            """,
            """
            CREATE TABLE IF NOT EXISTS beds (
                HospitalID INT NOT NULL PRIMARY KEY,
                ICU_Available INT NOT NULL,
                Bed_Available INT NOT NULL,
                FOREIGN KEY (HospitalID) REFERENCES hospital(HospitalID) ON DELETE CASCADE
            )
            """,
            """
            CREATE TABLE IF NOT EXISTS healthtest (
                TestName VARCHAR(50) NOT NULL,
                HospitalID INT NOT NULL,
                Availability VARCHAR(50) NOT NULL DEFAULT 'Available',
                TestFees DOUBLE NOT NULL,
                INDEX idx_healthtest (TestName, HospitalID),
                FOREIGN KEY (HospitalID) REFERENCES hospital(HospitalID) ON DELETE CASCADE
            )
            """,
            // Parking (singular table name to match UI queries)
            """
            CREATE TABLE IF NOT EXISTS parking_slot (
                Slot INT NOT NULL PRIMARY KEY,
                Location MEDIUMTEXT NOT NULL,
                Area MEDIUMTEXT NOT NULL
            )
            """,
            // Education domain tables used by UI
            """
            CREATE TABLE IF NOT EXISTS book_keeper (
                Book MEDIUMTEXT,
                Library MEDIUMTEXT,
                Availability TEXT,
                Price INT NOT NULL
            )
            """,
            """
            CREATE TABLE IF NOT EXISTS hiretutors (
                Name MEDIUMTEXT,
                Subjects MEDIUMTEXT,
                Class VARCHAR(10),
                `Expected Salary` DECIMAL(20,0),
                Email TEXT NOT NULL,
                Adminverified TINYINT(1) NOT NULL DEFAULT 0
            )
            """
        };
        
        try (Statement stmt = dbConnection.createStatement()) {
            for (String sql : tables) {
                stmt.execute(sql);
            }
        }
    }
    
    /**
     * Start the server
     */
    public void startServer() {
        startServer(DEFAULT_PORT);
    }
    
    public void startServer(int port) {
        try {
            serverSocket = new ServerSocket(port);
            isRunning = true;
            LOGGER.info("H.E.X Server started on port " + port);
            
            // Accept client connections
            while (isRunning) {
                Socket clientSocket = serverSocket.accept();
                ClientHandler clientHandler = new ClientHandler(clientSocket, this);
                threadPool.execute(clientHandler);
            }
        } catch (IOException e) {
            if (isRunning) {
                LOGGER.log(Level.SEVERE, "Server error", e);
            }
        }
    }
    
    /**
     * Stop the server
     */
    public void stopServer() {
        isRunning = false;
        try {
            if (serverSocket != null && !serverSocket.isClosed()) {
                serverSocket.close();
            }
            threadPool.shutdown();
            if (dbConnection != null && !dbConnection.isClosed()) {
                dbConnection.close();
            }
            LOGGER.info("Server stopped successfully");
        } catch (IOException | SQLException e) {
            LOGGER.log(Level.SEVERE, "Error stopping server", e);
        }
    }
    
    /**
     * Register a client
     */
    public void registerClient(String username, ClientHandler handler) {
        connectedClients.put(username, handler);
        LOGGER.info("Client registered: " + username);
    }
    
    /**
     * Unregister a client
     */
    public void unregisterClient(String username) {
        connectedClients.remove(username);
        LOGGER.info("Client unregistered: " + username);
    }
    
    /**
     * Get database connection
     */
    public Connection getConnection() {
        return dbConnection;
    }
    
    /**
     * Authenticate user
     */
    public boolean authenticateUser(String username, String password) {
        try {
            String sql = "SELECT COUNT(*) FROM users WHERE username = ? AND password = ?";
            try (PreparedStatement stmt = dbConnection.prepareStatement(sql)) {
                stmt.setString(1, username);
                stmt.setString(2, password); // In production, use hashed passwords
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Authentication error", e);
        }
        return false;
    }
    
    /**
     * Send message between users
     */
    public boolean sendMessage(String sender, String receiver, String message) {
        try {
            String sql = "INSERT INTO messages (sender_id, receiver_id, message) VALUES ((SELECT id FROM users WHERE username = ?), (SELECT id FROM users WHERE username = ?), ?)";
            try (PreparedStatement stmt = dbConnection.prepareStatement(sql)) {
                stmt.setString(1, sender);
                stmt.setString(2, receiver);
                stmt.setString(3, message);
                stmt.executeUpdate();
                
                // Notify receiver if online
                ClientHandler receiverHandler = connectedClients.get(receiver);
                if (receiverHandler != null) {
                    receiverHandler.sendMessage("NEW_MESSAGE:" + sender + ":" + message);
                }
                return true;
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error sending message", e);
        }
        return false;
    }
    
    /**
     * Get user messages
     */
    public String getMessages(String username, String chatWith) {
        try {
            String sql = """
                SELECT u1.username as sender, m.message, m.timestamp
                FROM messages m
                JOIN users u1 ON m.sender_id = u1.id
                JOIN users u2 ON m.receiver_id = u2.id
                WHERE (u1.username = ? AND u2.username = ?) OR (u1.username = ? AND u2.username = ?)
                ORDER BY m.timestamp
                """;
            
            try (PreparedStatement stmt = dbConnection.prepareStatement(sql)) {
                stmt.setString(1, username);
                stmt.setString(2, chatWith);
                stmt.setString(3, chatWith);
                stmt.setString(4, username);
                
                ResultSet rs = stmt.executeQuery();
                StringBuilder messages = new StringBuilder();
                
                while (rs.next()) {
                    messages.append(rs.getString("sender"))
                           .append(": ")
                           .append(rs.getString("message"))
                           .append(" (")
                           .append(rs.getString("timestamp"))
                           .append(")\n");
                }
                
                return messages.toString();
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error retrieving messages", e);
        }
        return "";
    }
}