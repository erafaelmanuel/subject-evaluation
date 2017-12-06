package io.ermdev.ees.ui.login;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.util.logging.Logger;

public class LoginStage extends Stage {

    private Logger logger = Logger.getLogger(LoginStage.class.getSimpleName());

    public LoginStage(ApplicationContext context, LoginListener listener) {
        try {
            initStyle(StageStyle.UNDECORATED);
            setMinWidth(514);
            setMinHeight(370);
            setResizable(false);

            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(new ClassPathResource("fxml/login.fxml").getURL());

            Parent root = loader.load();
            Scene scene = new Scene(root, 592, 390);
            scene.getStylesheets().add(new ClassPathResource("css/login.css").getURL().toString());
            setScene(scene);

            LoginController loginController=loader.getController();
            loginController.setStage(this);
            loginController.setApplicationContext(context);
            loginController.setListener(listener);
        } catch (IOException e) {
            logger.info(e.getMessage());
        }
    }
}
