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
            stage.initStyle(StageStyle.UTILITY);

            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(new ClassPathResource("/fxml/login_dialog.fxml").getURL());

            Parent root = loader.load();
            Scene scene = new Scene(root, 385, 173);
            scene.setFill(Color.TRANSPARENT);
            scene.getStylesheets().add(new ClassPathResource("/css/login.css").getURL().toString());

            stage.setTitle("Error");
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
