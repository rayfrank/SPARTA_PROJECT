import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class SpartaMusicPlayer extends Application {

    private MediaPlayer mediaPlayer;
    private List<String> playlist = new ArrayList<>();
    private int currentTrackIndex = 0;
    private ImageView albumCover;
    private Label trackTitleLabel;
    private Label trackDurationLabel;
    private Label trackLyricsLabel;
    private Slider trackSlider;
    private ListView<String> playlistView;
    private TextField newTrackField;
    private TextArea commentsTextArea;

    @Override
    public void start(Stage stage) {
        // Create buttons
        Button playButton = createButton("▶", "#1DB954");
        playButton.setOnAction(e -> mediaPlayer.play());

        Button pauseButton = createButton("||", "#1DB954");
        pauseButton.setOnAction(e -> mediaPlayer.pause());

        Button stopButton = createButton("■", "#1DB954");
        stopButton.setOnAction(e -> mediaPlayer.stop());

        Button nextButton = createButton("➡", "#1DB954");
        nextButton.setOnAction(e -> playNextTrack());

        Button prevButton = createButton("⬅", "#1DB954");
        prevButton.setOnAction(e -> playPreviousTrack());

        Button addTrackButton = createButton("Add Track", "#1DB954");
        addTrackButton.setOnAction(e -> addTrack());

        Button switchVideoButton = createButton("Switch to Video", "#1DB954");
        switchVideoButton.setOnAction(e -> switchToVideo());

        // Track title, duration, and lyrics labels
        trackTitleLabel = createLabel("", "#FFFFFF");
        trackDurationLabel = createLabel("", "#FFFFFF");
        trackLyricsLabel = createLabel("", "#FFFFFF");

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

        // Track slider
        trackSlider = new Slider();
        trackSlider.setMin(0);

        // Layout for music player controls and album cover
        HBox musicPlayerControls = new HBox(10, prevButton, playButton, pauseButton, stopButton, nextButton);
        musicPlayerControls.setAlignment(Pos.CENTER);
        VBox layout = new VBox(20, albumCover, musicPlayerControls, trackTitleLabel, trackDurationLabel, trackLyricsLabel, trackSlider, volumeSlider, addTrackButton, switchVideoButton);
        layout.setBackground(new Background(new BackgroundFill(javafx.scene.paint.Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY)));
        layout.setPadding(new Insets(20));
        layout.setSpacing(10);
        layout.setAlignment(Pos.CENTER);

        // Create the scene
        Scene scene = new Scene(layout);
        stage.setTitle("Sparta Media Player");
        stage.setScene(scene);
        stage.show();

        // Initialize playlist view
        playlistView = new ListView<>();
        newTrackField = new TextField();

        // Layout for playlist
        VBox playlistLayout = new VBox(10, new Label("Playlist:"), playlistView, newTrackField, addTrackButton);
        playlistLayout.setAlignment(Pos.CENTER);
        layout.getChildren().add(playlistLayout);

        // Add listener to playlist view to select track
        playlistView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            int selectedIndex = playlistView.getSelectionModel().getSelectedIndex();
            if (selectedIndex != -1) {
                playTrack(selectedIndex);
            }
        });

        // Initialize comments text area
        commentsTextArea = new TextArea();
        commentsTextArea.setEditable(false);
        commentsTextArea.setWrapText(true);
        commentsTextArea.setPrefHeight(150);

        // Layout for comments
        VBox commentsLayout = new VBox(10, new Label("Comments:"), commentsTextArea);
        commentsLayout.setAlignment(Pos.CENTER);
        layout.getChildren().add(commentsLayout);

        // Add TextField for users to input comments
        TextField commentInputField = new TextField();
        commentInputField.setPromptText("Type your comment here...");
        commentInputField.setOnKeyPressed(event -> {
            if (event.getCode().equals(javafx.scene.input.KeyCode.ENTER)) {
                addComment(commentInputField.getText());
                commentInputField.clear();
            }
        });
        layout.getChildren().add(commentInputField);
    }

    private Button createButton(String text, String color) {
        Button button = new Button(text);
        button.setStyle("-fx-font-size: 16px; -fx-background-color: " + color + "; -fx-text-fill: #ffffff;");
        return button;
    }

    private Label createLabel(String text, String color) {
        Label label = new Label(text);
        label.setStyle("-fx-text-fill: " + color + ";");
        return label;
    }

    private void addTrack() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Music File");
        File selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null) {
            playlist.add(selectedFile.toURI().toString());
            playlistView.getItems().add(selectedFile.getName());
            if (mediaPlayer == null) {
                mediaPlayer = new MediaPlayer(new Media(playlist.get(0)));
                mediaPlayer.play();
                updateAlbumCover();
                updateTrackInfo(selectedFile.getName(), "Lyrics for track 1");
                updateTrackDuration();
                bindSliderToMediaPlayer();
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
            File selectedFile = new File(playlist.get(currentTrackIndex));
            updateTrackInfo(selectedFile.getName(), "Lyrics for track " + (currentTrackIndex + 1));
            updateTrackDuration();
            bindSliderToMediaPlayer();
        }
    }

    private void playPreviousTrack() {
        if (!playlist.isEmpty()) {
            currentTrackIndex = (currentTrackIndex - 1 + playlist.size()) % playlist.size();
            mediaPlayer.stop();
            mediaPlayer = new MediaPlayer(new Media(playlist.get(currentTrackIndex)));
            mediaPlayer.play();
            updateAlbumCover();
            File selectedFile = new File(playlist.get(currentTrackIndex));
            updateTrackInfo(selectedFile.getName(), "Lyrics for track " + (currentTrackIndex + 1));
            updateTrackDuration();
            bindSliderToMediaPlayer();
        }
    }

    private void updateAlbumCover() {
        if (!playlist.isEmpty() && currentTrackIndex < playlist.size()) {
            // Update album cover to reflect the current track
            Image newAlbumCover = new Image("file:///C:/Users/rayte/IdeaProjects/SpartaMusicPlayer/src/albumcover.jpg"); // Change the path to your album cover
            albumCover.setImage(newAlbumCover);
        }
    }

    private void updateTrackInfo(String trackTitle, String lyrics) {
        trackTitleLabel.setText("Track: " + trackTitle);
        trackLyricsLabel.setText("Lyrics: " + lyrics);
    }

    private void updateTrackDuration() {
        mediaPlayer.setOnReady(() -> {
            int duration = (int) mediaPlayer.getTotalDuration().toSeconds();
            String durationText = String.format("%02d:%02d",
                    TimeUnit.SECONDS.toMinutes(duration),
                    duration % 60);
            trackDurationLabel.setText("Duration: " + durationText);
            trackSlider.setMax(duration);
        });
    }

    private void bindSliderToMediaPlayer() {
        mediaPlayer.currentTimeProperty().addListener((observable, oldValue, newValue) -> {
            trackSlider.setValue(newValue.toSeconds());
        });
    }

    private void switchToVideo() {
        try {
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
                Button playButton = createButton("▶", "#1DB954");
                playButton.setOnAction(e -> mediaPlayer.play());

                Button pauseButton = createButton("||", "#1DB954");
                pauseButton.setOnAction(e -> mediaPlayer.pause());

                Button stopButton = createButton("■", "#1DB954");
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
                videoLayout.setStyle("-fx-background-color: #01004b; -fx-padding: 20px;");
                videoLayout.setPrefSize(800, 600);

                // Create the scene and show the video player
                Scene videoScene = new Scene(videoLayout);
                Stage videoStage = new Stage();
                videoStage.setTitle("Video Player");
                videoStage.setScene(videoScene);
                videoStage.show();

                // Play the video
                mediaPlayer.play();
            }
        } catch (Exception e) {
            // Handle exception
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Unable to play video");
            alert.setContentText("An error occurred while trying to play the video. Please make sure the file is accessible and in a supported format.");
            alert.showAndWait();
            e.printStackTrace();
        }
    }


    private void playTrack(int trackIndex) {
        if (!playlist.isEmpty() && trackIndex >= 0 && trackIndex < playlist.size()) {
            currentTrackIndex = trackIndex;
            mediaPlayer.stop();
            mediaPlayer = new MediaPlayer(new Media(playlist.get(currentTrackIndex)));
            mediaPlayer.play();
            updateAlbumCover();
            File selectedFile = new File(playlist.get(currentTrackIndex));
            updateTrackInfo(selectedFile.getName(), "Lyrics for track " + (currentTrackIndex + 1));
            updateTrackDuration();
            bindSliderToMediaPlayer();
        }
    }

    private void addComment(String comment)
    {
        commentsTextArea.appendText(comment + "\n");
    }

    public static void main(String[] args) {
        launch(args);
    }
}

