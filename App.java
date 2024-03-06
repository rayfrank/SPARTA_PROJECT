import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class SpartaMusicPlayer extends Application {

    private MediaPlayer mediaPlayer;
    private List<String> playlist = new ArrayList<>();
    private int currentTrackIndex = 0;
    private ImageView albumCover;

    @Override
    public void start(Stage stage) {
        // Create buttons
        Button playButton = new Button("▶");
        playButton.setStyle("-fx-font-size: 16px; -fx-background-color: #00cb80; -fx-text-fill: white;");
        playButton.setOnAction(e -> mediaPlayer.play());

        Button pauseButton = new Button("||");
        pauseButton.setStyle("-fx-font-size: 16px; -fx-background-color: #30ccff; -fx-text-fill: white;");
        pauseButton.setOnAction(e -> mediaPlayer.pause());

        Button stopButton = new Button("■");
        stopButton.setStyle("-fx-font-size: 16px; -fx-background-color: #ff9800; -fx-text-fill: white;");
        stopButton.setOnAction(e -> mediaPlayer.stop());

        Button nextButton = new Button("➡");
        nextButton.setStyle("-fx-font-size: 16px; -fx-background-color: #2196F3; -fx-text-fill: white;");
        nextButton.setOnAction(e -> playNextTrack());

        Button prevButton = new Button("⬅");
        prevButton.setStyle("-fx-font-size: 16px; -fx-background-color: #2196F3; -fx-text-fill: white;");
        prevButton.setOnAction(e -> playPreviousTrack());

        Button addTrackButton = new Button("Add Track");
        addTrackButton.setStyle("-fx-font-size: 16px; -fx-background-color: #2989ff; -fx-text-fill: white;");
        addTrackButton.setOnAction(e -> addTrack());

        Button switchVideoButton = new Button("Switch to Video");
        switchVideoButton.setStyle("-fx-font-size: 16px; -fx-background-color: #007fd9; -fx-text-fill: white;");
        switchVideoButton.setOnAction(e -> switchToVideo());
        switchVideoButton.getStyleClass().add("custom-button");


        // Volume slider
        Slider volumeSlider = new Slider();
        volumeSlider.setMin(0);
        volumeSlider.setMax(100);
        volumeSlider.setValue(100);
        volumeSlider.valueProperty().addListener((observable, oldValue, newValue) -> mediaPlayer.setVolume(newValue.doubleValue() / 100));

        // Album cover image
        albumCover = new ImageView();
        albumCover.setFitWidth(200);
        albumCover.setFitHeight(200);

        // Layout for music player controls and album cover
        HBox musicPlayerControls = new HBox(10, playButton, pauseButton, stopButton, prevButton, nextButton);
        VBox layout = new VBox(20, albumCover, musicPlayerControls, volumeSlider, addTrackButton, switchVideoButton);
        layout.setStyle("-fx-background-color: #000554; -fx-padding: 20px;");
        layout.setPrefSize(400, 400);


        // Create the scene
        Scene scene = new Scene(layout);
        stage.setTitle("Sparta Media Player");
        stage.setScene(scene);
        stage.show();
    }

    private void addTrack() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Music File");
        File selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null) {
            playlist.add(selectedFile.toURI().toString());
            if (mediaPlayer == null) {
                mediaPlayer = new MediaPlayer(new Media(playlist.get(0)));
                mediaPlayer.play();
                updateAlbumCover();
            }
        }
    }

    private void playNextTrack() {
        if (!playlist.isEmpty()) {
            currentTrackIndex = (currentTrackIndex + 1) % playlist.size();
            mediaPlayer.stop();
            mediaPlayer = new MediaPlayer(new Media(playlist.get(currentTrackIndex)));
            mediaPlayer.play();
            updateAlbumCover();
        }
    }

    private void playPreviousTrack() {
        if (!playlist.isEmpty()) {
            currentTrackIndex = (currentTrackIndex - 1 + playlist.size()) % playlist.size();
            mediaPlayer.stop();
            mediaPlayer = new MediaPlayer(new Media(playlist.get(currentTrackIndex)));
            mediaPlayer.play();
            updateAlbumCover();
        }
    }

    private void updateAlbumCover() {
        if (!playlist.isEmpty() && currentTrackIndex < playlist.size()) {
            // Update album cover to reflect the current track
            Image newAlbumCover = new Image("file:///C:/Users/rayte/IdeaProjects/SpartaMusicPlayer/src/albumcover.jpg"); // Change the path to your album cover
            albumCover.setImage(newAlbumCover);
        }
    }

    private void switchToVideo() {
        // Select video file
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Video File");
        File selectedFile = fileChooser.showOpenDialog(null);

        if (selectedFile != null) {
            // Create media player and media
            Media media = new Media(selectedFile.toURI().toString());
            mediaPlayer = new MediaPlayer(media);

            // Create media view
            MediaView mediaView = new MediaView(mediaPlayer);

            // Create media player controls
            Button playButton = new Button("▶");
            playButton.setStyle("-fx-font-size: 16px; -fx-background-color: #4CAF50; -fx-text-fill: white;");
            playButton.setOnAction(e -> mediaPlayer.play());

            Button pauseButton = new Button("||");
            pauseButton.setStyle("-fx-font-size: 16px; -fx-background-color: #f44336; -fx-text-fill: white;");
            pauseButton.setOnAction(e -> mediaPlayer.pause());

            Button stopButton = new Button("■");
            stopButton.setStyle("-fx-font-size: 16px; -fx-background-color: #ff9800; -fx-text-fill: white;");
            stopButton.setOnAction(e -> mediaPlayer.stop());

            // Volume slider
            Slider volumeSlider = new Slider();
            volumeSlider.setMin(0);
            volumeSlider.setMax(100);
            volumeSlider.setValue(100);
            volumeSlider.valueProperty().addListener((observable, oldValue, newValue) -> mediaPlayer.setVolume(newValue.doubleValue() / 100));

            // Layout for media player controls
            HBox videoControls = new HBox(10, playButton, pauseButton, stopButton, volumeSlider);
            VBox videoLayout = new VBox(20, mediaView, videoControls);
            videoLayout.setStyle("-fx-background-color: #f0f0f0; -fx-padding: 20px;");
            videoLayout.setPrefSize(800, 600);
            videoLayout.getStyleClass().add("custom-stage");


            // Create the scene and show the video player
            Scene videoScene = new Scene(videoLayout);
            Stage videoStage = new Stage();
            videoStage.setTitle("Video Player");
            videoScene.getStylesheets().add(getClass().getResource("styles.css").toExternalForm());
            videoStage.setScene(videoScene);
            videoStage.show();

            // Play the video
            mediaPlayer.play();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}

