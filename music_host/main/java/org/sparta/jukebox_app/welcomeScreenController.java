package org.sparta.jukebox_app;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.util.Duration;
import javafx.scene.layout.AnchorPane;


public class welcomeScreenController {
    // Define a reference to the anchor pane in welcomeScreen.fxml

    @FXML
    private AnchorPane welcomeAnchorPane;

    @FXML
    private void initialize() {
        // Create a timeline that triggers an event after 2 seconds
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(2), this::loadLoginScreen));
        timeline.play();
    }

    private void loadLoginScreen(ActionEvent event) {
        // Load the login screen here
        // You can use FXMLLoader to load the login screen FXML file
    }
}
