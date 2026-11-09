package main.ahbab.main_interface.service;

import main.ahbab.main_interface.server.ServerManager;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import java.util.logging.Level;

/**
 * Service layer for User operations
 */
public class UserService {
    private static final Logger LOGGER = Logger.getLogger(UserService.class.getName());
    private ServerManager serverManager;
    
    public UserService() {
        this.serverManager = ServerManager.getInstance();
    }
    
    /**
     * Get all users for admin management
     */
    public List<String> getAllUsers() {
        List<String> users = new ArrayList<>();
        try {
            String sql = "SELECT username FROM users WHERE role = 'USER'";
            Connection conn = serverManager.getConnection();
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                ResultSet rs = stmt.executeQuery();
                while (rs.next()) {
                    users.add(rs.getString("username"));
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error fetching users", e);
        }
        return users;
    }
    
    /**
     * Create new user
     */
    public boolean createUser(String username, String password, String email, String role) {
        try {
            String sql = "INSERT INTO users (username, password, email, role) VALUES (?, ?, ?, ?)";
            Connection conn = serverManager.getConnection();
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, username);
                stmt.setString(2, password); // In production, hash this
                stmt.setString(3, email);
                stmt.setString(4, role);
                return stmt.executeUpdate() > 0;
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error creating user", e);
        }
        return false;
    }
    
    /**
     * Update user information
     */
    public boolean updateUser(String username, String email) {
        try {
            String sql = "UPDATE users SET email = ? WHERE username = ?";
            Connection conn = serverManager.getConnection();
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, email);
                stmt.setString(2, username);
                return stmt.executeUpdate() > 0;
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error updating user", e);
        }
        return false;
    }
    
    /**
     * Delete user
     */
    public boolean deleteUser(String username) {
        try {
            String sql = "DELETE FROM users WHERE username = ?";
            Connection conn = serverManager.getConnection();
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, username);
                return stmt.executeUpdate() > 0;
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error deleting user", e);
        }
        return false;
    }
    
    /**
     * Check if user exists
     */
    public boolean userExists(String username) {
        try {
            String sql = "SELECT COUNT(*) FROM users WHERE username = ?";
            Connection conn = serverManager.getConnection();
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, username);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error checking user existence", e);
        }
        return false;
    }
    
    /**
     * Get user role
     */
    public String getUserRole(String username) {
        try {
            String sql = "SELECT role FROM users WHERE username = ?";
            Connection conn = serverManager.getConnection();
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, username);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    return rs.getString("role");
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error getting user role", e);
        }
        return null;
    }
}