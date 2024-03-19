package org.sparta.jukebox_app;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.regex.Pattern;

public class signupScreenController {

    @FXML
    private TextField name_field;

    @FXML
    private TextField contact_field;

    @FXML
    private TextField email_field;

    @FXML
    private TextField pin_field;

    @FXML
    private TextField username_field;

    @FXML
    private PasswordField password1_field;

    @FXML
    private PasswordField password2_field;

    @FXML
    private Label name_label;

    @FXML
    private Label email_label;

    @FXML
    private Label username_label;

    @FXML
    private Label contact_label;

    @FXML
    private Label pin_label;

    @FXML
    private Label password1_label;

    @FXML
    private Label password2_label;

    // Event handler for the Login button
    @FXML
    public void handleSignupButtonAction(ActionEvent event) {
    // Get user input from text fields
        String names = name_field.getText();
        String username = username_field.getText();
        String contact = contact_field.getText();
        String email = email_field.getText();
        String pin = pin_field.getText();
        String password1 = password1_field.getText();
        String password2 = password2_field.getText();

        // Validate user input
        if (names.isEmpty() || username.isEmpty() || contact.isEmpty() || email.isEmpty() || pin.isEmpty() || password1.isEmpty() || password2.isEmpty()) {
            displayErrorAlert("Please fill in all the fields.");
            return;
        }

        if (!Pattern.matches("\\d{5}", pin)) {
            displayErrorAlert("PIN must be a 5-digit number.");
            return;
        }

        if (!Pattern.matches("\\d{10}", contact)) {
            displayErrorAlert("Contact must be a 10-digit number.");
            return;
        }

        if (!Pattern.matches("^[A-Za-z0-9+_.-]+@(.+)$", email)) {
            displayErrorAlert("Invalid email format.");
            return;
        }

        if (username.length() < 8) {
            displayErrorAlert("Username must be at least 8 characters long.");
            return;
        }

        if (password1.length() < 8 || !Pattern.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@#$%^&+=])[A-Za-z\\d@#$%^&+=]{8,}$", password1)) {
            displayErrorAlert("Password must be at least 8 characters long and include uppercase letters, lowercase letters, numbers, and symbols.");
            return;
        }

        if (!password1.equals(password2)) {
            displayErrorAlert("Passwords do not match.");
            return;
        }

        // Save user details to the database
        DatabaseConnection databaseConnection = new DatabaseConnection();
        boolean success = databaseConnection.saveUserDetails(username, password1, email, names, pin, contact);

        if (success) {
            System.out.println("User details saved successfully");

            // Close the signup frame
            Stage oldStage = (Stage) ((Node) event.getSource()).getScene().getWindow(); // Get the Stage of the login frame
            oldStage.close();

            // Open the musicPlayer_GUI.fxml
            Parent musicPlayerRoot;
            try {
                musicPlayerRoot = FXMLLoader.load(getClass().getResource("musicPlayer_GUI.fxml"));
                Stage musicPlayerStage = new Stage();
                musicPlayerStage.setTitle("Music Player");
                musicPlayerStage.setScene(new Scene(musicPlayerRoot));
                musicPlayerStage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Error saving user details to the database");
        }
        }

        // Event handler for the Exit button
        @FXML
        public void handleExitButtonAction (ActionEvent event){

            Platform.exit(); // Exit the application
        }

        // Event handler for the SignUp button
        @FXML
        public void handleLoginButtonAction (ActionEvent event){
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
    private void displayErrorAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}




