package io.ermdev.ees.ui.teacher;

import io.ermdev.ees.business.Dimension;
import javafx.animation.*;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
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
        sideLayout.setMaxWidth(130);
        Timeline timeline = new Timeline();
        timeline.getKeyFrames().add(new KeyFrame(Duration.millis(200), new KeyValue(sideLayout.prefWidthProperty(), 130)));
        timeline.getKeyFrames().add(new KeyFrame(Duration.millis(200), new KeyValue(lbEvaluate.visibleProperty(), true)));
        timeline.play();
    }

    @FXML
    public void onExitedSideLayout() {
        sideLayout.setMaxWidth(55);
        sideLayout.setPrefWidth(55);
        lbEvaluate.setVisible(false);
    }

    protected void setStage(Stage stage) {
        this.stage = stage;
    }
}
