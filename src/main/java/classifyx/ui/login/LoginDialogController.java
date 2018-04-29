package classifyx.ui.login;

import javafx.animation.AnimationTimer;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class LoginDialogController {


    @FXML
    private StackPane buttonClose;

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

    }

    @FXML
    public void onMouseDraggedTitle(MouseEvent event) {

    }

    @FXML
    public void onMouseClickedClose(MouseEvent event) {
        final Stage window;
        window = ((Stage) ((Node) event.getSource()).getScene().getWindow());
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
