package classifyx.ui.teacher;

import classifyx.commons.Dimension;
import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
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

    @FXML
    private StackPane buttonMinimize;

    @FXML
    private StackPane buttonClose;

    public TeacherController() {
        dimension = new Dimension();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    @FXML
    public void onPressedTitle(MouseEvent m) {
        if (m.isPrimaryButtonDown()) {
            dimension.setX(m.getSceneX());
            dimension.setY(m.getSceneY());
        }
    }

    @FXML
    public void onDraggedTitle(MouseEvent m) {
        if (m.isPrimaryButtonDown()) {
            stage.setX(m.getScreenX() - dimension.getX());
            stage.setY(m.getScreenY() - dimension.getY());
        }
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

    @FXML
    public void onMouseClickedClose(MouseEvent event) {
        final Stage window;
        if (stage != null) {
            window = stage;
        } else {
            window = ((Stage) ((Node) event.getSource()).getScene().getWindow());
        }
        AnimationTimer timer = new AnimationTimer() {
            int count = 2;

            @Override
            public void handle(long now) {
                if (--count <= 0) {
                    window.close();
                    this.stop();
                }
            }
        };
        buttonMinimize.setStyle("-fx-background-color: #0366d6;");
        timer.start();
    }

    @FXML
    public void onMouseClickedMinimize(MouseEvent event) {
        final Stage window;
        if (stage != null) {
            window = stage;
        } else {
            window = ((Stage) ((Node) event.getSource()).getScene().getWindow());
        }
        AnimationTimer timer = new AnimationTimer() {
            int count = 2;

            @Override
            public void handle(long now) {
                if (--count <= 0) {
                    window.setIconified(true);
                    this.stop();
                }
            }
        };
        buttonMinimize.setStyle("-fx-background-color: #0366d6;");
        timer.start();
    }

    @FXML
    public void onMouseEnteredMinimize() {
        buttonMinimize.setStyle("-fx-background-color: #045cb5;");
    }


    @FXML
    public void onMouseExitedMinimize() {
        buttonMinimize.setStyle("-fx-background-color: #0366d6;");
    }

    @FXML
    public void onMouseEnteredClose() {
        buttonClose.setStyle("-fx-background-color: #045cb5;");
    }


    @FXML
    public void onMouseExitedClose() {
        buttonClose.setStyle("-fx-background-color: #0366d6;");
    }

    protected void setStage(Stage stage) {
        this.stage = stage;
    }
}
