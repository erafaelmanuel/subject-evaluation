package io.ermdev.ees.ui.login;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.util.logging.Logger;

public class LoginStage extends Stage {

    private Logger logger = Logger.getLogger(LoginStage.class.getSimpleName());

    public LoginStage() {
        try {
            initStyle(StageStyle.UNDECORATED);
            setTitle("Login");
            setMinWidth(514);
            setMinHeight(370);
            setResizable(false);

            Parent root = FXMLLoader.load(new ClassPathResource("fxml/login.fxml").getURL());
            Scene scene = new Scene(root, 592, 390);
            scene.getStylesheets().add(new ClassPathResource("css/login.css").getURL().toString());
            setScene(scene);
        } catch (IOException e) {
            logger.info(e.getMessage());
        }
    }
}
