package classifyx.ui.login;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class LoginDialogController implements Initializable {

    @FXML
    ImageView imgv;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        imgv.setImage(new Image(getClass().getResource("/image/warning.png").toExternalForm()));
    }

    @FXML
    void onClose(ActionEvent event) {
        ((Stage) ((Node) event.getSource()).getScene().getWindow()).close();
    }
}
