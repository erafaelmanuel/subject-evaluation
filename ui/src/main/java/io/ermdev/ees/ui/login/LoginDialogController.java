package io.ermdev.ees.ui.login;

import io.ermdev.ees.commons.util.Dimension;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class LoginDialogController {

    private Dimension dimension;

    public LoginDialogController() {
        dimension = new Dimension();
    }

    @FXML
    public void onClickClose(MouseEvent event) {
        Stage stage = (Stage) (((Node) event.getSource()).getScene().getWindow());
        stage.close();
    }

    @FXML
    public void onPressedDialog(MouseEvent m) {
        if(m.isPrimaryButtonDown()) {
            dimension.setX(m.getSceneX());
            dimension.setY(m.getSceneY());
        }
    }

    @FXML
    public void onDraggedDialog(MouseEvent event) {
        if(event.isPrimaryButtonDown()) {
            Stage stage = (Stage) (((Node) event.getSource()).getScene().getWindow());
            stage.setX(event.getScreenX() - dimension.getX());
            stage.setY(event.getScreenY() - dimension.getY());
        }
    }

}
