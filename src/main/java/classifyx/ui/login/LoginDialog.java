package classifyx.ui.login;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;

public class LoginDialog {

    public void displayError() {
        try {
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initStyle(StageStyle.UNDECORATED);
            stage.initStyle(StageStyle.TRANSPARENT);

            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(new ClassPathResource("/fxml/login_dialog.fxml").getURL());

            Parent root = loader.load();
            Scene scene = new Scene(root, 385, 122);
            scene.setFill(Color.TRANSPARENT);
            scene.getStylesheets().add(new ClassPathResource("/css/login_dialog_style.css").getURL().toString());

            stage.setScene(scene);
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
