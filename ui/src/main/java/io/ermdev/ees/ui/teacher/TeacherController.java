package io.ermdev.ees.ui.teacher;

import io.ermdev.ees.business.Dimension;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class TeacherController implements Initializable {

    private Dimension dimension;
    private Stage stage;

    public TeacherController() {
        dimension = new Dimension();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

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

    protected void setStage(Stage stage) {
        this.stage = stage;
    }
}
