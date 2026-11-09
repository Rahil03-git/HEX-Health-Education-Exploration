package main.ahbab.main_interface.service;

import main.ahbab.main_interface.server.ServerManager;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import java.util.logging.Level;

/**
 * Service layer for Health-related operations
 */
public class HealthService {
    private static final Logger LOGGER = Logger.getLogger(HealthService.class.getName());
    private ServerManager serverManager;
    
    public HealthService() {
        this.serverManager = ServerManager.getInstance();
    }
    
    /**
     * Save health record for user
     */
    public boolean saveHealthRecord(String username, String recordType, String data) {
        try {
            String sql = "INSERT INTO health_records (user_id, record_type, data) VALUES ((SELECT id FROM users WHERE username = ?), ?, ?)";
            Connection conn = serverManager.getConnection();
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, username);
                stmt.setString(2, recordType);
                stmt.setString(3, data);
                return stmt.executeUpdate() > 0;
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error saving health record", e);
        }
        return false;
    }
    
    /**
     * Get health records for user
     */
    public List<Map<String, String>> getHealthRecords(String username) {
        List<Map<String, String>> records = new ArrayList<>();
        try {
            String sql = "SELECT record_type, data, created_at FROM health_records WHERE user_id = (SELECT id FROM users WHERE username = ?) ORDER BY created_at DESC";
            Connection conn = serverManager.getConnection();
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, username);
                ResultSet rs = stmt.executeQuery();
                while (rs.next()) {
                    Map<String, String> record = new HashMap<>();
                    record.put("type", rs.getString("record_type"));
                    record.put("data", rs.getString("data"));
                    record.put("date", rs.getString("created_at"));
                    records.add(record);
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error fetching health records", e);
        }
        return records;
    }
    
    /**
     * Get available blood bank information
     */
    public List<Map<String, String>> getBloodBankInfo() {
        List<Map<String, String>> bloodBanks = new ArrayList<>();
        
        // Mock data - in real application, this would come from database
        Map<String, String> bank1 = new HashMap<>();
        bank1.put("name", "City Blood Bank");
        bank1.put("address", "123 Health Street");
        bank1.put("phone", "555-0123");
        bank1.put("available", "A+: 10, B+: 5, O+: 15, AB+: 3");
        bloodBanks.add(bank1);
        
        Map<String, String> bank2 = new HashMap<>();
        bank2.put("name", "General Hospital Blood Bank");  
        bank2.put("address", "456 Medical Avenue");
        bank2.put("phone", "555-0456");
        bank2.put("available", "A+: 8, B+: 12, O+: 20, AB+: 7");
        bloodBanks.add(bank2);
        
        return bloodBanks;
    }
    
    /**
     * Get available health tests
     */
    public List<Map<String, String>> getAvailableHealthTests() {
        List<Map<String, String>> tests = new ArrayList<>();
        
        // Mock data
        Map<String, String> test1 = new HashMap<>();
        test1.put("name", "Complete Blood Count");
        test1.put("description", "Comprehensive blood analysis");
        test1.put("cost", "$50");
        test1.put("duration", "30 minutes");
        tests.add(test1);
        
        Map<String, String> test2 = new HashMap<>();
        test2.put("name", "Lipid Panel");
        test2.put("description", "Cholesterol and triglyceride levels");
        test2.put("cost", "$40");
        test2.put("duration", "15 minutes");
        tests.add(test2);
        
        Map<String, String> test3 = new HashMap<>();
        test3.put("name", "Diabetes Screening");
        test3.put("description", "Blood glucose and HbA1c test");
        test3.put("cost", "$35");
        test3.put("duration", "20 minutes");
        tests.add(test3);
        
        return tests;
    }
    
    /**
     * Get ICU and bed availability
     */
    public Map<String, Object> getICUAndBedAvailability() {
        Map<String, Object> availability = new HashMap<>();
        
        // Mock data
        availability.put("icu_beds_total", 50);
        availability.put("icu_beds_occupied", 32);
        availability.put("icu_beds_available", 18);
        availability.put("general_beds_total", 200);
        availability.put("general_beds_occupied", 145);
        availability.put("general_beds_available", 55);
        availability.put("last_updated", "2025-10-03 14:30:00");
        
        return availability;
    }
    
    /**
     * Book health test appointment
     */
    public boolean bookHealthTest(String username, String testName, String preferredDate, String preferredTime) {
        try {
            String data = String.format("{\"test\": \"%s\", \"date\": \"%s\", \"time\": \"%s\", \"status\": \"booked\"}", 
                                      testName, preferredDate, preferredTime);
            return saveHealthRecord(username, "TEST_BOOKING", data);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error booking health test", e);
        }
        return false;
    }
}