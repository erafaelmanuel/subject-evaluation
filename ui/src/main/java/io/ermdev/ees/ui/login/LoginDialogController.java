package io.ermdev.ees.ui.login;

import io.ermdev.ees.business.Dimension;
import javafx.animation.AnimationTimer;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class LoginDialogController {

    private Dimension dimension;

    @FXML
    private StackPane buttonClose;

    public LoginDialogController() {
        dimension = new Dimension();
    }

    @FXML
    public void onActionOK(ActionEvent event) {
        ((Stage) ((Node) event.getSource()).getScene().getWindow()).close();
    }

    @FXML
    public void onClickClose(MouseEvent event) {
        Stage stage = (Stage) (((Node) event.getSource()).getScene().getWindow());
        stage.close();
    }

    @FXML
    public void onMousePressedTitle(MouseEvent m) {
        if(m.isPrimaryButtonDown()) {
            dimension.setX(m.getSceneX());
            dimension.setY(m.getSceneY());
        }
    }

    @FXML
    public void onMouseDraggedTitle(MouseEvent event) {
        if(event.isPrimaryButtonDown()) {
            Stage stage = (Stage) (((Node) event.getSource()).getScene().getWindow());
            stage.setX(event.getScreenX() - dimension.getX());
            stage.setY(event.getScreenY() - dimension.getY());
        }
    }

    @FXML
    public void onMouseClickedClose(MouseEvent event) {
        final Stage window;
        window=((Stage) ((Node) event.getSource()).getScene().getWindow());
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
        buttonClose.setStyle("-fx-background-color: #0366d6;");
        timer.start();
    }

    @FXML
    public void onMouseEnteredClose() {
        buttonClose.setStyle("-fx-background-color: #045cb5;");
    }


    @FXML
    public void onMouseExitedClose() {
        buttonClose.setStyle("-fx-background-color: #0366d6;");
    }

}
