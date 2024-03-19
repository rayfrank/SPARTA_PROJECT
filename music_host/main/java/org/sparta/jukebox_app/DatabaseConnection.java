package org.sparta.jukebox_app;

import java.sql.*;

public class DatabaseConnection {
    // Database connection details (replace with your actual credentials)
    private static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    private static final String DB_URL = "jdbc:mysql://localhost/sparta_player";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    private static Connection connection = null;

    private static void getDBConnection() {
        try {
            // Register JDBC driver
            Class.forName(JDBC_DRIVER);

            // Open a connection
            connection = DriverManager.getConnection(DB_URL, USER, PASSWORD);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    public static Connection getConnection() {
        if (connection == null) {
            getDBConnection();
        }
        return connection;
    }

    public boolean saveUserDetails(String username, String password, String email, String names, String pin, String contact) {
        try {
            Connection conn = getConnection();

            // Generate salt and hash password using Blowfish algorithm
            String salt = PasswordHasher.generateSalt();
            String hashedPassword = PasswordHasher.hashPassword(password, salt);

            String query = "INSERT INTO users (username, password, email, name, pin, salt, contact) VALUES (?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setString(1, username);
            pstmt.setString(2, hashedPassword);
            pstmt.setString(3, email);
            pstmt.setString(4, names);
            pstmt.setString(5, pin);
            pstmt.setString(6, salt);
            pstmt.setString(7, contact);

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Method to authenticate user against the database
    public boolean authenticateUser(String username, String password) {
        try {
            Connection conn = getConnection();
            String query = "SELECT * FROM users WHERE username = ?";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setString(1, username);

            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                String storedHashedPassword = rs.getString("password");
                String storedSalt = rs.getString("salt");

                // Hash the entered password with the stored salt
                String hashedEnteredPassword = PasswordHasher.hashPassword(password, storedSalt);

                // Compare the hashed entered password with the stored hashed password
                if (hashedEnteredPassword.equals(storedHashedPassword)) {
                    return true; // Authentication successful
                }
            }

            return false; // Authentication failed
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    // Method to check if username exists in the database
    public boolean checkUsernameExists(String username) {
        try {
            Connection conn = getConnection();
            String query = "SELECT * FROM users WHERE username = ?";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setString(1, username);

            ResultSet rs = pstmt.executeQuery();

            // If a record is found, the username exists
            return rs.next();

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    // Method to reset user's password using the PIN
    public boolean resetPassword(String username, String pin, String newPassword) {
        try {
            Connection conn = getConnection();
            String query = "SELECT salt FROM users WHERE username = ? AND pin = ?";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setString(1, username);
            pstmt.setString(2, pin);

            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                String salt = rs.getString("salt");

                // Hash the new password with the retrieved salt
                String hashedNewPassword = PasswordHasher.hashPassword(newPassword, salt);

                String updateQuery = "UPDATE users SET password = ? WHERE username = ? AND pin = ?";
                PreparedStatement updateStmt = conn.prepareStatement(updateQuery);
                updateStmt.setString(1, hashedNewPassword);
                updateStmt.setString(2, username);
                updateStmt.setString(3, pin);

                int rowsAffected = updateStmt.executeUpdate();
                return rowsAffected > 0;
            }

            return false; // User not found or incorrect PIN
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
