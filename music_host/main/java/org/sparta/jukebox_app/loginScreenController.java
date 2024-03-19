package org.sparta.jukebox_app;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.io.IOException;

public class loginScreenController {
    @FXML
    private TextField username_field;

    @FXML
    private PasswordField password_field;

    // Event handler for the Login button
    @FXML
    public void handleLoginButtonAction(ActionEvent event) {
        // User Authentication logic
        String username = username_field.getText();
        String password = password_field.getText();

        DatabaseConnection databaseConnection = new DatabaseConnection();
        // Call the authenticateUser method from the DatabaseConnection class
        if (databaseConnection.authenticateUser(username, password)) {
            // Close the login screen
            Stage oldStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            oldStage.close();


            // Open the musicPlayer_GUI.fxml screen
            try {
                Parent musicPlayerRoot = FXMLLoader.load(getClass().getResource("musicPlayer_GUI.fxml"));
                Stage musicPlayerStage = new Stage();
                musicPlayerStage.setTitle("Music Player");
                musicPlayerStage.setScene(new Scene(musicPlayerRoot));
                musicPlayerStage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }  else {
            // Check if username exists in the database
            if (!databaseConnection.checkUsernameExists(username)) {
                // Prompt the user to create an account
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Account Creation Required");
                alert.setHeaderText(null);
                alert.setContentText("Username does not exist in the database. Please create an account.");
                alert.showAndWait();
            } else {
                // Display an error message for incorrect password
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Login Error");
                alert.setHeaderText(null);
                alert.setContentText("Incorrect password. Please try again.");
                alert.showAndWait();
            }
        }
    }


    // Event handler for the SignUp button
    @FXML
    public void handleSignUpButtonAction(ActionEvent event) {
        try {
            Stage oldStage = (Stage) ((Node) event.getSource()).getScene().getWindow(); // Get the Stage of the login frame
            oldStage.close(); // Close the login frame
            Parent signupRoot = FXMLLoader.load(getClass().getResource("signupScreen.fxml"));
            Stage stage = new Stage();
            stage.setTitle("Sign Up");
            stage.setScene(new Scene(signupRoot));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Event handler for the Forget button
    @FXML
    public void handleForgetButtonAction(ActionEvent event) {
        try {
            Stage oldStage = (Stage) ((Node) event.getSource()).getScene().getWindow(); // Get the Stage of the login frame
            oldStage.close(); // Close the login frame
            Parent forgotRoot = FXMLLoader.load(getClass().getResource("forgetScreen.fxml"));
            Stage stage = new Stage();
            stage.setTitle("Forgot Password");
            stage.setScene(new Scene(forgotRoot));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
