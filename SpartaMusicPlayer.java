package com.example.spartamusicplayer;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;

public class SpartaMusicPlayer extends Application {

    private MediaPlayer mediaPlayer;

    @Override
    public void start(Stage stage) throws IOException {
        // Path to your music file
        String musicFile = "C:\\Users\\rayte\\IdeaProjects\\SpartaMusicPlayer\\src\\Never gonna give you up.mp3";
        Media sound = new Media(new File(musicFile).toURI().toString());
        mediaPlayer = new MediaPlayer(sound);

        // Play button
        Button playButton = new Button("▶");
        playButton.setStyle("-fx-font-size: 16px; -fx-background-color: #4CAF50; -fx-text-fill: white;");
        playButton.setOnAction(e -> mediaPlayer.play());

        // Pause button
        Button pauseButton = new Button("||");
        pauseButton.setStyle("-fx-font-size: 16px; -fx-background-color: #f44336; -fx-text-fill: white;");
        pauseButton.setOnAction(e -> mediaPlayer.pause());

        // Stop button
        Button stopButton = new Button("■");
        stopButton.setStyle("-fx-font-size: 16px; -fx-background-color: #ff9800; -fx-text-fill: white;");
        stopButton.setOnAction(e -> mediaPlayer.stop());

        // Volume slider
        Slider volumeSlider = new Slider();
        volumeSlider.setMin(0);
        volumeSlider.setMax(100);
        volumeSlider.setValue(100);
        volumeSlider.valueProperty().addListener((observable, oldValue, newValue) -> mediaPlayer.setVolume(newValue.doubleValue() / 100));

        // Layout for music player controls
        HBox musicPlayerControls = new HBox(10, playButton, pauseButton, stopButton);
        VBox layout = new VBox(20, musicPlayerControls, volumeSlider);
        layout.setStyle("-fx-background-color: #f0f0f0; -fx-padding: 20px;");
        layout.setPrefSize(320, 240);

        // Create the scene
        Scene scene = new Scene(layout);
        stage.setTitle("Sparta Music Player");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args)
    {
        launch(args);
    }
}

