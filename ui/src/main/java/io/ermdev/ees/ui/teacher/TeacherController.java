package io.ermdev.ees.ui.teacher;

import io.ermdev.ees.business.Dimension;
import javafx.animation.*;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.net.URL;
import java.util.ResourceBundle;

public class TeacherController implements Initializable {

    private Dimension dimension;
    private Stage stage;

    @FXML
    private VBox sideLayout;

    @FXML
    private Label lbEvaluate;

    @FXML
    private Label lbDropSubject;

    @FXML
    private VBox boxProfile;

    public TeacherController() {
        dimension = new Dimension();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {}

    @FXML
    public void onPressedTitle(MouseEvent m) {
        if(m.isPrimaryButtonDown()) {
            dimension.setX(m.getSceneX());
            dimension.setY(m.getSceneY());
        }
    }

    @FXML
    public void onDraggedTitle(MouseEvent m) {
        if(m.isPrimaryButtonDown()) {
            stage.setX(m.getScreenX() - dimension.getX());
            stage.setY(m.getScreenY() - dimension.getY());
        }
    }

    @FXML
    public void onClickedClose() {
        stage.close();
    }

    @FXML
    public void onClickedMinimize() {
        stage.setIconified(true);
    }

    @FXML
    public void onEnteredSideLayout() {
        sideLayout.setMaxWidth(160);
        Timeline timeline = new Timeline();
        timeline.getKeyFrames().add(new KeyFrame(Duration.millis(100),
                new KeyValue(sideLayout.prefWidthProperty(), 160)));
        timeline.getKeyFrames().add(new KeyFrame(Duration.millis(100),
                new KeyValue(lbEvaluate.visibleProperty(), true)));
        timeline.getKeyFrames().add(new KeyFrame(Duration.millis(100),
                new KeyValue(lbDropSubject.visibleProperty(), true)));
        timeline.getKeyFrames().add(new KeyFrame(Duration.millis(100),
                new KeyValue(boxProfile.visibleProperty(), true)));
        timeline.getKeyFrames().add(new KeyFrame(Duration.millis(100),
                new KeyValue(boxProfile.paddingProperty(), new Insets(5, 0, 0, 10))));
        timeline.play();
    }

    @FXML
    public void onExitedSideLayout() {
        Timeline timeline = new Timeline();
        timeline.getKeyFrames().add(new KeyFrame(Duration.millis(50),
                new KeyValue(sideLayout.prefWidthProperty(), 56)));
        timeline.getKeyFrames().add(new KeyFrame(Duration.millis(50),
                new KeyValue(lbEvaluate.visibleProperty(), false)));
        timeline.getKeyFrames().add(new KeyFrame(Duration.millis(50),
                new KeyValue(lbDropSubject.visibleProperty(), false)));
        timeline.getKeyFrames().add(new KeyFrame(Duration.millis(50),
                new KeyValue(boxProfile.visibleProperty(), false)));
        timeline.getKeyFrames().add(new KeyFrame(Duration.millis(50),
                new KeyValue(boxProfile.paddingProperty(), new Insets(0))));
        timeline.play();
    }

    protected void setStage(Stage stage) {
        this.stage = stage;
    }
}
