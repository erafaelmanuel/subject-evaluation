package io.ermdev.ees.ui.login;

import io.ermdev.ees.business.login.LoginListener;
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

    private static final Logger LOGGER = Logger.getLogger(LoginStage.class.getSimpleName());

    public LoginStage(ApplicationContext context, LoginListener listener) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(new ClassPathResource("fxml/login.fxml").getURL());

            Parent root = loader.load();
            Scene scene = new Scene(root, 592, 390);
            scene.getStylesheets().add(new ClassPathResource("css/login_style.css").getURL().toString());

            initStyle(StageStyle.UNDECORATED);
            setMinWidth(592);
            setMinHeight(390);
            setResizable(false);
            setScene(scene);

            LoginController controller=loader.getController();
            controller.setStage(this);
            controller.setApplicationContext(context);
            controller.setListener(listener);
        } catch (IOException e) {
            LOGGER.info(e.getMessage());
        }
    }
}
