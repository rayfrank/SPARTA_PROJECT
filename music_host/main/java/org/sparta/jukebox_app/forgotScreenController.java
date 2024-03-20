package org.sparta.jukebox_app;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class forgotScreenController {
    @FXML
    private TextField username_field;

    @FXML
    private PasswordField password_field;

    @FXML
    private AnchorPane rootPane;

    // Event handler for the Login button
    @FXML
    public void handleSubmitButtonAction(ActionEvent event) {
        // Authentication logic
        String username = username_field.getText();
        String newPassword = password_field.getText();

        if (username.isEmpty() || newPassword.isEmpty()) {
            showNotification("Please enter username and new password.");
            return;
        }

        DatabaseConnection dbConnection = new DatabaseConnection();

        if (dbConnection.checkUsernameExists(username)) {
            // Assuming user needs to enter their PIN (you can add PIN input field)
            String pin = ""; // Retrieve user's PIN from the database using username

            if (dbConnection.resetPassword(username, pin, newPassword)) {
                showNotification("Password reset successfully.");
            } else {
                showNotification("Password reset failed. Please check your username and PIN.");
            }
        } else {
            showNotification("Username not found in the database.");
        }
    }

    // Method to show toast notification messages
    private void showNotification(String message) {
        Toast toast = new Toast(message);
        rootPane.getChildren().add(toast); // Add toast to the rootPane
    }

       // Event handler for the SignUp button
    @FXML
    public void handleReturnButtonAction(ActionEvent event) {
        try {
            Stage oldStage = (Stage) ((Node) event.getSource()).getScene().getWindow(); // Get the Stage of the login frame
            oldStage.close(); // Close the login frame
            Parent signupRoot = FXMLLoader.load(getClass().getResource("loginScreen.fxml"));
            Stage stage = new Stage();
            stage.setTitle("Log In");
            stage.setScene(new Scene(signupRoot));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
