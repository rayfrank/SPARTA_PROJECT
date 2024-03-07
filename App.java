import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
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
    private Label trackTitleLabel;
    private Label trackDurationLabel;
    private Slider trackProgressSlider;

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

        // Labels for track title and duration
        trackTitleLabel = new Label();
        trackTitleLabel.setStyle("-fx-text-fill: white;");
        trackDurationLabel = new Label();
        trackDurationLabel.setStyle("-fx-text-fill: white;");

        // Slider for track progress
        trackProgressSlider = new Slider();
        trackProgressSlider.setMin(0);
        trackProgressSlider.setMax(100);

        // Handle track progress slider changes
        trackProgressSlider.valueProperty().addListener((obs, oldValue, newValue) -> {
            if (mediaPlayer != null) {
                mediaPlayer.seek(mediaPlayer.getTotalDuration().multiply(newValue.doubleValue() / 100));
            }
        });

        // Layout for music player controls, album cover, and track information
        HBox musicPlayerControls = new HBox(10, playButton, pauseButton, stopButton, prevButton, nextButton);
        VBox trackInfo = new VBox(5, trackTitleLabel, trackDurationLabel, trackProgressSlider); // Added track progress slider
        VBox layout = new VBox(20, albumCover, trackInfo, musicPlayerControls, volumeSlider, addTrackButton);
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
                updateTrackInfo(selectedFile.getName());
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
            updateTrackInfo(new File(playlist.get(currentTrackIndex)).getName());
        }
    }

    private void playPreviousTrack() {
        if (!playlist.isEmpty()) {
            currentTrackIndex = (currentTrackIndex - 1 + playlist.size()) % playlist.size();
            mediaPlayer.stop();
            mediaPlayer = new MediaPlayer(new Media(playlist.get(currentTrackIndex)));
            mediaPlayer.play();
            updateAlbumCover();
            updateTrackInfo(new File(playlist.get(currentTrackIndex)).getName());
        }
    }

    private void updateAlbumCover() {
        if (!playlist.isEmpty() && currentTrackIndex < playlist.size()) {
            // Update album cover to reflect the current track
            Image newAlbumCover = new Image("file:///C:/Users/rayte/IdeaProjects/SpartaMusicPlayer/src/albumcover.jpg"); // Change the path to your album cover
            albumCover.setImage(newAlbumCover);
        }
    }

    private void updateTrackInfo(String trackName) {
        // Set track title
        trackTitleLabel.setText("Title: " + trackName);

        // Set track duration (if available)
        if (mediaPlayer != null) {
            mediaPlayer.setOnReady(() -> {
                int duration = (int) mediaPlayer.getTotalDuration().toSeconds();
                int minutes = duration / 60;
                int seconds = duration % 60;
                String durationString = String.format("Duration: %02d:%02d", minutes, seconds);
                trackDurationLabel.setText(durationString);
            });
        }

        // Update track progress slider
        mediaPlayer.currentTimeProperty().addListener((obs, oldValue, newValue) -> {
            if (!trackProgressSlider.isValueChanging()) {
                trackProgressSlider.setValue(newValue.toSeconds() / mediaPlayer.getTotalDuration().toSeconds() * 100);
            }
        });
    }

    public static void main(String[] args) {
        launch(args);
    }
}
