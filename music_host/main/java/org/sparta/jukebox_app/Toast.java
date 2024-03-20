package org.sparta.jukebox_app;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.util.Duration;

public class Toast extends VBox {
    private static final double TOAST_WIDTH = 500;
    private static final double TOAST_HEIGHT = 40;

    public Toast(String message) {
        Label label = new Label(message);
        label.setFont(Font.font("Arial", 14));
        label.setTextFill(Color.WHITE);

        setPrefWidth(TOAST_WIDTH);
        setPrefHeight(TOAST_HEIGHT);
        setAlignment(Pos.CENTER);
        setStyle("-fx-background-color: #333333; -fx-background-radius: 10px; -fx-border-color: #cccccc; -fx-border-radius: 10px; -fx-border-width: 1px;");

        getChildren().add(label);

        animateToast();
    }

    private void animateToast() {
        Timeline timeline = new Timeline();
        timeline.getKeyFrames().addAll(
                new KeyFrame(Duration.ZERO, new KeyValue(opacityProperty(), 0.0)),
                new KeyFrame(Duration.seconds(1), new KeyValue(opacityProperty(), 1.0)),
                new KeyFrame(Duration.seconds(4), new KeyValue(opacityProperty(), 1.0)),
                new KeyFrame(Duration.seconds(5), new KeyValue(opacityProperty(), 0.0))
        );

        timeline.play();
    }
}
